package kr.co.skcc.oss.gateway.service.impl;

import kr.co.skcc.oss.gateway.api.dto.TokenStatus;
import kr.co.skcc.oss.gateway.domain.Token;
import kr.co.skcc.oss.gateway.repository.TokenRepository;
import kr.co.skcc.oss.gateway.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.*;

/**
 * AuthorizationServiceImpl.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-09, 최초 작성
 */
@Service
@Transactional
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    TokenRepository tokenRepository;

    public TokenStatus getUserTokenStatusByAccessToken(String accessToken, ServerWebExchange exchange){

        /*
            최신 발행 토큰 여부 검색
         */
        Optional<Token> tokenResult = tokenRepository.findByAccessTokenOrOldAccessToken(accessToken, accessToken);
        if (!tokenResult.isPresent()) {
            /*
               (신) (구) 토큰 동시 검색해서 없을 경우 NOK Return
            */
            return TokenStatus.TOKEN_NOTOK;
        } else {
            /*
                (신/구) 토큰 있을 경우  refresh 토큰이 만료되었는지 검증
             */
            Token token = tokenResult.get();
            if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
                return TokenStatus.REFRESH_TOKEN_EXPIRED;
            }
        }
        /*
            여기까지 오면 refresh 토큰 양호하며, (신) (구) 토큰 중 1개로 API 요청했으며 만료 상태임.
            access 토큰 연장 필요 리턴
         */
        return TokenStatus.ACCESS_TOKEN_EXPIRED;
    }


    @Override
    public TokenStatus getUserTokenStatusByUserid(String userid) {
        Optional<Token> tokenResult = tokenRepository.findById(userid);
        /*
            TOKEN - STATUS
            00 : 토큰 양호
            01 : access token 시간만료 - 토큰 갱신 필요
            02 : refresh token 시간만료 - 재인증 필요
            99 : 토큰 없음 / 토큰 유효하지 않음
         */

        /*
            토큰 조회결과가 없을 경우
         */
        if (!tokenResult.isPresent()) {
            return TokenStatus.TOKEN_NOTOK;
        }

        /*
            refresh 토큰이 만료되었는지 검증
         */
        Token token = tokenResult.get();
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            return TokenStatus.REFRESH_TOKEN_EXPIRED;
        }

        /*
            access 토큰 연장 필요 리턴
         */
        return TokenStatus.ACCESS_TOKEN_EXPIRED;
    }
}
