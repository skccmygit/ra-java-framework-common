package kr.co.skcc.oss.gateway.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JwtUtils.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2021-10-19, 최초 작성
 */

@Component
@Slf4j
public class JwtUtils {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateTokenFromUsername(String username, Map<String, Object> claims) {

        /*
            setSubject가 작동하지 않아서 Claim Body에 User ID 저장
         */
        claims.put("ACCOUNT_ID", username);

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJwtToken(String token) {
        Map<String, Object> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return (String)claims.get("ACCOUNT_ID");
    }

    public String validateJwtToken(String authToken) {
        String msg = "";
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return msg;
        } catch (SignatureException e) {
            msg = "INVALID JWT SIGNATURE : " + e.getMessage();
        } catch (MalformedJwtException e) {
            msg = "INVALID JWT TOKEN : " + e.getMessage();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            msg = "JWT TOKEN IS UNSUPPORTED : " + e.getMessage();
        } catch (IllegalArgumentException e) {
            msg = "JWT CLAIMS STRING IS EMPTY : " + e.getMessage();
        }

        return msg;
    }

    public Map<String, Object> getClaims(String token){
        Map<String, Object> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            //logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            //logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            //logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            //logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return claims;
    }
}