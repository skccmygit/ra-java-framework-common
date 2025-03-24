package kr.co.skcc.base.com.common.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Schema(name = "ApiUseTrendDto", description = "사용비율")
public class ApiUseTrendDto {

    @Schema(description = "업무구분코드")
    private int aproGroupId;

    @Schema(description = "그룹ID")
    private String aproGroupClNm;

    @Schema(description = "호출일시")
    private String callDtm;

    @Schema(description = "날자별 호출 건수")
    private Long callDtmCnt;

    public ApiUseTrendDto(int aproGroupId, String aproGroupClNm, String callDtm, Long callDtmCnt) {
        this.aproGroupId = aproGroupId;
        this.aproGroupClNm = aproGroupClNm;
        this.callDtm = callDtm;
        this.callDtmCnt = callDtmCnt;
    }
}
