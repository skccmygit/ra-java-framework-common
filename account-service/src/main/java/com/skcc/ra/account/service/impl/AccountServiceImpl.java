package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.repository.*;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.adaptor.client.MessagingClient;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.responseDto.AccountUpdateDto;
import com.skcc.ra.account.api.dto.responseDto.PasswordDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.api.type.MsgFormType;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.auth.Role;
import com.skcc.ra.account.domain.auth.UserMenu;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.UserScrenBttn;
import com.skcc.ra.account.domain.hist.AccountStsChng;
import com.skcc.ra.account.domain.hist.UserMenuHist;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.account.domain.hist.UserScrenBttnHist;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.service.AccountService;
import com.skcc.ra.common.api.dto.requestDto.SendReqDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountStsChngRepository accountStsChngRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    @Autowired
    UserRoleHistRepository userRoleHistRepository;

    @Autowired
    UserMenuRepository userMenuRepository;

    @Autowired
    UserScrenBttnRepository userScrenBttnRepository;

    @Autowired
    ShortcutMenuRepository shortcutMenuRepository;

    @Autowired
    BookmarkMenuRepository bookmarkMenuRepository;

    @Autowired
    MessagingClient messagingClient;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommonClient commonClient;

    @Autowired
    MyViewDtlRepository myViewDtlRepository;

    @Autowired
    MyViewRepository myViewRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserMenuHistRepository userMenuHistRepository;

    @Autowired
    private UserScrenBttnHistRepository userScrenBttnHistRepository;

    public AccountDto searchAccountByUserid(String userid) {

        Optional<Account> oAccount = accountRepository.findByUseridAndUseridStsCdNot(userid, "D");
        if(oAccount.isPresent()){
            Account account = oAccount.get();

            List<UserRole> userRoleList =  userRoleRepository.findByUserid(account.getUserid());
            List<String> roles = userRoleList.stream().map(item -> item.getUserRoleId()).collect(Collectors.toList());

            return roles.isEmpty() ? new AccountDto(account) : new AccountDto(account, roles);

        }else{
            return new AccountDto();
        }
    }

    @Override
    public HashMap<String, String> searchInfoByUserid(String userid) {
        Optional<Account> oAccount =  accountRepository.findByUseridAndUseridStsCdNot(userid, "D");
        if(!oAccount.isPresent())     throw new ServiceException("COM.I1006");

        Account account = oAccount.get();

        if(!"1".equals(account.getInnerUserClCd())) throw new ServiceException("COM.I1038");

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userNm", account.getUserNm());
        userInfo.put("userContPhno", account.getUserContPhno());
        userInfo.put("userEmailaddr", account.getUserEmailaddr());

        return userInfo;
    }

    @Override
    public Page<AccountDto> searchAccount(List<String> deptcdList, String useridOrNm, String useridStsCd, String deptNm, String fstRegDtmdFrom, String fstRegDtmdTo, Pageable pageable) {

        String deptcdListYn = "Y";
        if (deptcdList == null || deptcdList.isEmpty()) deptcdListYn = "N";

        if (useridOrNm == null) useridOrNm = "";
        if (useridStsCd == null) useridStsCd = "";
        if (deptNm == null) deptNm = "";

        LocalDateTime fstRegDtmFromL, fstRegDtmToL;
        if (fstRegDtmdFrom == null || "".equals(fstRegDtmdFrom)) {
            fstRegDtmFromL = LocalDateTime.MIN;
        }else{
            fstRegDtmFromL = LocalDate.parse(fstRegDtmdFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        }
        if (fstRegDtmdTo == null || "".equals(fstRegDtmdTo)) {
            fstRegDtmToL = LocalDateTime.MAX;
        }else{
            fstRegDtmToL = LocalDate.parse(fstRegDtmdTo, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().plusDays(1);
        }

        Page<AccountDto> accounts = accountRepository.searchAccount(deptcdList, deptcdListYn, useridOrNm, useridStsCd, deptNm, fstRegDtmFromL, fstRegDtmToL, pageable);

        return accounts;
    }

    @Override
    public List<AccountDto> searchAccountByCondition(List<String> deptcdList, String userNm) {
        String deptcdListYn = "Y";
        if (deptcdList == null || deptcdList.isEmpty()) deptcdListYn = "N";
        if(userNm == null)  userNm = "";

        if("N".equals(deptcdListYn) && "".equals(userNm)){
            throw new ServiceException("COM.I1044");
        }
        List<AccountDto> accountList = accountRepository.searchAccountByCondition(deptcdList, deptcdListYn, userNm);
        if(accountList.isEmpty())   throw new ServiceException("COM.I1006");
        return accountList;
    }

    @Override
    public Boolean updateAccount(List<AccountUpdateDto> accountUpdateDtoList) {
        try {
            for (AccountUpdateDto item : accountUpdateDtoList) {
                changeAccountInfo(item, RequestUtil.getClientIP());
            }
            return true;
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("COM.E0001");
        }
    }

    public void changeAccountInfo(AccountUpdateDto item, String ipAddr){
//        boolean isChg = false;
        // 1. Account update
        try {
            Optional<Account> oAccount = accountRepository.findByUserid(item.getUserid());
            if (oAccount.isPresent()) {
                Account account = oAccount.get();

                if ("D".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1007");

                if (item.getUserNm() == null) item.setUserNm("");
                if (item.getUserContPhno() == null) item.setUserContPhno("");
                if (item.getUserIpaddr() == null) item.setUserIpaddr("");

                // 이름 변경(상담사만 가능)
                if (!"".equals(item.getUserNm()) && !item.getUserNm().equals(account.getUserNm())) {
                    accountStsChngLog(item.getUserid(), "USER_NM", account.getUserNm());
                    account.setUserNm(item.getUserNm());
                }
                // 전화번호 변경(상담사만 가능)
                if (!"".equals(item.getUserContPhno()) && !item.getUserContPhno().equals(account.getUserContPhno())) {
                    accountStsChngLog(item.getUserid(), "USER_CONT_PHNO", account.getUserContPhno());
                    account.setUserContPhno(item.getUserContPhno());
                }

                // IP 주소 변경
                if (!"".equals(item.getUserIpaddr()) && !item.getUserIpaddr().equals(account.getUserIpaddr())) {
                    accountStsChngLog(item.getUserid(), "USER_IPADDR", account.getUserIpaddr());
                    account.setUserIpaddr(item.getUserIpaddr());
                }

                accountRepository.save(account);

            } else {
                throw new ServiceException("COM.I1006");
            }
        } catch (Exception e) {
            throw new ServiceException("COM.E0001");
        }
    }
    public boolean[] authCheck(){
        /* flag 기준
          flag[0] : IT담당
          flag[1] : BP권한자
          flag[2] : 상담사권한자
         */
        boolean[] flag =  {false,false,false};
        List<String> userRoleList = RequestUtil.getLoginUserRoleList();

        // 잠금기능 권한이 있는 역할리스트
        List<Role> roleList = roleRepository.findLockAthrtyRoleList();

        for(Role role : roleList){
            if(userRoleList.contains(role.getUserRoleId())){
                if("A".equals(role.getLockAthrtyClCd())){
                    flag[0] = true;
                    break;
                }
                else if("S".equals(role.getLockAthrtyClCd()))  flag[1] = true;
                else if("C".equals(role.getLockAthrtyClCd()))  flag[2] = true;
            }
        }
        return flag;
    }
    @Override
    public Boolean updateUseridStsForLock(List<String> userids) {

        boolean[] flag = authCheck();
        log.info("userid : {} , flag : {} ", RequestUtil.getLoginUserid(), flag);

        List<String> bpList = commonClient.findByCmmnCd("BP_VCTN_CD")
                .stream()
                .map(i -> i.getCmmnCdVal())
                .collect(Collectors.toList());

        for(String userid : userids) {
            Account account = accountRepository.getById(userid);

            // A권한은 프리패스
            if(!flag[0]){
                // BP 대상 일 경우 - S 권한 필요
                if(bpList.contains(account.getVctnCd()) && !flag[1])    throw new ServiceException("COM.I1050");
                // 상담사일 경우 - C 권한 필요
                if("1".equals(account.getInnerUserClCd()) && !flag[2])    throw new ServiceException("COM.I1050");
            }

            if("W".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1004");
            else if("R".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1023");
            else if("D".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1007");
            else if("O".equals(account.getUseridStsCd()) || "T".equals(account.getUseridStsCd())) {
                account.setUseridStsCd("L");
            }
            else if("L".equals(account.getUseridStsCd())){ // L 일 경우
                if(account.getPsswdErrFrqy() >= 5 && "1".equals(account.getInnerUserClCd())){
                    account.setUseridStsCd("T");
                }else{
                    account.setUseridStsCd("O");
                    account.setPsswdErrFrqy(0);
                }
            }else{
                throw new ServiceException("COM.E0001");
            }

            accountRepository.save(account);

            //계정상태 변경 로그 db
            accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());
        }
        return true;
    }

    @Override
    public Boolean deleteAccount(List<String> userids, String ipAddr, boolean isCtc) {

        for(int i= 0; i<userids.size(); i++) {
            Account account = accountRepository.getById(userids.get(i));

            if(account == null) throw new ServiceException("COM.I1046");
            if("D".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1007");

            //계정 삭제처리(개인정보 삭제 & 상태값 변경)
            String maskedName = account.getUserNm().substring(0,1) + "**";
            account.setUseridStsCd("D");
            account.setConnPsswd("");
            account.setUserNm(maskedName);
            account.setUserContPhno("");
            account.setUserEmailaddr("");
            accountRepository.save(account);

            //계정상태 변경 로그 db
            accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());

            String chngResonCntnt = "계정삭제로 인한 삭제";
            if(isCtc)   chngResonCntnt = "퇴사로 인한 삭제";
            //기존 역할 전부 삭제
            deleteUserRole(account, ipAddr, chngResonCntnt);
            //추가메뉴/화면버튼 삭제
            deleteUserAddAuth(userids.get(i), ipAddr, chngResonCntnt);
            //바로가기 삭제
            shortcutMenuRepository.deleteByUserid(userids.get(i));
            //즐겨찾기 삭제
            bookmarkMenuRepository.deleteByUserid(userids.get(i));
            //마이뷰 상세 삭제
            myViewDtlRepository.deleteMyViewDtl(userids.get(i));
            //마이뷰 삭제
            myViewRepository.deleteByUserid(userids.get(i));

            // 상담사이고 ctc에서 호출한 경우가 아닐 경우 - 사용자관리 상태변경 API호출
            if("1".equals(account.getInnerUserClCd()) && !isCtc ){
                String lastChngrId = RequestUtil.getLoginUserid();
                List<HashMap<String, String>> delUserList = new ArrayList<>();

                for(String item : userids){
                    HashMap<String, String> delUser = new HashMap<>();
                    delUser.put("lastChngrId",lastChngrId);
                    delUser.put("userid",item);

                    if(!delUser.isEmpty()){
                        delUserList.add(delUser);
                    }
                }
            }

        }
        return true;
    }

    @Override
    public String findConnPsswd(String userid, String method) {

        Optional<Account> oAccount = accountRepository.findByUserid(userid);
        Account account;
        if(oAccount.isPresent()){
            account = oAccount.get();
            if(!"1".equals(account.getInnerUserClCd())) throw new ServiceException("COM.I1038");
            if("D".equals(account.getUseridStsCd()) ) throw new ServiceException("COM.I1007");
            if("W".equals(account.getUseridStsCd()) || "R".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1022");

        }else{
            throw new ServiceException("COM.I1006");
        }

        //임시비밀번호 생성
        String tempPsswd = generateTempPassword();
        account.setConnPsswd(passwordEncoder.encode(tempPsswd));
        account.setUseridStsCd("T");
        account.setPsswdErrFrqy(0);
        account = accountRepository.save(account);

        if("phno".equals(method)){
            //문자보내기 모듈
            List<SendReqDto> sendReqDtoList = new ArrayList<>();

            List<String> params = new ArrayList<>();
            params.add(account.getUserNm() + "(" + account.getUserid() + ")");
            params.add(tempPsswd);
            //사용자 문자
            sendReqDtoList.add(SendReqDto.builder()
                    .smsNotitClCd("1")
                    .dsprIdentNo(RequestUtil.getLoginUserid())
                    .rcverPhno(account.getUserContPhno())
                    .smsMsgFormId(MsgFormType.PASSWORD_INIT.getCode())
                    .params(params)
                    .build());

            messagingClient.multiSendSms(sendReqDtoList);

        }else if("email".equals(method)){
            // 메일 보내기 모듈
        }else{}

        //계정상태 변경 로그
        accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());
        //비밀번호 변경 로그
        accountStsChngLog(account.getUserid(), "CONN_PSSWD", account.getConnPsswd());

        return tempPsswd;
    }

    public boolean loopChar(String userPwd) {
        int tmp = 0;
        int loopCnt = 0;
        for (int i = 0; i < userPwd.length(); i++) {
            if (userPwd.charAt(i) == tmp) {
                loopCnt++;
            } else {
                loopCnt = 0;
            }
            if (loopCnt == 2) {
                return true;
            }
            tmp = userPwd.charAt(i);
        }
        return false;
    }

    public boolean continuosChar(String userPwd) {
        int tmp = 0;
        int reverseLoopCnt = 0;
        int loopCnt = 0;
        for (int i = 0; i < userPwd.length(); i++) {
            int gap = userPwd.charAt(i) - tmp;
            if (gap == 1) {
                reverseLoopCnt = 0;
                loopCnt++;
            } else if (gap == -1) {
                reverseLoopCnt++;
                loopCnt = 0;
            } else {
                reverseLoopCnt = 0;
                loopCnt = 0;
            }
            if (loopCnt == 2 || reverseLoopCnt == 2) {
                return true;
            }
            tmp = userPwd.charAt(i);
        }
        return false;
    }

    public int isValid(PasswordDto passwordDto){
        int result = 0;

        Pattern passPattern1 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(passwordDto.getNewConnPsswd());

        if(!passMatcher1.find()){
            result = 1;
            return result;
        }
        if(loopChar(passwordDto.getNewConnPsswd())){
            result = 2;
            return result;
        }
        if(continuosChar(passwordDto.getNewConnPsswd())){
            result = 3;
            return result;
        }

        return result;
    }

    @Override
    public Boolean connPsswdChange(PasswordDto passwordDto) {

        String userid = passwordDto.getUserid();
//        String userid = RequestUtil.getLoginUserid();
        Optional<Account> oAccount = accountRepository.findById(userid);

        if(!oAccount.isPresent())   throw new ServiceException("COM.I1022");

        Account account = oAccount.get();

        // 상담사만 비밀번호 변경이 가능
        if(!"1".equals(account.getInnerUserClCd())) throw new ServiceException("COM.I1038");
        //잠김 상태일 때 초기화 안됨. 관리자가 해제해 주어야 함.
        if("L".equals(account.getUseridStsCd()))    throw new ServiceException("COM.I1012");
        // 현재 비밀번호와 화면에서 받은 이전 비밀번호가 같지 않을 경우
        if(!passwordDto.getOldConnPsswd().equals(account.getConnPsswd()))   throw new ServiceException("COM.I1010");
        // 신규 비밀번호와 신규비밀번호 확인 값이 다를 경우
        if(!passwordDto.getNewConnPsswd().equals(passwordDto.getNewConnPsswdCheck()))   throw new ServiceException("COM.I1011");

        // 비밀번호 유효성 체크
        int isvalid = isValid(passwordDto);
        log.debug("isvalid : " + isvalid);
        if(isvalid == 1){
            throw new ServiceException("COM.I1041");
        }else if(isvalid == 2){
            throw new ServiceException("COM.I1042");
        }else if(isvalid == 3) {
            throw new ServiceException("COM.I1043");
        }

        // 비밀번호 암호화
        String encPasswd = passwordEncoder.encode(passwordDto.getNewConnPsswd());

        // 사용한 적 있는 비밀번호인지 확인
        if(accountStsChngRepository.findAllAccountPassword(userid,encPasswd).size() > 0){
            throw new ServiceException("COM.I1015");
        }

        // 조건을 모두 통과했을 경우
        account.setConnPsswd(encPasswd);
        account.setPsswdExpirDt(LocalDateTime.now().plusDays(90).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        account.setUseridStsCd("O");
        account.setPsswdErrFrqy(0);
        accountRepository.save(account);
        //계정상태 변경 로그 db
        accountStsChngLog(account.getUserid(), "USERID_STS_CD", account.getUseridStsCd());
        accountStsChngLog(account.getUserid(), "CONN_PSSWD", encPasswd);

        return true;
    }

//    @Override
//    public UserDetails loadUserByUsername(String userNm) throws UsernameNotFoundException {
//        Account account = accountRepository.findByUserid(userNm).orElseThrow(() -> new UsernameNotFoundException("COM.I1006"));
//        return AccountDetailsDto.build(account);
//    }

    @Override
    public String generateTempPassword(){
        int pwdLength = 10;
        final char[] passwordTable_Eng_L = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        final char[] passwordTable_Eng_S = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        final char[] passwordTable_Sp = {'!', '@', '#', '$', '%', '^', '*', '(', ')'};
        final char[] passwordTable_Num = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        Random random = new Random(System.currentTimeMillis());
        StringBuffer tempPassword = new StringBuffer();
        for(int i = 0; i < 3; i++) {
            tempPassword.append(passwordTable_Eng_L[random.nextInt(passwordTable_Eng_L.length)]);
            tempPassword.append(passwordTable_Eng_S[random.nextInt(passwordTable_Eng_S.length)]);
            tempPassword.append(passwordTable_Sp[random.nextInt(passwordTable_Sp.length)]);
            tempPassword.append(passwordTable_Num[random.nextInt(passwordTable_Num.length)]);
        }
        return tempPassword.substring(0,pwdLength);
    }

    public void deleteUserRole(Account account, String ipAddr, String chngResonCntnt){

        List<UserRole> userRoleList = userRoleRepository.findByUserid(account.getUserid());
        List<UserRoleHist> userRoleHistList = new ArrayList<>();
        AccountDto accountDto = accountRepository.findUserDeptInfo(account.getUserid());
        for (UserRole userRole : userRoleList) {
            userRoleHistList.add(UserRoleHist.builder()
                    .userid(account.getUserid())
                    .userRoleId(userRole.getUserRoleId())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("D")
                    .athrtyReqstSeq(null)
                    .deptcd(accountDto.getDeptcd())
                    .deptNm(accountDto.getDeptNm())
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt)
                    .build()
            );
        }
        // 삭제 이력추가
        userRoleHistRepository.saveAll(userRoleHistList);
        // 역할 삭제
        userRoleRepository.deleteByUserid(account.getUserid());
    }

    public void deleteUserAddAuth(String userid, String ipAddr, String chngResonCntnt){

        //추가 메뉴 이력생성
        List<UserMenu> userMenuList = userMenuRepository.findByUserid(userid);
        for(UserMenu userMenu : userMenuList){
            userMenuHistRepository.save(UserMenuHist.builder()
                    .userid(userid)
                    .menuId(userMenu.getMenuId())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("D")
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt)
                    .build());
        }
        //추가 메뉴 삭제
        userMenuRepository.deleteByUserid(userid);

        //추가 메뉴 이력생성
        List<UserScrenBttn> userScrenBttnList = userScrenBttnRepository.findByUserid(userid);
        for(UserScrenBttn userScrenBttn : userScrenBttnList){
            userScrenBttnHistRepository.save(UserScrenBttnHist.builder()
                    .userid(userid)
                    .screnId(userScrenBttn.getScrenId())
                    .bttnId(userScrenBttn.getBttnId())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("D")
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt)
                    .build());
        }
        //추가 화면/버튼 삭제
        userScrenBttnRepository.deleteByUserid(userid);
     }

    public void changeAuthInfo(Account account, String ipAddr){

        String userid = account.getUserid();
        String chngResonCntnt = "고객센터팀/직책 변경으로 인한 삭제";

        //기존 역할 전부 삭제
        deleteUserRole(account, ipAddr, chngResonCntnt);
        //추가메뉴/화면버튼 삭제
        deleteUserAddAuth(userid, ipAddr, chngResonCntnt);

        //신규역할 등록
        List<String> userRoleIdList = new ArrayList<>();

        //부서와 직급 직책에 따른 역할
        if(account.getDeptcd() != null && !"".equals(account.getDeptcd()) && account.getReofoCd() != null && !"".equals(account.getReofoCd())){

            List<String> userDeptRoleIdList = userRoleDeptMappingRepository.findByRoleDeptTeamCdAndRoleMappReofoCd(account.getDeptcd(), account.getReofoCd());
            List<String> vdtnCdList = Arrays.asList("410", "430", "510", "511","512");
            if(userDeptRoleIdList != null && !userDeptRoleIdList.isEmpty()) {
                userRoleIdList.addAll(userDeptRoleIdList);
                String deptNm = commonClient.searchDeptcd(account.getDeptcd()).getDeptNm();

                if (deptNm.endsWith("지사") || deptNm.endsWith("영업소")) {
                    if ("250".equals(account.getReofoCd())) {
                        if ("340".equals(account.getVctnCd())) {
                            userRoleIdList.add(AuthType.TSE_SMALL_TEAM_LEADER.getCode());
                            userRoleIdList.add(AuthType.KNOWLEDGE_MANAGEMENT_BP_TSE_SEARCH.getCode());
                        }
                    } else if ("300".equals(account.getReofoCd())) {
                        if ("340".equals(account.getVctnCd())) {
                            userRoleIdList.add(AuthType.TSE_TEAM_MEMBER.getCode());
                            userRoleIdList.add(AuthType.KNOWLEDGE_MANAGEMENT_BP_TSE_SEARCH.getCode());
                        } else if(vdtnCdList.contains(account.getVctnCd())) {
                            userRoleIdList.add(AuthType.BP_COMMON.getCode());
                        }
                    }
                }
            }
        }

        if(account.getReofoCd() != null && !"".equals(account.getReofoCd())) {
            if (Integer.parseInt(account.getReofoCd()) >= 112 && Integer.parseInt(account.getReofoCd()) <= 210) {
                userRoleIdList.add(AuthType.AUTH_CONFIRM.getCode());
            }
        }
        log.info("account : {}", account);

        // 아무것도 매핑되는 역할이 없을 경우 기본 역할 부여
        if(userRoleIdList == null || userRoleIdList.isEmpty()){
            userRoleIdList.add(AuthType.AUTH_DEFAULT.getCode());
        }

        List<UserRole> userRoleList = new ArrayList<>();
        List<UserRoleHist> UserRoleHistList = new ArrayList<>();

        AccountDto accountDto = accountRepository.findUserDeptInfo(userid);

        String chngResonCntnt1 = "고객센터팀/직책 변경으로 인한 생성";
        for(String userRoleId : userRoleIdList) {

            userRoleList.add(UserRole.builder()
                    .userid(userid)
                    .userRoleId(userRoleId)
                    .build());

            // 계정 생성시 권한 변경 이력 저장
            UserRoleHistList.add(UserRoleHist.builder()
                    .userid(userid)
                    .userRoleId(userRoleId)
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("C")
                    .athrtyReqstSeq(null)
                    .deptcd(accountDto.getDeptcd())
                    .deptNm(accountDto.getDeptNm())
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt1)
                    .build());

        }
        userRoleRepository.saveAll(userRoleList);
        userRoleHistRepository.saveAll(UserRoleHistList);

        //바로가기 삭제
        shortcutMenuRepository.deleteByRoleDeptTeamCdAndRoleMappReofoCd(userid);
        //즐겨찾기 삭제
        bookmarkMenuRepository.deleteByRoleDeptTeamCdAndRoleMappReofoCd(userid);
    }

    @Override
    public void accountStsChngLog(String userid, String chngColEngshNm, String chngColVal) {
        AccountStsChng accountStsChng = new AccountStsChng();

        accountStsChng.setUserid(userid);
        accountStsChng.setChngColEngshNm(chngColEngshNm);
        accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        accountStsChng.setChngColVal(chngColVal);

        accountStsChngRepository.save(accountStsChng);
    }

    @Override
    public void connPsswdReset(String userid) {

        Optional<Account> oAccount = accountRepository.findByUseridAndUseridStsCdNot(userid, "D");
        if(!oAccount.isPresent())     throw new ServiceException("COM.I1006");

        Account account = oAccount.get();

        if(!"1".equals(account.getInnerUserClCd())) throw new ServiceException("COM.I1039");
        if("O".equals(account.getUseridStsCd()) || "T".equals(account.getUseridStsCd())) {
            String cryptoConnPsswd = passwordEncoder.encode(userid);
            String expiredDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            accountRepository.updateConnPwd(userid, cryptoConnPsswd, "O", 0, expiredDt, "SYSTEM");
        }else{
            throw new ServiceException("COM.I1039");
        }
    }

}