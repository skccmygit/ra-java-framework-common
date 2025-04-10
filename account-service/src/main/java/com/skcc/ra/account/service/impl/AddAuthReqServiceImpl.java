package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.adaptor.client.MessagingClient;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqDto;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqHisDto;
import com.skcc.ra.account.api.dto.requestDto.UserAuthProcReqDto;
import com.skcc.ra.account.api.dto.responseDto.UserAuthReqListDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.addAuth.UserAuthReq;
import com.skcc.ra.account.domain.auth.Agent;
import com.skcc.ra.account.domain.auth.UserMenu;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.UserScrenBttn;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import com.skcc.ra.account.domain.auth.pk.UserScrenBttnPK;
import com.skcc.ra.account.domain.hist.*;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.domain.hist.*;
import com.skcc.ra.account.domain.loginCert.UserBasedApi;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.service.AccountService;
import com.skcc.ra.account.service.UserAuthReqService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.CmmnCdDtlDto;
import com.skcc.ra.common.api.dto.domainDto.UserBasicDto;
import com.skcc.ra.common.api.dto.requestDto.SendReqDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AddAuthReqServiceImpl implements UserAuthReqService {

    @Autowired
    private UserAuthReqRepository userAuthReqRepository;

    @Autowired
    private UserAuthReqHisRepository userAuthReqHisRepository;

    @Autowired
    private UserAuthReqProcHisRepository userAuthReqProcHisRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleHistRepository userRoleHistRepository;

    @Autowired
    private UserMenuRepository userMenuRepository;

    @Autowired
    private UserScrenBttnRepository userScrenBttnRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommonClient commonClient;

    @Autowired
    private MessagingClient messagingClient;

    @Autowired
    private UserMenuHistRepository userMenuHistRepository;

    @Autowired
    private UserScrenBttnHistRepository userScrenBttnHistRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserBasedApiRepository userBasedApiRepository;

    @Autowired
    private RoleScrenBttnRepository roleScrenBttnRepository;

    public boolean[] checkAuth(){
        /* flag 기준
          flag[0] : IT담당
          flag[1] : 팀장
          flag[2] : 개인정보담당
          flag[3] : 승인자
         */

        boolean[] flag = {false,false,false,false};
        String userid = RequestUtil.getLoginUserid();
        List<String> userRoleList = RequestUtil.getLoginUserRoleList();

        if(userRoleList == null) throw new ServiceException("COM.I1016");

        if(userRoleList.contains(AuthType.IT_ADMIN.getCode()))  flag[0] = true;
        if(userRoleList.contains(AuthType.AUTH_CONFIRM.getCode()))  flag[1] = true;
        if(userRoleList.contains(AuthType.AUTH_APPROVE.getCode()))  flag[3] = true;

        // 개인정보 담당자
        List<CmmnCdDtlDto> cmmnCdDtlDto = commonClient.findByCmmnCd("SETTL_USER_ID");
        if (cmmnCdDtlDto != null && !cmmnCdDtlDto.isEmpty()) {
            try {
                String conUserid = cmmnCdDtlDto.stream()
                        .filter(i -> "INDIV".equals(i.getCmmnCdVal()))
                        .filter(i -> "Y".equals(i.getUseYn()))
                        .findFirst().get().getRefrnAttrVal1();
                if (conUserid.equals(userid)) flag[2] = true;
            } catch(Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return flag;
    }
    //사용자 추가권한 리스트/조건검색(관리자용 화면)
    @Override
    public Page<UserAuthReqListDto> searchUserAuthReqListByConditionForAdmin(String userNm,
                                                                             String athrtyReqstOpDtmFrom,
                                                                             String athrtyReqstOpDtmTo,
                                                                             String athrtyReqstStsCd,
                                                                             String indivInfoYn,
                                                                             Pageable pageable) {
        userNm = userNm == null ? "" : userNm;
        athrtyReqstStsCd = athrtyReqstStsCd == null ? "" : athrtyReqstStsCd;
        indivInfoYn = indivInfoYn == null ? "" : indivInfoYn;
        athrtyReqstOpDtmFrom = StringUtils.isBlank(athrtyReqstOpDtmFrom) ? "00000000000000" : athrtyReqstOpDtmFrom;
        athrtyReqstOpDtmTo = StringUtils.isBlank(athrtyReqstOpDtmTo) ? "99991231235959" : athrtyReqstOpDtmTo;

        Optional<Account> oAccount =  accountRepository.findByUseridAndUseridStsCdNot(RequestUtil.getLoginUserid(), "D");
        if(oAccount.isEmpty())     throw new ServiceException("COM.I1006");
        Account account = oAccount.get();

        String myDept = account.getDeptcd();

        boolean[] flag = this.checkAuth();
        List<CmmnCdDtlDto> innerUserClCd = commonClient.findByCmmnCd("INNER_USER_CL_CD");
        List<String> allList = innerUserClCd.stream().map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());
        List<String> ctcList = innerUserClCd.stream().filter(i -> "CTC".equals(i.getRefrnAttrVal1())).map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());
        List<String> othList = innerUserClCd.stream().filter(i -> !"CTC".equals(i.getRefrnAttrVal1())).map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());

        //승인자/IT담당자 또는 개인정보 담당자
        if(flag[0] || flag[2] || flag[3]){
            // 승인자는 모든 부서
            return userAuthReqRepository.searchUserAuthReqList(
                    userNm,
                    athrtyReqstOpDtmFrom,
                    athrtyReqstOpDtmTo,
                    athrtyReqstStsCd,
                    "",
                    "",
                    null,
                    allList,
                    indivInfoYn,
                    pageable);
        }
        else {
            // 검토자 권한이 있을 경우 본인부서 + 대무자로 지정된 건의 대상부서의 데이터가 조회된다.
            List<String> deptcdList = new ArrayList<>();
            List<Agent> agentList = agentRepository.findByAgentIdAndEndYn(account.getUserid(), "N");
            if(!agentList.isEmpty()){
                deptcdList = agentList.stream().map(Agent::getDeptcd).collect(Collectors.toList());
            }

           // 본인 부서
           return userAuthReqRepository.searchUserAuthReqList(
                    userNm,
                    athrtyReqstOpDtmFrom,
                    athrtyReqstOpDtmTo,
                    athrtyReqstStsCd,
                    "",
                    myDept,
                    deptcdList,
                    othList,
                    indivInfoYn,
                    pageable);

        }
    }

    //사용자 추가권한 리스트/조건검색(사용자용 화면)
    @Override
    public Page<UserAuthReqListDto> searchUserAuthReqListByConditionForUser(String userNm,
                                                                            String athrtyReqstOpDtmFrom,
                                                                            String athrtyReqstOpDtmTo,
                                                                            String athrtyReqstStsCd,
                                                                            Pageable pageable) {
        userNm = userNm == null ? "" : userNm;
        athrtyReqstStsCd = athrtyReqstStsCd == null ? "" : athrtyReqstStsCd;
        athrtyReqstOpDtmFrom = StringUtils.isBlank(athrtyReqstOpDtmFrom) ? "00000000000000" : athrtyReqstOpDtmFrom;
        athrtyReqstOpDtmTo = StringUtils.isBlank(athrtyReqstOpDtmTo) ? "99991231235959" : athrtyReqstOpDtmTo;

        List<CmmnCdDtlDto> innerUserClCd = commonClient.findByCmmnCd("INNER_USER_CL_CD");
        List<String> allList = innerUserClCd.stream().map(CmmnCdDtlDto::getCmmnCdVal).collect(Collectors.toList());

        return userAuthReqRepository.searchUserAuthReqList(userNm,
                                                          athrtyReqstOpDtmFrom,
                                                          athrtyReqstOpDtmTo,
                                                          athrtyReqstStsCd,
                                                            RequestUtil.getLoginUserid(),
                                                          "",
                                                          null,
                                                          allList,
                                                          "",
                                                          pageable);
    }

    @Override
    public List<UserAuthReqHisDto> checkUserAuthReq(Integer athrtyReqstSeq) {

        List<UserAuthReqHis> userAuthReqHiss = userAuthReqHisRepository.findByAthrtyReqstSeq(athrtyReqstSeq);
        return userAuthReqHiss.stream().map(UserAuthReqHis::toApi).collect(Collectors.toList());
    }

    //사용자 추가권한 신청
    @Override
    public UserAuthReqDto createUserAuthReq(UserAuthReqDto userAuthReqDto) throws UnknownHostException {

        String userid = RequestUtil.getLoginUserid();
        Optional<Account> reqUser = accountRepository.findByUserid(userid);
        if(reqUser.isEmpty()) {
            throw new ServiceException("COM.I1006");
        }

        Account account = reqUser.get();
        if("Y".equals(userAuthReqDto.getIndivInfoYn())) {
            if (StringUtils.isBlank(account.getUserIpaddr()) && StringUtils.isBlank(userAuthReqDto.getReqstUserIpaddr())) {
                throw new ServiceException("COM.I1049");
            }
        }

        //사용자권한신청기본
        UserAuthReq userAuthReq = userAuthReqDto.toEntity();
        userAuthReq.setUserid(userid);
        userAuthReq.setAthrtyReqstStsCd("Q");

        userAuthReq = userAuthReqRepository.save(userAuthReq);

        //사용자권한신청처리내역
        createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "Q", null);

        // 검토자 권한이 있을 경우 검토까지 처리
        List<String> userRoleList = RequestUtil.getLoginUserRoleList();
        if(userRoleList == null) throw new ServiceException("COM.I1016");

        if(userRoleList.contains(AuthType.AUTH_CONFIRM.getCode())){
            List<Integer> seq = new ArrayList<>();
            seq.add(userAuthReq.getAthrtyReqstSeq());
            this.confirmUserAuthReq(seq, "검토자 신청 건 자동검토처리");
        }else{
            // 검토자에게 SMS 발송
            UserBasicDto userBasicDto = commonClient.searchTeamLeader(account.getDeptcd(), null);
            if (userBasicDto != null) {
                List<SendReqDto> sendReqDtoList = new ArrayList<>();
                List<String> params = new ArrayList<>();
                params.add(account.getUserNm() + "(" + account.getUserid() + ")");
                messagingClient.sendSms(SendReqDto.builder()
                                            .smsNotitClCd("1")
                                            .dsprIdentNo("SYSTEM")
                                            .rcverPhno(userBasicDto.getMphno())
                                            .smsMsgFormId("C011")
                                            .params(params)
                                            .build());
            }
        }

        return userAuthReq.toApi();
    }

    @Override
    public List<UserAuthReqDto> procUserAuthReq(UserAuthProcReqDto userAuthProcReqDto) throws UnknownHostException {

        List<UserAuthReqDto> result = new ArrayList<>();
        result = switch (userAuthProcReqDto.getStatus()) {
            case "CANCEL" -> this.cancelUserAuthReq(userAuthProcReqDto.getAthrtyReqstSeq());
            case "CONFIRM" ->
                    this.confirmUserAuthReq(userAuthProcReqDto.getAthrtyReqstSeq(), userAuthProcReqDto.getGvbkResonCntnt());
            case "INDIV_CONFIRM" ->
                    this.confirmIndivUserAuthReq(userAuthProcReqDto.getAthrtyReqstSeq(), userAuthProcReqDto.getGvbkResonCntnt());
            case "REJECT" ->
                    this.rejectUserAuthReq(userAuthProcReqDto.getAthrtyReqstSeq(), userAuthProcReqDto.getGvbkResonCntnt());
            default -> result;
        };
        return result;
    }
    //사용자 추가권한 신청취소
    public List<UserAuthReqDto> cancelUserAuthReq(List<Integer> athrtyReqstSeq) throws UnknownHostException {
        List<UserAuthReqDto> userAuthReqDtos = new ArrayList<>();

        for(Integer seq : athrtyReqstSeq){
            Optional<UserAuthReq> optional = userAuthReqRepository.findByAthrtyReqstSeq(seq);

            if (optional.isPresent()) {
                //사용자권한신청기본 업데이트
                UserAuthReq userAuthReq = optional.get();
                if("Q".equals(userAuthReq.getAthrtyReqstStsCd())){
                    userAuthReq.setAthrtyReqstStsCd("U");
                    userAuthReqRepository.save(userAuthReq);
                    //사용자권한신청처리내역 신청취소 정보 넣기
                    createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "U", null);
                    userAuthReqDtos.add(userAuthReq.toApi());
                } else {
                    throw new ServiceException("COM.I1048");
                }
            }
        }
        return userAuthReqDtos;
    }

    //사용자 추가권한 검토
    public List<UserAuthReqDto> confirmUserAuthReq(List<Integer> athrtyReqstSeq, String gvbkResonCntnt) throws UnknownHostException {
        List<UserAuthReqDto> userAuthReqDtos = new ArrayList<>();

        boolean[] flag = this.checkAuth();
        int sendSmsChk = 0;
        for(Integer seq : athrtyReqstSeq){
            Optional<UserAuthReq> optional = userAuthReqRepository.findByAthrtyReqstSeq(seq);
            if (optional.isPresent()) {
                UserAuthReq userAuthReq = optional.get();
                // 신청 -> 검토
                if("Q".equals(userAuthReq.getAthrtyReqstStsCd())){
                    // 부서장 또는 부서장이면서 개인정보담당자 또는 승인자
                    if(flag[1] || flag[3]) {
                        // 일반 권한 신청 건 검토
                        userAuthReq.setAthrtyReqstStsCd("C");
                        userAuthReqRepository.save(userAuthReq);
                        createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "C", gvbkResonCntnt);
                        userAuthReqDtos.add(userAuthReq.toApi());

                        // 개인정보건이면 문자보내기
                        if ("Y".equals(userAuthReq.getIndivInfoYn()))   sendSmsChk++;
                    }else{
                        throw new ServiceException("COM.I1020"); // 검토자 아님
                    }
                }
                else{
                    throw new ServiceException("COM.I1028");
                }
            }
        }
        // 개인정보건이 포함되어 있었을 경우
        if(sendSmsChk > 0){
            try{
                List<CmmnCdDtlDto> cmmnCdDtlDto = commonClient.findByCmmnCd("SETTL_USER_ID");
                if (cmmnCdDtlDto != null && !cmmnCdDtlDto.isEmpty()) {
                    String conUserid = cmmnCdDtlDto.stream()
                            .filter(i -> "INDIV".equals(i.getCmmnCdVal()))
                            .filter(i -> "Y".equals(i.getUseYn()))
                            .findFirst().get().getRefrnAttrVal1();

                    List<UserBasicDto> userBasicDto = commonClient.searchUserBasic("", null, conUserid);
                    if (userBasicDto != null && !userBasicDto.isEmpty()) {
                        messagingClient.sendSms(SendReqDto.builder()
                                .smsNotitClCd("1")
                                .dsprIdentNo("SYSTEM")
                                .rcverPhno(userBasicDto.get(0).getMphno())
                                .smsMsgFormId("C010")
                                .build());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return userAuthReqDtos;
    }

    public List<UserAuthReqDto> confirmIndivUserAuthReq(List<Integer> athrtyReqstSeq, String gvbkResonCntnt) throws UnknownHostException {
        List<UserAuthReqDto> userAuthReqDtos = new ArrayList<>();

        boolean[] flag = this.checkAuth();
        for(Integer seq : athrtyReqstSeq){
            Optional<UserAuthReq> optional = userAuthReqRepository.findByAthrtyReqstSeq(seq);

            if (optional.isPresent()) {
                UserAuthReq userAuthReq = optional.get();
                // 신청 -> 검토
                if("C".equals(userAuthReq.getAthrtyReqstStsCd()) && "Y".equals(userAuthReq.getIndivInfoYn())){
                    // 개인정보담당자 또는 부서장이면서 개인정보담당자 또는 승인자
                    if(flag[2] || flag[3]){
                        // 개인정보여부가 Y 일경우 담당자 검토 처리, 사유 입력도 추가
                        userAuthReq.setAthrtyReqstStsCd("S");
                        userAuthReqRepository.save(userAuthReq);
                        createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "S", gvbkResonCntnt);
                        userAuthReqDtos.add(userAuthReq.toApi());
                    } else {
                        throw new ServiceException("COM.I1020"); // 검토자 아님
                    }
                } else {
                    throw new ServiceException("COM.I1028");
                }
            }
        }
        return userAuthReqDtos;
    }

    public List<UserAuthReqDto> rejectUserAuthReq(List<Integer> athrtyReqstSeq, String gvbkResonCntnt) throws UnknownHostException {
        List<UserAuthReqDto> userAuthReqDtos = new ArrayList<>();
        boolean[] flag = this.checkAuth();
        for(Integer seq : athrtyReqstSeq){
            Optional<UserAuthReq> optional = userAuthReqRepository.findByAthrtyReqstSeq(seq);

            if (optional.isPresent()) {
                //사용자권한신청기본 업데이트
                UserAuthReq userAuthReq = optional.get();
                if ("Q".equals(userAuthReq.getAthrtyReqstStsCd()) || "C".equals(userAuthReq.getAthrtyReqstStsCd()) || "S".equals(userAuthReq.getAthrtyReqstStsCd())) {
                    // 각자 조건에 맞는 것만 반려 가능
                    // 승인자 or // 부서장 권한 + 신청상태 or 개인정보담당자 권한 + 검토 상태
                    if(flag[3]
                            || (flag[1] && "Q".equals(userAuthReq.getAthrtyReqstStsCd()))
                            || (flag[2] && "C".equals(userAuthReq.getAthrtyReqstStsCd()))) {
                        userAuthReq.setAthrtyReqstStsCd("R");
                        userAuthReqRepository.save(userAuthReq);
                        createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "R", gvbkResonCntnt);
                        userAuthReqDtos.add(userAuthReq.toApi());

//                        try{
//                            List<UserBasicDto> userBasicDto = commonClient.searchUserBasic("", null, userAuthReq.getUserid());
//                            if (userBasicDto != null && !userBasicDto.isEmpty()) {
//                                String param = userBasicDto.get(0).getEmpKrnNm() + "(" + userBasicDto.get(0).getEmpno() + ")";
//                                messagingClient.sendSms(SendReqDto.builder()
//                                        .smsNotitClCd("1")
//                                        .dsprIdentNo("SYSTEM")
//                                        .rcverPhno(userBasicDto.get(0).getMphno())
//                                        .smsMsgFormId("C012")
//                                        .params(Arrays.asList(param))
//                                        .build());
//                            }
//                        } catch (Exception e) {
//                            log.error(e.getMessage(), e);
//                        }
                    } else {
                        throw new ServiceException("COM.I1047");
                    }
                } else {
                    throw new ServiceException("COM.I1047");
                }
            }
        }
        return userAuthReqDtos;
    }


    //사용자 추가권한 승인
    @Override
    public List<UserAuthReqDto> approveUserAuthReq(List<UserAuthReqDto> userAuthReqDtoList) throws UnknownHostException {
        List<UserAuthReqDto> userAuthReqDtos = new ArrayList<>();
        boolean[] flag = this.checkAuth();
        for(UserAuthReqDto userAuthReqDto : userAuthReqDtoList) {
            Optional<UserAuthReq> optional = userAuthReqRepository.findByAthrtyReqstSeq(userAuthReqDto.getAthrtyReqstSeq());

            if (optional.isPresent()) {
                //사용자권한신청기본 업데이트
                UserAuthReq userAuthReq = optional.get();
                if(("C".equals(userAuthReq.getAthrtyReqstStsCd()) && "N".equals(userAuthReq.getIndivInfoYn()))
                        || "S".equals(userAuthReq.getAthrtyReqstStsCd())){

                    if(flag[3]) {
                        userAuthReq.setAthrtyReqstStsCd("A");
                        userAuthReqRepository.save(userAuthReq);
                        userAuthReqDto.setUserid(userAuthReq.getUserid());
                        //사용자권한신청처리내역 승인 정보 넣기
                        createUserAuthReqProcHis(userAuthReq.getAthrtyReqstSeq(), "A", userAuthReqDto.getApprvResonCntnt());
                        if (userAuthReq.getUserAuthReqHiss() != null) {
                            //신청내역
                            createUserAuthReqHis(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserAuthReqHiss());
                            createUserAuthByReqHis(userAuthReqDto);
                        }
                        userAuthReqDtos.add(userAuthReq.toApi());
                        // 개인정보여부가 Y이거나 RPA 계정여부가 Y일때만
                        if("Y".equals(userAuthReq.getIndivInfoYn()) || "Y".equals(userAuthReq.getRpaUserYn())) {
                            if (userAuthReq.getReqstUserIpaddr() != null && !"".equals(userAuthReq.getReqstUserIpaddr())) {
                                Optional<Account> oReqUser = accountRepository.findByUserid(userAuthReq.getUserid());
                                if (oReqUser.isPresent()) {
                                    Account reqUser = oReqUser.get();
                                    reqUser.setUserIpaddr(userAuthReq.getReqstUserIpaddr());
                                    accountRepository.save(reqUser);
                                    // 변경 이력 저장
                                    accountService.accountStsChngLog(reqUser.getUserid(), "USER_IPADDR", userAuthReq.getReqstUserIpaddr());
                                }
                            }
                        }

//                        try{
//                            List<UserBasicDto> userBasicDto = commonClient.searchUserBasic("", null, userAuthReq.getUserid());
//                            if (userBasicDto != null && !userBasicDto.isEmpty()) {
//                                String param = userBasicDto.get(0).getEmpKrnNm() + "(" + userBasicDto.get(0).getEmpno() + ")";
//                                messagingClient.sendSms(SendReqDto.builder()
//                                        .smsNotitClCd("1")
//                                        .dsprIdentNo("SYSTEM")
//                                        .rcverPhno(userBasicDto.get(0).getMphno())
//                                        .smsMsgFormId("C013")
//                                        .params(Arrays.asList(param))
//                                        .build());
//                            }
//                        } catch (Exception e) {
//                            log.error(e.getMessage(), e);
//                        }
                    } else {
                        throw new ServiceException("COM.I1021");
                    }
                } else {
                    throw new ServiceException("COM.I1029");
                }
            }
        }
        return userAuthReqDtos;
    }

    @Override
    //신청 내역 각각 사용자권한 테이블에 넣기
    public void createUserAuthByReqHis(UserAuthReqDto userAuthReqDto) throws UnknownHostException {

        List<UserAuthReqHis> userAuthReqHiss = new ArrayList<>(userAuthReqDto.getUserAuthReqHiss());

        for(UserAuthReqHis item : userAuthReqHiss) {
            //역할
            if (item.getUserRoleId() != null) {
                if("Y".equals(item.getAthrtyAddnYn()))
                    createUserAuthforRole(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getUserRoleId());
                else deleteUserAuthforRole(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getUserRoleId());
            }
            //메뉴
            else if (item.getMenuId() != null) {
                if("Y".equals(item.getAthrtyAddnYn()))
                    createUserAuthforMenu(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getMenuId(), userAuthReqDto.getChngResonCntnt());
                else deleteUserAuthforMenu(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getMenuId(), userAuthReqDto.getChngResonCntnt());
            }
            //화면/버튼
            else if (item.getBttnId() != null) {
                if ("Y".equals(item.getAthrtyAddnYn()))
                    createUserAuthforScrenBttn(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getScrenId(), item.getBttnId(), userAuthReqDto.getChngResonCntnt());
                else
                    deleteUserAuthforScrenBttn(userAuthReqDto.getAthrtyReqstSeq(), userAuthReqDto.getUserid(), item.getScrenId(), item.getBttnId(), userAuthReqDto.getChngResonCntnt());
            }
        }
    }

    /** 필요시 API 생성 가능 **/
    //사용자권한 역할 추가
    public void createUserAuthforRole(Integer athrtyReqstSeq, String userid, String roleId) throws UnknownHostException {

        UserRolePK userRolePK = new UserRolePK(roleId, userid);
        Optional<UserRole> userRole = userRoleRepository.findById(userRolePK);
        String crudClCd = "C";
        String ipAddr = RequestUtil.getClientIP();
        if(userRole.isPresent()){
            // 삭제
            crudClCd = "U";
        }
        // 추가
        userRoleRepository.save(UserRole.builder()
                .userid(userid)
                .userRoleId(roleId)
                .athrtyReqstSeq(athrtyReqstSeq)
                .build());

        AccountDto account = accountRepository.findUserDeptInfo(userid);

        // 이력추가
        userRoleHistRepository.save(UserRoleHist.builder()
                .userid(userid)
                .userRoleId(roleId)
                .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .athrtyReqstSeq(athrtyReqstSeq)
                .crudClCd(crudClCd)
                .deptcd(account.getDeptcd())
                .deptNm(account.getDeptNm())
                .chngUserIpaddr(ipAddr)
                .build());
    }

    public void deleteUserAuthforRole(Integer athrtyReqstSeq, String userid, String roleId) throws UnknownHostException {
        UserRolePK userRolePK = new UserRolePK(roleId, userid);
        Optional<UserRole> userRole = userRoleRepository.findById(userRolePK);
        String crudClCd = "D";
        String ipAddr = RequestUtil.getClientIP();
        if(userRole.isEmpty()) {
            return;
        }
        // 추가
        userRoleRepository.deleteById(userRolePK);

        AccountDto account = accountRepository.findUserDeptInfo(userid);
        // 이력 추가
        userRoleHistRepository.save(UserRoleHist.builder()
                .userid(userid)
                .userRoleId(roleId)
                .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .athrtyReqstSeq(athrtyReqstSeq)
                .crudClCd(crudClCd)
                .deptcd(account.getDeptcd())
                .deptNm(account.getDeptNm())
                .chngUserIpaddr(ipAddr)
                .build());
    }


    //사용자권한 메뉴 추가
    public void createUserAuthforMenu(Integer athrtyReqstSeq, String userid, String menuId, String chngResonCntnt) throws UnknownHostException {
        Optional<UserMenu> optionalMenu =
                userMenuRepository.findByUseridAndMenuId(userid, menuId);

        if (optionalMenu.isEmpty()) {
            //사용자권한신청기본 업데이트
            UserMenu userMenu = new UserMenu();
            userMenu.setUserid(userid);
            userMenu.setMenuId(menuId);
            userMenu.setAthrtyReqstSeq(athrtyReqstSeq);

            userMenuRepository.save(userMenu);

            userMenuHistRepository.save(UserMenuHist.builder()
                            .userid(userid)
                            .menuId(menuId)
                            .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                            .athrtyReqstSeq(athrtyReqstSeq)
                            .crudClCd("C")
                            .chngUserIpaddr(RequestUtil.getClientIP())
                            .chngResonCntnt(chngResonCntnt == null ? "" : chngResonCntnt) //추가권한신청 승인으로 인한 건은 승인사유가 있어서 변경사유가 불필요
                            .build());

            // 매핑된 메인 화면-버튼 권한도 추가 - 23.01.10 편의기능 - 나중에 테스트
//            Scren scren = roleMenuRepository.findScrenByMenuId(menuId);
//            if (scren != null){
//                // 버튼 리스트 가져오기
//                if(!scren.getBttnList().isEmpty()) {
//                    for (Bttn bttn : scren.getBttnList()) {
//                        // 가져온 만큼 looping
//                        createUserAuthforScrenBttn(athrtyReqstSeq, userid, scren.getScrenId(), bttn.getBttnId());
//                    }
//                }
//            }

        }
    }

    //사용자권한 메뉴 삭제
    public void deleteUserAuthforMenu(Integer athrtyReqstSeq, String userid, String menuId, String chngResonCntnt) throws UnknownHostException {
        UserMenuPK pk = new UserMenuPK(userid, menuId);
        if(userMenuRepository.existsById(pk)){
            userMenuRepository.deleteById(pk);

            userMenuHistRepository.save(UserMenuHist.builder()
                    .userid(userid)
                    .menuId(menuId)
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .athrtyReqstSeq(athrtyReqstSeq)
                    .crudClCd("D")
                    .chngUserIpaddr(RequestUtil.getClientIP())
                    .chngResonCntnt(chngResonCntnt == null ? "" : chngResonCntnt) //추가권한신청 승인으로 인한 건은 승인사유가 있어서 변경사유가 불필요
                    .build());

            // 매핑된 메인 화면-버튼 권한도 삭제 - 23.01.10 편의기능 - 내역 남지 않는 문제..  - 나중에 테스트
//            Scren scren = roleMenuRepository.findScrenByMenuId(menuId);
//            if (scren != null){
//                // 버튼 리스트 가져오기
//                if(!scren.getBttnList().isEmpty()) {
//                    for (Bttn bttn : scren.getBttnList()) {
//                        // 가져온 만큼 looping
//                        deleteUserAuthforScrenBttn(userid, scren.getScrenId(), bttn.getBttnId());
//                    }
//                }
//            }
        }
    }

    //사용자권한 화면/버튼 추가
    public void createUserAuthforScrenBttn(Integer athrtyReqstSeq, String userid, String screnId, String bttnId, String chngResonCntnt) throws UnknownHostException {

        Optional<UserScrenBttn> optionalScrenBttn =
                userScrenBttnRepository.findByUseridAndScrenIdAndBttnId(userid, screnId, bttnId);

        if (optionalScrenBttn.isEmpty()) {
            //사용자권한신청기본 업데이트
            UserScrenBttn userScrenBttn = new UserScrenBttn();

            userScrenBttn.setUserid(userid);
            userScrenBttn.setScrenId(screnId);
            userScrenBttn.setBttnId(bttnId);
            userScrenBttn.setAthrtyReqstSeq(athrtyReqstSeq);

            userScrenBttnRepository.save(userScrenBttn);

            userScrenBttnHistRepository.save(UserScrenBttnHist.builder()
                    .userid(userid)
                    .screnId(screnId)
                    .bttnId(bttnId)
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .athrtyReqstSeq(athrtyReqstSeq)
                    .crudClCd("C")
                    .chngUserIpaddr(RequestUtil.getClientIP())
                    .chngResonCntnt(chngResonCntnt == null ? "" : chngResonCntnt) //추가권한신청 승인으로 인한 건은 승인사유가 있어서 변경사유가 불필요
                    .build());
            // redis 에도 적용
            // 1. redis에 있는지 확인
            Optional<UserBasedApi> oUserBasedApi = userBasedApiRepository.findByUseridAndScrenIdAndBttnId(userid, screnId, bttnId);
            // 2. redis에 존재하면 return
            if(oUserBasedApi.isPresent())   return;

            // 3. 버튼 정보 찾기
            BttnDto btn = roleScrenBttnRepository.findBttnInfo(screnId, bttnId);

            // 4. 버튼이 없으면 return
            if (btn == null)    return;
            if (btn.getApiId() == null || "".equals(btn.getApiId()))   return;

            // 5. onm 에서 api 정보 찾기
            Map<String, String> apiInfo = commonClient.findApiInfo(btn.getApiId());
            if (apiInfo == null)    return;

            // 6. redis에 저장
            UserBasedApi userBasedApi = new UserBasedApi();
            userBasedApi.setUserid(userid);
            userBasedApi.setScrenId(screnId);
            userBasedApi.setBttnId(bttnId);
            userBasedApi.setApiId(Integer.parseInt(apiInfo.get("apiId")));
            userBasedApi.setHttMethodVal(apiInfo.get("httMethodVal"));
            userBasedApi.setApiLocUrladdr(apiInfo.get("apiLocUrladdr"));
            userBasedApiRepository.save(userBasedApi);
        }
    }

    //사용자권한 화면/버튼 삭제
    public void deleteUserAuthforScrenBttn(Integer athrtyReqstSeq, String userid, String screnId, String bttnId, String chngResonCntnt) throws UnknownHostException {
        UserScrenBttnPK pk = new UserScrenBttnPK(userid, screnId, bttnId);
        if(userScrenBttnRepository.existsById(pk)){
            userScrenBttnRepository.deleteById(pk);

            userScrenBttnHistRepository.save(UserScrenBttnHist.builder()
                    .userid(userid)
                    .screnId(screnId)
                    .bttnId(bttnId)
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .athrtyReqstSeq(athrtyReqstSeq)
                    .crudClCd("D")
                    .chngUserIpaddr(RequestUtil.getClientIP())
                    .chngResonCntnt(chngResonCntnt == null ? "" : chngResonCntnt) //추가권한신청 승인으로 인한 건은 승인사유가 있어서 변경사유가 불필요
                    .build());

            // redis 에도 적용
            // 1. redis에 있는지 확인
            Optional<UserBasedApi> oUserBasedApi = userBasedApiRepository.findByUseridAndScrenIdAndBttnId(userid, screnId, bttnId);
            // 2. redis에 존재하면 삭제
            if(oUserBasedApi.isPresent()){
                userBasedApiRepository.deleteById(oUserBasedApi.get().getId());
            }
        }
    }

    //사용자권한신청처리내역 저장
    public void createUserAuthReqProcHis(Integer athrtyReqstSeq, String athrtyReqstStsCd, String gvbkReson) throws UnknownHostException {

        UserAuthReqProcHis userAuthReqProcHis = new UserAuthReqProcHis();
        userAuthReqProcHis.setAthrtyReqstSeq(athrtyReqstSeq);
        userAuthReqProcHis.setAthrtyReqstOpDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        userAuthReqProcHis.setAthrtyReqstStsCd(athrtyReqstStsCd);
        userAuthReqProcHis.setGvbkResonCntnt(gvbkReson);
        userAuthReqProcHis.setSettlUserid(RequestUtil.getLoginUserid());

        if("A".equals(athrtyReqstStsCd)) {
            String ip_new = RequestUtil.getClientIP();
            // 임시로 넣어놓음
            if (!("".equals(ip_new) || ip_new == null)) {
                log.debug("ip_new :: {}", ip_new);
                if (ip_new.length() > 16) {
                    userAuthReqProcHis.setSettlUserIpaddr("127.0.0.1");
                }else{
                    userAuthReqProcHis.setSettlUserIpaddr(ip_new);
                }
            }
        }

        userAuthReqProcHisRepository.save(userAuthReqProcHis);
    }

    //사용자권한신청내역 저장
    public void createUserAuthReqHis(Integer athrtyReqstSeq, Set<UserAuthReqHis> userAuthReqHiss) {
        int athrtyReqstDtlSeq = 1;

        for(UserAuthReqHis userAuthReqHis : userAuthReqHiss) {
            userAuthReqHis.setAthrtyReqstSeq(athrtyReqstSeq);
            userAuthReqHis.setAthrtyReqstDtlSeq(athrtyReqstDtlSeq++);
            userAuthReqHisRepository.save(userAuthReqHis);
        }
    }
}