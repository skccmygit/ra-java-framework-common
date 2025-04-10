package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.api.dto.domainDto.UserActvyLogDto;
import com.skcc.ra.account.api.dto.requestDto.LoginReqDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.config.ArgosPasswordEncoder;
import com.skcc.ra.account.config.JwtResponse;
import com.skcc.ra.account.config.JwtUtils;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.loginCert.RefreshToken;
import com.skcc.ra.account.repository.AccountRepository;
import com.skcc.ra.account.repository.RoleRepository;
import com.skcc.ra.account.service.AccountService;
import com.skcc.ra.account.service.LoginService;
import com.skcc.ra.account.service.RefreshTokenService;
import com.skcc.ra.account.service.UserActvyLogService;
import com.skcc.ra.common.api.dto.domainDto.CmmnCdDtlDto;
import com.skcc.ra.common.api.dto.domainDto.DeptDto;
import com.skcc.ra.common.api.dto.domainDto.UserBasicDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private CommonClient commonClient;

    @Autowired
    private UserActvyLogService userActvyLogService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArgosPasswordEncoder argosPasswordEncoder;
    //로그인
    @Override
    public JwtResponse accountLogin(LoginReqDto loginReqDto) throws UnknownHostException {

        String userid = loginReqDto.getUserid().toUpperCase();

        //로그인 불가 케이스
        Optional<Account> optional = accountRepository.findByUserid(userid);
        if (optional.isPresent()) {
            Account account = optional.get();

            //계정 삭제 확인
            if ("D".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1007");
                //승인 대기
            else if ("W".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1004");
                //계정 잠김 --> 오랫동안 사용하지 않았을 경우 잠김 || 비밀번호 5회 틀림 시 잠김 --> 계정이 잠김상태 입니다. 관리자에게 문의하세요.
            else if ("L".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1012");

            else if (!"O".equals(account.getUseridStsCd())&&!"T".equals(account.getUseridStsCd())) throw new ServiceException("COM.I1036");

            //비밀번호 5회 이상 틀림
            if (account.getPsswdErrFrqy()>=5) throw new ServiceException("COM.I1034");

            //비밀번호 일치 확인
            String connPsswd = argosPasswordEncoder.encode(loginReqDto.getConnPsswd());

            if (!connPsswd.equals(account.getConnPsswd())) {
                account.setPsswdErrFrqy(account.getPsswdErrFrqy()+1);
                if (account.getPsswdErrFrqy() >= 5) {
                    account.setUseridStsCd("L");
                    accountService.accountStsChngLog(account.getUserid(), "USERID_STS_CD", "L");
                }

                account.setLastChngrId(userid);
                account = accountRepository.save(account);

                return new JwtResponse(
                        "E",
                        "로그인 오류 " + account.getPsswdErrFrqy() + "회 <br /> 5회 이상 로그인 오류 시 보안을 위해 로그인이 제한됩니다. ");
            }

            //정상 로그인
            else {
                //정상 로그인 시, 비밀번호 에러 횟수 초기화
                account.setPsswdErrFrqy(0);
                account.setLastChngrId(userid);
                accountRepository.save(account);

                Map<String, Object> userInfo = setUserInfo(account, loginReqDto.getSystmCtgryCd());

                List<RoleDto> r = roleRepository.findUserRole(account.getUserid());
                List<String> roles = r.stream().map(item ->item.getUserRoleId()).collect(Collectors.toList());

                userInfo.put("roles", roles);

                String jwt = null;
                RefreshToken refreshToken = null;

                // jwt token 발행  대상 시스템 (모바일 제외처리)
                if ("W".equals(loginReqDto.getSystmCtgryCd())) {

                    // 승인자 역할이 포함되어 있을 경우 IP를 체크
                    String errReson = "";
                    boolean flag = false;
                    if (roles.contains(AuthType.AUTH_APPROVE.getCode())) {
                        flag = true;
                        errReson = "로그인 오류 <br /> 계정,권한 승인자의 경우 Cloud PC(VDI)에서만 접속이 가능합니다.";
                    } else if (roles.contains(AuthType.AUTH_RPA.getCode())) {
                        flag = true;
                        errReson = "로그인 오류 <br /> RPA 계정의 경우 지정된 IP에서만 접속이 가능합니다.";
                    }
                    if (flag && !StringUtils.equals(RequestUtil.getClientIP(), account.getUserIpaddr())) {
                        return new JwtResponse("E", errReson);
                    }

                    Map<String, Object> claims = new HashMap<>();
                    claims.put("ROLE", roles);

                    jwt = jwtUtils.generateTokenFromUsername(account.getUserid(), claims);
                    refreshToken = refreshTokenService.createRefreshToken(account.getUserid(), jwt);
                }

                userActvyLogService.regUserActvyLog(UserActvyLogDto.builder()
                                .userid(account.getUserid())
                                .userActvyTypeCd("1")
                                .systmCtgryCd(loginReqDto.getSystmCtgryCd())
                                .lastChngrId(account.getUserid())
                                .build());

                return new JwtResponse(jwt,
                        refreshToken == null ? null : refreshToken.getRefreshToken(),
                        "S",
                        "로그인 성공",
                        userInfo);
            }
        } else {
            return new JwtResponse("E", "사용자 계정이 존재하지 않습니다.");
        }
    }

    public Map<String, Object> setUserInfo(Account account, String systmCtgryCd){ // 로그인한 시스템에 따라 다르게 설정 필요?

        Map<String, Object> userInfo = new HashMap<>();
        // 사원정보
        String vctnCd = "", vctnNm = "", clofpCd = "", clofpNm = "";

        // 본부,부서 정보
        String bssmacd = "", bsssmanm = "";

        // 사원구분, APP 구분(모바일)
        String uslv = "", aplv = "";
        List<UserBasicDto> userBasicDtoList;
        UserBasicDto userBasic;
        DeptDto deptDto;

        try {
            AccountDto accountDto = accountRepository.searchAccountDtoInfo(account.getUserid());

            // 내부사용자 구분이 상담사 아닐 경우(인사마스터에 정보가 있을 경우) userBasic 정보를 가져와서 세팅
            if (!"1".equals(accountDto.getInnerUserClCd())) {
                userBasicDtoList = commonClient.searchUserBasic(null, null, account.getUserIdentNo());

                if (!userBasicDtoList.isEmpty()) {
                    // 인사마스터 정보 가져오기
                    userBasic = userBasicDtoList.get(0);

                    // 직능코드 -> BP 로그인시 사용자구분값 세팅 시 필요
                    vctnCd = userBasic.getVctnCd();
                    vctnNm = userBasic.getVctnNm();
                    // 직급코드, 직급명
                    clofpCd = userBasic.getClofpCd();
                    clofpNm = userBasic.getClofpNm();
                }
            }

            // 부서명 가져오기
            if (!(accountDto.getDeptcd() == null || accountDto.getDeptcd().isEmpty())) {
                deptDto = commonClient.searchDeptcd(accountDto.getDeptcd());
                if (deptDto != null) {
                    bssmacd = deptDto.getBssmacd();

                    if (bssmacd != null) {
                        CmmnCdDtlDto bssma = accountRepository.findCmmnCdValNm("BSSMACD", bssmacd);
                        if (bssma != null) bsssmanm = bssma.getCmmnCdValNm();
                    }
                }
            }
            // 전체 공통 제공
            userInfo.put("userid", accountDto.getUserid());
            userInfo.put("userNm", accountDto.getUserNm());
            userInfo.put("userIdentNo", accountDto.getUserIdentNo());
            userInfo.put("bssmacd", bssmacd);
            userInfo.put("bsssmanm", bsssmanm);
            userInfo.put("deptcd", accountDto.getDeptcd());
            userInfo.put("deptNm", accountDto.getDeptNm());
            userInfo.put("reofoCd", accountDto.getReofoCd());
            userInfo.put("reofoNm", accountDto.getReofoNm());
            userInfo.put("clofpCd", clofpCd);
            userInfo.put("clofpNm", clofpNm);
            userInfo.put("vctnCd", vctnCd);
            userInfo.put("vctnNm", vctnNm);
            userInfo.put("innerUserClCd", accountDto.getInnerUserClCd());
            userInfo.put("useridStsCd", accountDto.getUseridStsCd());

            // BLUEPATROL 등 기존 ARCOLIB.BP_LOGIN_PR1 사용대상 시스템 로그인 시 필요 userInfo 추가
            if (!"W".equals(systmCtgryCd)) {
                // 직능코드에 따른 사원구분 / APP구분 값 추가
                if ("160".equals(vctnCd) || "163".equals(vctnCd) || "165".equals(vctnCd)
                        || "175".equals(vctnCd) || "176".equals(vctnCd) || "177".equals(vctnCd) || "178".equals(vctnCd)) {
                    uslv = "BP";
                    aplv = "B";
                } else if ("155".equals(vctnCd) || "156".equals(vctnCd) || "157".equals(vctnCd)) {
                    uslv = "BP1";
                    aplv = "B";
                } else if ("310".equals(vctnCd) || "340".equals(vctnCd)) {
                    uslv = "TSE";
                    aplv = "T";
                } else if ("210".equals(vctnCd) || "215".equals(vctnCd)
                        || "220".equals(vctnCd) || "225".equals(vctnCd) || "230".equals(vctnCd)) { // 관제는 BP로 간주
                    uslv = "BP";
                    aplv = "B";
                } else {
                    uslv = "N";
                    aplv = "N";
                }

                userInfo.put("uslv", uslv);
                userInfo.put("aplv", aplv);
            }
        } catch (Exception e) {
            throw new ServiceException("COM.I1036");
        }

        return userInfo;
    }

    @Override
    public void accountLogout(HashMap<String, String> params){
        try {
            String userid = RequestUtil.getLoginUserid();
            String systmCtgryCd = params.get("systmCtgryCd");

            // token 만료 처리(refresh token 삭제)
            refreshTokenService.deleteByUserid(userid);

            // 로그아웃 활동 로그 입력
            userActvyLogService.regUserActvyLog(UserActvyLogDto.builder()
                    .userid(userid)
                    .userActvyTypeCd("2")
                    .systmCtgryCd(systmCtgryCd)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}