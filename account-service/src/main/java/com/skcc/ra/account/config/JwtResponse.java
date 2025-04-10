package com.skcc.ra.account.config;

import lombok.Data;

import java.util.Map;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;
    //--------- 임시 ---------
//    private String userId;
//    private String userName;
//    private List<String> roles;
    //--------- 임시 ---------
    private Map<String, Object> userInfo;
    private String resultCd;
    private String resultMsg;


//    public JwtResponse(String accessToken, String refreshToken, String userId, String userName, List<String> roles) {
//        this.token = accessToken;
//        this.refreshToken = refreshToken;
//        this.userId = userId;
//        this.userName = userName;
//        this.roles = roles;
//    }

//    public JwtResponse(String accessToken, String refreshToken, String userId, String userName, String resultCd, String resultMsg, List<String> roles, Map<String, Object> userInfo) {
//        this.token = accessToken;
//        this.refreshToken = refreshToken;
//        //--------- 임시 ---------
//        this.userId = userId;
//        this.userName = userName;
//        this.roles = roles;
//        //--------- 임시 ---------
//        this.userInfo = userInfo;
//        this.resultCd = resultCd;
//        this.resultMsg = resultMsg;
//    }

    public JwtResponse(String accessToken, String refreshToken, String resultCd, String resultMsg, Map<String, Object> userInfo) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
        this.resultCd = resultCd;
        this.resultMsg = resultMsg;
    }

    public JwtResponse(String resultCd, String resultMsg) {
        this.token = null;
        this.refreshToken = null;
        this.userInfo = null;
        this.resultCd = resultCd;
        this.resultMsg = resultMsg;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public List<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = roles;
//    }

    /**
     * @return the refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken the refreshToken to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
