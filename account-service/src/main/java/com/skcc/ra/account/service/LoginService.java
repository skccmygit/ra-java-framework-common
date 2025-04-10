package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.requestDto.LoginReqDto;
import com.skcc.ra.account.config.JwtResponse;

import java.net.UnknownHostException;
import java.util.HashMap;

public interface LoginService {

    JwtResponse accountLogin(LoginReqDto loginReqDto) throws UnknownHostException;

    void accountLogout(HashMap<String, String> params);
}
