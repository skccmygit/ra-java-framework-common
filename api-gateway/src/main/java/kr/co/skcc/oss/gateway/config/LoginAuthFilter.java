package kr.co.skcc.oss.gateway.config;

import kr.co.skcc.oss.gateway.api.dto.TokenStatus;
import kr.co.skcc.oss.gateway.service.AuthorizationService;
import kr.co.skcc.oss.gateway.util.IpAddressMatcher;
import kr.co.skcc.oss.gateway.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoginAuthFilter extends AbstractGatewayFilterFactory<LoginAuthFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IpAddressMatcher ipAddressMatcher;

    @Autowired
    private AuthorizationService authorizationService;

    public static class Config {
    }

    public LoginAuthFilter() {
        super(Config.class);
    }

    /*
        인증 실패 시 처리할 메소드
     */
    private Mono<Void> returnUnAuthorized(ServerWebExchange exchange, TokenStatus tokenStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("token-status", tokenStatus.getCode());
        return response.setComplete();
    }

    private void print(String status, String msg, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        RemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
        String ip = resolver.resolve(exchange).getAddress().getHostAddress();
        String method = request.getMethod().toString();
        String path   = request.getPath().toString();
        String userid = request.getHeaders().get("ACCOUNT_ID") == null ? "" : request.getHeaders().get("ACCOUNT_ID").get(0);
        log.info("[{}][{}][{}][{} {}][{}]"
                , status
                , ip
                , userid
                , method
                , path
                , msg);
    }

    private void print(ServerWebExchange exchange){
        this.print("SUCC", "", exchange);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                ServerHttpRequest request = exchange.getRequest();
                List<String> token = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

                String tokenString = null;
                if (token != null) {
                    tokenString = token.get(0);
                }
                /*
                    인증 토큰이 있는가 ?   Bearer로 시작
                */
                if (!StringUtils.hasText(tokenString) || !tokenString.startsWith("Bearer ")) {
                    /*
                        토큰이 없을 경우
                     */
                    /*
                        white list ip 에 포함되는지 확인
                     */
                    RemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
                    String sourceIp = resolver.resolve(exchange).getAddress().getHostAddress();
                    boolean matchYn = ipAddressMatcher.matches(sourceIp);

                    if (!matchYn) {
                        /*
                            white list ip가 아닐 경우 오류 리턴
                         */
                        this.print("UNAUTH", "NOT WHITE LIST IP", exchange);
                        return returnUnAuthorized(exchange, TokenStatus.TOKEN_NOTOK);
                    } else {
                        /*
                            white list ip 대상이라면
                         */

                        /*
                            header 값에서 ACCOUNT_ID 추출
                         */
                        List<String> accountIdList = request.getHeaders().get("ACCOUNT_ID");
                        String accountId = null;
                        if (accountIdList != null) {
                            accountId = accountIdList.get(0);
                        }
                        if( accountId == null ) {
                            /*
                                ID가 없을 경우 default는 Source IP
                                 --> LastChngrId 값에 IP가 들어가는데, 자리수가 넘쳐서 insert 에러가 발생하여 일단 시스템으로 변경
                                 IP 기준으로 시스템명을 넣는 쪽으로..
                             */
                            accountId = "SYSTEM";
                            // accountId = sourceIp;
                        }

                        /*
                            WHITE_LIST_IP의 API Role
                         */
                        String whitelistRoleStr = "WHITELIST_IP";

                        ServerHttpRequest mutatedRequest = request
                                .mutate()
                                .header("ACCOUNT_ID", accountId)
                                .header("ROLES_STR", whitelistRoleStr)
                                .build();

                        exchange = exchange.mutate().request(mutatedRequest).build();
                    }
                } else {
                    /*
                        토큰이 있을 경우
                     */
                    /*
                        인증 토큰 Text 추출
                    */
                    final String jwt = tokenString.substring(7);
                    /*
                        인증 토큰이 있는가 ?
                     */
                    if (jwt == null) {
                        this.print("UNAUTH", "JWT IS NULL", exchange);
                        return returnUnAuthorized(exchange, TokenStatus.TOKEN_NOTOK);
                    }

                    /*
                        인증 토큰이 있다면 유효 한가 ?
                     */
                    try {
                        String msg = jwtUtils.validateJwtToken(jwt);
                        if (!"".equals(msg)) {
                            this.print("UNAUTH", msg, exchange);
                            return returnUnAuthorized(exchange, TokenStatus.TOKEN_NOTOK);
                        }
                    } catch (ExpiredJwtException e) {
                        /*
                            JWT 만료 일 경우 Redis 에서 Token Status 확인
                         */
                        TokenStatus ts = authorizationService.getUserTokenStatusByAccessToken(jwt, exchange);
                        if (ts.equals(TokenStatus.ACCESS_TOKEN_EXPIRED)) {
                            this.print("UNAUTH", "ACCESS TOKEN EXPIRED", exchange);
                        } else if (ts.equals(TokenStatus.TOKEN_NOTOK)) {
                            this.print("UNAUTH", "JWT EXPIRED", exchange);
                        } else {
                            this.print("UNAUTH", "REFRESH TOKEN EXPIRED", exchange);
                        }
                        return returnUnAuthorized(exchange, ts);
                    }

                    Map<String, Object> claims = jwtUtils.getClaims(jwt);
                    String userid = (String) claims.get("ACCOUNT_ID");
                    List<String> roles = (List<String>) claims.get("ROLE");

                    String rolesString =  roles.stream().collect(Collectors.joining("|"));

                    /*
                        mutate()를 사용하여 Header값에 계정정보 입력
                     */

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("ACCOUNT_ID", userid)
                            .header("ROLES_STR", rolesString)
                            .build();

                    exchange = exchange.mutate().request(mutatedRequest).build();
                }
            } catch (NullPointerException e) {
                this.print("ERR", "NULL POINTER EXCEPTION", exchange);
                return returnUnAuthorized(exchange, TokenStatus.TOKEN_NOTOK);
            }
            /*
                여기까지 왔다면 인증에 통과했음. 통과했으니, 다음 필터로 보내주자
             */
            this.print(exchange);
            return chain.filter(exchange);
        };
    }
}