package com.skcc.ra.common.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ApiUseHistDto", description = "사용이력 통계")
public class ApiUseHistDto {

    @Schema(description = "업무구분코드")
    private BizTask aproTaskClCd;

    @Schema(description = "업무구분코드명")
    private String aproTaskClCdNm;

    @Schema(description = "그룹ID")
    private int aproGroupId;

    @Schema(description = "그룹명")
    private String aproGroupClNm;

    @Schema(description = "APIID")
    private int apiId;

    @Schema(description = "API명")
    private String apiNm;

    @Schema(description = "API설명")
    private String apiDesc;

    @Schema(description = "API위치URL주소")
    private String apiLocUrladdr;

    @Schema(description = "HTT메소드값")
    private String httMethodVal;

    @Schema(description = "호출수")
    private Long apiCallCnt;

    @Schema(description = "사용율")
    private String useRate;

    public ApiUseHistDto(BizTask aproTaskClCd, int aproGroupId, String aproGroupClNm,
                         int apiId, String apiNm, String apiDesc, String apiLocUrladdr, String httMethodVal,
                         Long apiCallCnt) {
        this.aproTaskClCd = aproTaskClCd;
        if(this.aproTaskClCd != null) this.aproTaskClCdNm = this.aproTaskClCd.getName();
        this.aproGroupId = aproGroupId;
        this.aproGroupClNm = aproGroupClNm;
        this.apiId = apiId;
        this.apiNm = apiNm;
        this.apiDesc = apiDesc;
        this.apiLocUrladdr = apiLocUrladdr;
        this.httMethodVal = httMethodVal;
        this.apiCallCnt = apiCallCnt;
    }

    public void setUseRate(Long totalCnt) {
        double result = (this.apiCallCnt / (double) totalCnt) * 100.0 ;
        this.useRate = String.format("%.2f", result) + "%";
    }
}
