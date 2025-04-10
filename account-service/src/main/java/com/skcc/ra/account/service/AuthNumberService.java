package com.skcc.ra.account.service;

public interface AuthNumberService {
    String getAuthNum(String userid, String method);

    Boolean authByAuthNum(String userid, String authNumber);

    String createAuthNumber();

    void saveAuthNumber(String userid, String authRand);

}
