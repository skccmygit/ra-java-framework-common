package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.adaptor.client.MessagingClient;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto;
import com.skcc.ra.account.api.dto.requestDto.AccountRejectReqDto;
import com.skcc.ra.account.api.dto.requestDto.SimpleSendDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.api.type.MsgFormType;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.account.AccountReqMgmt;
import com.skcc.ra.account.domain.auth.Agent;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.service.AccountRequestService;
import com.skcc.ra.account.service.AccountService;
import com.skcc.ra.account.service.MessageSendService;
import com.skcc.ra.common.api.dto.domainDto.CmmnCdDtlDto;
import com.skcc.ra.common.api.dto.domainDto.UserBasicDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AccountRequestServiceImpl implements AccountRequestService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountReqMgmtRepository accountReqMgmtRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    UserRoleHistRepository userRoleHistRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    CommonClient commonClient;

    @Autowired
    MessagingClient messagingClient;

    @Autowired
    MessageSendService messageSendService;


    public boolean invalidCheck(String userid){
        //계정 테이블 확인
        Optional<Account> oAccount = accountRepository.findByUserid(userid);
        if(oAccount.isPresent()){
            if(!"R".equals(oAccount.get().getUseridStsCd()) && !"D".equals(oAccount.get().getUseridStsCd()))
                return true;
        }
        //신청 테이블 확인
        Optional<AccountReqMgmt> oAccountReqMgmt = accountReqMgmtRepository.findByUserid(userid);
        if(oAccountReqMgmt.isPresent()){
            if(!"R".equals(oAccountReqMgmt.get().getUseridReqstStsCd()))
                return true;
        }
        return false;
    }
    @Override
    public AccountDto searchUserBasicByUserid(String empno) {

        empno = empno.toUpperCase();

        // 사번 신청 내역이 존재하는지 확인하는 로직.
        //계정 테이블 확인
        if(this.invalidCheck(empno))    throw new ServiceException("COM.I1024");

        //사번이 BSS에 존재하는지 확인
        List<UserBasicDto> userBasicDtoList = commonClient.searchUserBasic(null,null, empno);
        if(userBasicDtoList.isEmpty())  throw new ServiceException("COM.I1006");

        AccountDto accountDto = new AccountDto();

        accountDto.setUserid(userBasicDtoList.get(0).getEmpno());
        accountDto.setUserNm(userBasicDtoList.get(0).getEmpKrnNm());
        accountDto.setUserContPhno(userBasicDtoList.get(0).getMphno());

        return accountDto;
    }

    //정직원 계정신청
    @Override
    public Boolean requestAccount(AccountDto accountDto) {
        String userid = accountDto.getUserid().toUpperCase();

        if(this.invalidCheck(userid))    throw new ServiceException("COM.I1024");

        //사원정보 조회
        List<UserBasicDto> userBasicDtoList = commonClient.searchUserBasic(null,null, userid);
        if(userBasicDtoList.isEmpty()) { throw new ServiceException("COM.I1006"); }

        //사용자신청 테이블 INSERT
        saveAccountRequest(userid, "Q", accountDto.getReqstResonCntnt());

        //사용자기본 테이블 INSERT
        Account account = new Account();

        account.setUserid(userid);
        account.setUserNm(userBasicDtoList.get(0).getEmpKrnNm());
        account.setConnPsswd(passwordEncoder.encode(account.getUserid()));
        account.setPsswdExpirDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        account.setUserContPhno(userBasicDtoList.get(0).getMphno());
        account.setUserEmailaddr(userBasicDtoList.get(0).getEmailaddr());
        account.setDeptcd(userBasicDtoList.get(0).getDeptcd());
        account.setUserGroupCd("1");
        account.setInnerUserClCd("0");
        account.setReofoCd(userBasicDtoList.get(0).getReofoCd());
        account.setUserIdentNo(userid);
        account.setUseridStsCd("W");
        account.setPsswdErrFrqy(0);
        account.setFstRegDtmd(LocalDateTime.now());

        account = accountRepository.save(account);

        //계정상태 변경 로그 db
        accountService.accountStsChngLog(account.getUserid(), "USERID_STS_CD", "W");

        //문자보내기 모듈
        List<SimpleSendDto> simpleSendDtoList = new ArrayList<>();

        List<String> params = new ArrayList<>();
        params.add(account.getUserNm() + "(" + account.getUserid()+ ")");

        //사용자 문자
        simpleSendDtoList.add(new SimpleSendDto(account.getUserContPhno(), MsgFormType.ACCOUNT_REQUEST_FINISH.getCode(), params));

        // 검토자 찾기
        UserBasicDto userBasicDto = commonClient.searchTeamLeader(account.getDeptcd(), null);

        // 계정검토문자
        if(userBasicDto != null) {
            simpleSendDtoList.add(new SimpleSendDto(userBasicDto.getMphno(), MsgFormType.ACCOUNT_CONFIRM_APPLY.getCode(), params));
        }
        messageSendService.sendSmsList(simpleSendDtoList);
        return true;
    }

    //사용자ID신청목록 테이블 저장
    public void saveAccountRequest(String userid, String useridReqstStsCd, String requestResonCntnt){

        AccountReqMgmt accountReqMgmt = new AccountReqMgmt();

        accountReqMgmt.setUserid(userid);
        accountReqMgmt.setUseridReqstStsCd(useridReqstStsCd);
        accountReqMgmt.setUseridReqstDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        accountReqMgmt.setReqstResonCntnt(requestResonCntnt);

        accountReqMgmtRepository.save(accountReqMgmt);
    }

    //전체 계정 신청 조건검색
    @Override
    public Page<AccountReqMgmtDto> searchAccountRequestByCondition(String userNm, String useridReqstStsCd, String useridReqstDtFrom, String useridReqstDtTo, Pageable pageable) {

        userNm = userNm == null ? "" : userNm;
        useridReqstStsCd = useridReqstStsCd == null ? "" : useridReqstStsCd;
        useridReqstDtFrom = StringUtils.isBlank(useridReqstDtFrom) ? "00000000" : useridReqstDtFrom;
        useridReqstDtTo = StringUtils.isBlank(useridReqstDtTo) ? "99991231" : useridReqstDtTo;

        String userid = RequestUtil.getLoginUserid();
        List<String> userRoles = RequestUtil.getLoginUserRoleList();

        Account account = accountRepository.getById(userid);

        int flag = 0;
        if(userRoles == null)   throw new ServiceException("COM.I1016");

        if(userRoles.contains(AuthType.AUTH_CONFIRM.getCode()))  flag = 1;
        if(userRoles.contains(AuthType.IT_ADMIN.getCode()) || userRoles.contains(AuthType.AUTH_APPROVE.getCode()))  flag = 2;

        Page<AccountReqMgmtDto> accountReqMgmts;
        List<CmmnCdDtlDto> innerUserClCd = commonClient.findByCmmnCd("INNER_USER_CL_CD");
        List<String> allList = innerUserClCd.stream().map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());
        List<String> ctcList = innerUserClCd.stream().filter(i -> "CTC".equals(i.getRefrnAttrVal1())).map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());
        List<String> othList = innerUserClCd.stream().filter(i -> !"CTC".equals(i.getRefrnAttrVal1())).map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());

        //검토자인 경우
        if(flag == 1){
            String myDept = account.getDeptcd();
            List<String> deptcdList = new ArrayList<>();
            List<Agent> agentList = agentRepository.findByAgentIdAndEndYn(account.getUserid(), "N");
            if(!agentList.isEmpty()){
                deptcdList = agentList.stream().map(Agent::getDeptcd).collect(Collectors.toList());
            }

            accountReqMgmts = accountReqMgmtRepository.searchAccountRequestByCondition(userNm,
                    useridReqstStsCd,
                    useridReqstDtFrom,
                    useridReqstDtTo,
                    othList,
                    myDept,
                    deptcdList,
                    pageable);

            return accountReqMgmts;
        }
        //승인자/IT담당자인 경우
        else if(flag == 2){
            accountReqMgmts = accountReqMgmtRepository.searchAccountRequestByCondition(
                    userNm,
                    useridReqstStsCd,
                    useridReqstDtFrom,
                    useridReqstDtTo,
                    allList,
                    "",
                    null,
                    pageable);

            return accountReqMgmts;
        }else{
            throw new ServiceException("COM.I1040");
        }
    }

    //사용자계정 검토
    @Override
    public Boolean confirmAccountRequest(List<Integer> useridReqstSeqs) {

        List<SimpleSendDto> simpleSendDtoList = new ArrayList<>();
        List<AccountReqMgmt> accountReqMgmtList = new ArrayList<>();
        for(Integer id : useridReqstSeqs) {
            //관리자 검토 정보 업데이트
            AccountReqMgmt accountReqMgmt = accountReqMgmtRepository.getById(id);

            if("A".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1005"); //승인
            else if("C".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1003"); //검토
            else if("R".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1023"); //반려
            else if (!"Q".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1022"); //신청 상태 아니면

            accountReqMgmt.setRvwDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            accountReqMgmt.setUseridReqstStsCd("C");
            accountReqMgmt.setRvwUserid(RequestUtil.getLoginUserid());
            accountReqMgmtList.add(accountReqMgmt);

            List<String> params = new ArrayList<>();
            Account account = accountRepository.getById(accountReqMgmt.getUserid());
            params.add(account.getUserNm() + "(" + account.getUserid() + ")");

            //사용자 문자
            simpleSendDtoList.add(new SimpleSendDto(account.getUserContPhno(), MsgFormType.ACCOUNT_CONFIRM_FINISH.getCode(), params));
        }

        accountReqMgmtRepository.saveAll(accountReqMgmtList);
        messageSendService.sendSmsList(simpleSendDtoList);

        return true;
    }

    //사용자계정 승인
    @Override
    public Boolean approveAccountRequest(List<Integer> useridReqstSeqs) throws UnknownHostException {

        List<SimpleSendDto> simpleSendDtoList = new ArrayList<>();

        for(Integer id : useridReqstSeqs) {

            AccountReqMgmt accountReqMgmt = accountReqMgmtRepository.getById(id);

            //예외처리
            if("A".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1005"); //승인
            else if("R".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1023"); //반려
            else if ("Q".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1002"); //신청
            else if (!"C".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1022");

            //IT 승인 정보 업데이트
            accountReqMgmt.setAuthzUserid(RequestUtil.getLoginUserid());
            accountReqMgmt.setAuthzUserIpaddr(RequestUtil.getClientIP());
            accountReqMgmt.setAuthzDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            accountReqMgmt.setUseridReqstStsCd("A");
            accountReqMgmtRepository.save(accountReqMgmt);

            //IT 승인 후, 계정(Account) 테이블에 Update
            Account account = accountRepository.getById(accountReqMgmt.getUserid());

            //비밀번호변경필요 상태로 변경
            if(account.getInnerUserClCd().equals("1")) account.setUseridStsCd("T");
            else account.setUseridStsCd("O");

            account.setFstRegDtmd(LocalDateTime.now());
            //임시비밀번호 생성
            account.setConnPsswd(passwordEncoder.encode(accountReqMgmt.getUserid()));
            account = accountRepository.save(account);

            // 기본역할 세팅
            basicUserRoleSetting(account.getUserid());

            //계정상태 변경 로그 db
            accountService.accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());

            List<String> params = new ArrayList<>();
            params.add(account.getUserNm() + "(" + account.getUserid() + ")");
            params.add(accountReqMgmt.getUserid());
            //사용자 문자
            simpleSendDtoList.add(new SimpleSendDto(account.getUserContPhno(), MsgFormType.ACCOUNT_APPROVE_FINISH.getCode(), params));
        }
        messageSendService.sendSmsList(simpleSendDtoList);
        return true;
    }

    //사용자계정 반려
    @Override
    public Boolean rejectAccountRequest(List<AccountRejectReqDto> accountRejectReqDtos) {

        List<SimpleSendDto> simpleSendDtoList = new ArrayList<>();

        for(AccountRejectReqDto dto : accountRejectReqDtos) {

            AccountReqMgmt accountReqMgmt = accountReqMgmtRepository.getById(dto.getUseridReqstSeq());
            Account account = accountRepository.getById(accountReqMgmt.getUserid());

            if ("A".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1005");
            else if("R".equals(accountReqMgmt.getUseridReqstStsCd())) throw new ServiceException("COM.I1023");
            else if ("Q".equals(accountReqMgmt.getUseridReqstStsCd())) {
                accountReqMgmt.setRvwUserid(RequestUtil.getLoginUserid());
                accountReqMgmt.setRvwDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));}
            else if ("C".equals(accountReqMgmt.getUseridReqstStsCd())) {
                accountReqMgmt.setAuthzUserid(RequestUtil.getLoginUserid());
                accountReqMgmt.setAuthzDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));}
            else throw new ServiceException("COM.E0004");

            //반려 후, 계정(Account) 테이블에 상태값 Update
            accountReqMgmt.setUseridReqstStsCd("R");
            accountReqMgmt.setGvbkResonCntnt(dto.getGvbkResonCntnt());
            accountReqMgmtRepository.save(accountReqMgmt);

            //계정테이블 반려 상태로 업데이트
            account.setUseridStsCd("R");
            account.setConnPsswd("");
            account.setUserNm(account.getUserNm().charAt(0) + "**");
            account.setUserContPhno("");
            account.setUserEmailaddr("");
            accountRepository.save(account);

            //계정상태 변경 로그 db
            accountService.accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());

            List<String> params = new ArrayList<>();
            params.add(account.getUserNm() + "(" + account.getUserid() + ")");
            //사용자 문자
            simpleSendDtoList.add(new SimpleSendDto(account.getUserContPhno(), MsgFormType.ACCOUNT_REJECT.getCode(), params));
        }
        messageSendService.sendSmsList(simpleSendDtoList);

        return true;
    }

    void basicUserRoleSetting(String userid) throws UnknownHostException {
        UserRole userRole = new UserRole();

        String userRoleId = AuthType.AUTH_DEFAULT.getCode();
        String ipAddr = RequestUtil.getClientIP();

        userRole.setUserid(userid);
        userRole.setUserRoleId(userRoleId);
        userRoleRepository.save(userRole);

        AccountDto account = accountRepository.findUserDeptInfo(userid);

        // 이력추가
        String chngResonCntnt = "신규 계정 생성으로 인한 권한 추가";
        userRoleHistRepository.save(UserRoleHist.builder()
                .userid(userid)
                .userRoleId(userRoleId)
                .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .crudClCd("C")
                .athrtyReqstSeq(null)
                .deptcd(account.getDeptcd())
                .deptNm(account.getDeptNm())
                .chngUserIpaddr(ipAddr)
                .chngResonCntnt(chngResonCntnt)
                .build());
    }
}