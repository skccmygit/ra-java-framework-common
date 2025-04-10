package com.skcc.ra.account.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.RefreshTokenDto;
import com.skcc.ra.account.domain.loginCert.RefreshToken;
import com.skcc.ra.account.service.AuthorizationService;
import com.skcc.ra.account.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[권한관리] 권한 변경 이력 관리(AuthHistResource)", description = "API 권한 확인 및 인증 토큰 관리")
@Slf4j
@RestController
@RequestMapping("/v1/com/account/authorization")
public class AuthorizationResource {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    
    @Operation(summary = "Access Token 갱신")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenDto request) {
        RefreshToken token = refreshTokenService.updateAccessToken(request.toEntity());
        return ResponseEntity.ok(token.toApi());
    }

    
    @Operation(summary = "Access Token 유효성 체크")
    @PostMapping("/tokenAliveChk")
    public ResponseEntity<Boolean> tokenAliveChk(@RequestBody RefreshTokenDto request) {
        return new ResponseEntity<>(refreshTokenService.checkTokenValid(request.toEntity()), HttpStatus.OK);
    }

    
    @Operation(summary = "API 권한 조회 - 역할, 사용자 기준")
    @GetMapping("/api")
    public boolean getApiAuthorization(@RequestParam(required = false) String userId,
                                       @RequestParam(required = false) List<String> roles,
                                       @RequestParam String apiPath,
                                       @RequestParam String methodValue){
        return authorizationService.isAuthorizedAccess(userId, roles, apiPath, methodValue);
    }
}
