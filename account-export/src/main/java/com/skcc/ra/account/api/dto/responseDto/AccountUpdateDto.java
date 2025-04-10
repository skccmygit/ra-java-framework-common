package com.skcc.ra.account.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AccountUpdateDto", description = "계정수정 Dto")
public class AccountUpdateDto {

    @Schema(description = "사용자ID")
    private String userid;

    @Schema(description = "사용자명")
    private String userNm;


    @Schema(description = "사용자 IP주소")
    private String userIpaddr;

    @Schema(description = "사용자 휴대폰번호")
    private String userContPhno;
}
