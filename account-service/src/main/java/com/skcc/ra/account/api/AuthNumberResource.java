package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.service.AuthNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[계정관리] 인증번호 인증(AuthNumberResource)", description = "인증번호를 통한 인증 시 사용하는 API ")
@RestController
@RequestMapping("/v1/com/account/authNumer")
@Slf4j
public class AuthNumberResource {

    @Autowired
    AuthNumberService authNumberService;

    @Operation(summary = "인증번호 전송 - 계정 신청 시 본인 인증")
    @GetMapping
    public ResponseEntity<String> getAuthNum(@RequestParam String userid,
                                             @RequestParam String method) {
        return new ResponseEntity<>(authNumberService.getAuthNum(userid, method), HttpStatus.OK);
    }

    @Operation(summary = "인증번호 확인 - 계정 신청 시 본인 인증")
    @GetMapping("/auth")
    public ResponseEntity<Boolean> authByAuthNum(@RequestParam String userid,
                                                 @RequestParam String authNumber) {
        return new ResponseEntity<>(authNumberService.authByAuthNum(userid, authNumber), HttpStatus.OK);
    }
}
