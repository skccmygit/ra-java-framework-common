package com.skcc.ra.account.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Password", description = "비밀번호관리")
public class PasswordDto {

    @Schema(description = "사용자ID")
    private String userid;

    @Schema(description = "변경 전 비밀번호")
    private String oldConnPsswd;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 10자 ~ 20자의 비밀번호여야 합니다.")
    @Schema(description = "변경 후 비밀번호")
    private String newConnPsswd;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 10자 ~ 20자의 비밀번호여야 합니다.")
    @Schema(description = "변경 후 비밀번호")
    private String newConnPsswdCheck;
}
