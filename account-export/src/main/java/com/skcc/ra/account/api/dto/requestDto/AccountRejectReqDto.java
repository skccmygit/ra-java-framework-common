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
@Schema(name = "AccountRejectReqDto", description = "사용자계정반려")
public class AccountRejectReqDto {

    @Schema(description = "사용자ID신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer useridReqstSeq;

    @Schema(description = "반려사유내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String gvbkResonCntnt;


}
