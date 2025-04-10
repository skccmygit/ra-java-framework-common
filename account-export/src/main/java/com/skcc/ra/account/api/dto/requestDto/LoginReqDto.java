package com.skcc.ra.account.api.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginReqDto", description = "로그인 Request Dto")
public class LoginReqDto {

    @Schema(description = "사용자명")
    String userid;

    @Schema(description = "비밀번호")
    String connPsswd;

    @Schema(description = "시스템구분")
    String systmCtgryCd;

    @Schema(description = "핸드폰번호(필요시)")
    String userContPhno;
}
