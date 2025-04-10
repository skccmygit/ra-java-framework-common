package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.requestDto.LoginReqDto;
import com.skcc.ra.account.config.JwtResponse;
import com.skcc.ra.account.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.HashMap;

@Tag(name = "[인증] 로그인 / 로그아웃(LoginResource)", description = "로그인 / 로그아웃 관리")
@RestController
@RequestMapping("/v1/com/account")
@Slf4j
public class LoginResource {

    @Autowired
    private LoginService loginService;
    
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> accountLogin(@RequestBody LoginReqDto loginReqDto) throws UnknownHostException {
        JwtResponse jwtResponse = loginService.accountLogin(loginReqDto);
        return ResponseEntity.ok(jwtResponse);
    }

    
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> accountLogout(@RequestBody HashMap<String, String> params) {
        loginService.accountLogout(params);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}