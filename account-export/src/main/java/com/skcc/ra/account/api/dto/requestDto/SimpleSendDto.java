package com.skcc.ra.account.api.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SimpleSendDto", description = "SimpleSend Dto")
public class SimpleSendDto {

    @Schema(description = "수신자번호")
    String rcverPhno;

    @Schema(description = "SMS형식ID")
    String smsMsgFormId;

    @Schema(description = "param")
    List<String> params;
}
