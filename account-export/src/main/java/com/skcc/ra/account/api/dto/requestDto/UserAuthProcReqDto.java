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
@Schema(name = "UserAuthProcReqDto", description = "추가권한검토/반려")
public class UserAuthProcReqDto {

    @Schema(description = "권한신청순번", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> athrtyReqstSeq;

    @Schema(description = "사유", requiredMode = Schema.RequiredMode.REQUIRED)
    private String gvbkResonCntnt;

    @Schema(description = "상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

}
