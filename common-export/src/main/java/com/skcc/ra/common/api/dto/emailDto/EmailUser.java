package com.skcc.ra.common.api.dto.emailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "EmailUser", description = "EmailUser")
public class EmailUser {

    @Schema(description = "보내는메일주소")
    private String fromEmailAddress;

    @Schema(description = "받는 메일주소")
    private String[] emailAddress;

    @Schema(description = "참조")
    private String[] cemailAddress;

    @Schema(description = "사용자")
    private String[] userName;

}
