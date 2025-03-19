package kr.co.skcc.oss.gateway.service;

import kr.co.skcc.oss.gateway.api.dto.TokenStatus;
import org.springframework.web.server.ServerWebExchange;

/**
 * AuthorizationService.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-09, 최초 작성
 */
public interface AuthorizationService {
    @Deprecated
    TokenStatus getUserTokenStatusByUserid(String userid);

    TokenStatus getUserTokenStatusByAccessToken(String accessToken, ServerWebExchange exchang);
}
