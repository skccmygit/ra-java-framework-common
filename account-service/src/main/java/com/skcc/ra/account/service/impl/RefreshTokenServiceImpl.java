package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.config.JwtUtils;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.loginCert.RefreshToken;
import com.skcc.ra.account.repository.AccountRepository;
import com.skcc.ra.account.repository.RoleRepository;
import com.skcc.ra.account.repository.RefreshTokenRepository;
import com.skcc.ra.account.service.RefreshTokenService;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.exception.UnauthorizedException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${app.jwtExpirationMs}")
    private Long accessTokenDurationMs;

    @Value("${app.jwtTimeout}")
    private Long timeout;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public RefreshToken createRefreshToken(String userid, String accessToken) {
        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserid(userid);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setAccessToken(accessToken);
        // 최초 로그인 시에도 강제로 현재꺼 넣어준다
        refreshToken.setOldAccessToken(accessToken);
        // 토큰생성시간 추가
        refreshToken.setUpdateTime(updateTime);

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken updateAccessToken(RefreshToken refreshTokenRequest) {

        String accessToken =refreshTokenRequest.getAccessToken();
        String refreshToken =refreshTokenRequest.getRefreshToken();

        /*
            파라미터 검증
         */
        if (accessToken == null) {
            throw new ServiceException("요청 Access Token 정보가 없습니다.");
        }
        if (refreshToken == null) {
            throw new ServiceException("요청 Refresh Token 정보가 없습니다.");
        }

        /*
            AccessToken 으로 RefreshToken 검색
         */
        Optional<RefreshToken> refreshTokenResult = refreshTokenRepository.findByAccessToken(accessToken);

        /*
            (신) AccessToken 으로 검색 결과 없을 경우
         */

        if (refreshTokenResult.isEmpty()) {
            log.info("신 Token 없음 ");
            /*
                (구) AccessToken으로 재검색
            */
            Optional<RefreshToken> oldRefreshTokenResult = refreshTokenRepository.findByOldAccessToken(accessToken);

            /*
                (구) AccessToken으로 검색 결과 없을 경우
             */
            if (oldRefreshTokenResult.isEmpty()) {
                log.info("구 Token 없음 ");
                /*
                    오류 리턴
                 */
                throw new UnauthorizedException("등록된 Token 정보가 없습니다.");
            } else {
                log.info("구 Token 있음 : 현재 토큰 반환");
                /*
                    (구) AccessToken 있을 경우  갱신이 아니라 현재 Refresh Token을 반환
                    동시간에 2연속 갱신요청이 왔을 때 후속 요청에 대한 처리를 위한 로직
                 */
                return oldRefreshTokenResult.get();
            }

        }

        /*
            정상발행 검증절차
         */
        RefreshToken rt = refreshTokenResult.get();

        /*
            RefreshToken 에서 사용자ID 추출
            삭제 및 역할 조회 용
         */
        String userid = rt.getUserid();

        /*
            refresh token string 비교
         */
        if (!refreshToken.equals(rt.getRefreshToken())) {
            deleteByUserid(userid);
            log.info("case 1 : Refresh Token 정보가 불일치 : " + userid);
            throw new UnauthorizedException("Refresh Token 정보가 불일치합니다.");
        }

        /*
            타임아웃 체크 . 타임아웃 이전 갱신 요청만 유효
         */
        if (rt.getExpiryDate().compareTo(Instant.now()) < 0) {
            deleteByUserid(userid);
            log.info("case 2 : Refresh Token 만료 : " + userid);
            throw new UnauthorizedException("Refresh Token 이 만료 되었습니다.");
        }

        /*
            타임아웃 체크 .  타임아웃 이후에 갱신 요청만 유효
            TODO 토큰 시간 만료처리가 어려워서 임시로 주석 처리함.  추후 주석 해제 필요
         */
//        if(!jwtUtils.isExpired(accessToken)){
//            deleteByUserid(userid);
//            throw new UnauthorizedException("만료 전 Access Token 갱신 요청 입니다.");
//        }


        /*
        * accessToken 갱신시간 확인
        * 갱신시간 + 한시간 + accessToken 만료시간 < 현재시간 이면 1시간 동안 요청이 없던 건으로 간주하여 로그아웃처리
        * */
        LocalDateTime requestTime = LocalDateTime
                                    .parse(rt.getUpdateTime(),DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                    .plusSeconds((timeout + accessTokenDurationMs)/1000);

        long request = Long.parseLong(requestTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        long now = Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        // 갱신시간 + 한시간 + accessToken 만료시간 < 현재시간 -> api가 호출된지 1시간이 넘음..
        if (request < now ){
            log.info("Auth Timeout :: " + request + " :::: " + now);
            deleteByUserid(userid);
            log.info("case 3 : 인증 시간 만료 : " + userid);
            throw new UnauthorizedException("인증이 만료 되었습니다. 재로그인 해주세요,");
        }

        /*
            사용자ID로 권한정보 추출
         */
        Optional<Account> accountResult = accountRepository.findByUserid(userid);

        /*
            사용자검증
         */
        if (accountResult.isEmpty()) {
            log.info("case 4 : 계정없음 : " + userid);
            throw new UnauthorizedException("사용자 계정이 존재하지 않습니다.");
        }

        /*
            RefreshToken발행 검증 완료!  신규 AccessToken 발행 로직
         */
        Account account = accountResult.get();
        List<RoleDto> r = roleRepository.findUserRole(account.getUserid());
        List<String> roles = r.stream().map(item -> item.getUserRoleId()).collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("ROLE", roles);

        /*
            신규 AccessToken 발행 및 RefreshToken 시간 갱신
         */
        String newAccessToken = jwtUtils.generateTokenFromUsername(userid, claims);
        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        rt.setOldAccessToken(rt.getAccessToken());
        rt.setAccessToken(newAccessToken);
        rt.setUpdateTime(updateTime);
        refreshTokenRepository.save(rt);

        return rt;
    }

//    private boolean verifyExpiration(RefreshToken token) {
//        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(token);
//            throw new TokenRefreshException(token.getRefreshToken(), "Refresh token Expiration");
//        }
//        return token;
//    }

    @Override
    public void deleteByUserid(String userid) {
        refreshTokenRepository.deleteById(userid);
    }

    @Override
    public boolean checkTokenValid(RefreshToken refreshTokenRequest){

        String accessToken = refreshTokenRequest.getAccessToken();
        String refreshToken = refreshTokenRequest.getRefreshToken();

        try {
            if(accessToken == null || "".equals(accessToken)){
                log.info("case 1 : accessToken == null");
                return false;
            }

            if(refreshToken == null || "".equals(refreshToken)){
                log.info("case 2 : refreshToken == null");
                return false;
            }

            Optional<RefreshToken> refreshTokenResult = refreshTokenRepository.findByAccessToken(accessToken);

            // accessToken 으로 redis 조회
            if (refreshTokenResult.isPresent()) {

                // refreshToken 만료여부 체크
                RefreshToken rt = refreshTokenResult.get();
                if (rt.getExpiryDate().compareTo(Instant.now()) < 0) {
                    log.info("case 3 : refreshToken Expired : {}", RequestUtil.getClientIP());
                    return false;
                }

                // accessToken 유효여부 체크
                if (!jwtUtils.isExpired(accessToken)) {
                    log.info("case 4 : accessToken not Expired");
                    return true;
                }

                // accessToken 의 유효성 체크(timeout 시간)
                LocalDateTime requestTime = LocalDateTime
                        .parse(rt.getUpdateTime(),DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        .plusSeconds((timeout + accessTokenDurationMs)/1000);

                long request = Long.parseLong(requestTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                long now = Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                // 갱신시간 + 한시간 + accessToken 만료시간 < 현재시간 -> api가 호출된지 1시간이 넘음..
                if (request < now) {
                    log.info("case 5 : time out " + request + " < " + now);
                    return false;
                }
                return true;
            }

            // 현재 accessToken 이 아니면 return false
            else {
                // 이전 accessToken 으로 조회해봐서 있으면 true?
                Optional<RefreshToken> oldRefreshTokenResult = refreshTokenRepository.findByOldAccessToken(accessToken);
                if (oldRefreshTokenResult.isPresent()) {
                    log.info("case 6 : old accesstoken : {}", RequestUtil.getClientIP());
                    log.info("case 6 : accessToken : {}", accessToken);

                    // refreshToken 만료여부 체크
                    RefreshToken rt = oldRefreshTokenResult.get();
                    if (rt.getExpiryDate().compareTo(Instant.now()) < 0) {
                        log.info("case 6-1 : refreshToken Expired : {}", RequestUtil.getClientIP());
                        return false;
                    }

                    log.info("case 6-2 : return true : {}", rt.getAccessToken());
                    return true;
                }

                log.info("case 7 : not now, old accesstoken : {}", RequestUtil.getClientIP());
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}