package kr.co.skcc.oss.gateway.api.dto;

/**
 * TokenStatus.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-04-27, 최초 작성
 */
public enum TokenStatus {
    /*
        token-status
        0 : 토큰 양호
        1 : access token 시간만료 - 토큰 갱신 필요
        2 : refresh token 시간만료 - 재인증 필요
        9 : 토큰 없음 / 토큰 유효하지 않음
     */

    TOKEN_OK("0"),
    ACCESS_TOKEN_EXPIRED("1"),
    REFRESH_TOKEN_EXPIRED("2"),
    TOKEN_NOTOK("9");

    private final String code;

    TokenStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}



