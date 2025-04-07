package com.skcc.ra.common.api.dto.responseDto.ifDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;

@Schema(name = "ApiUseHist2Dto", description = "사용이력 통계")
public interface ApiUseHist2Dto {

    @Schema(description = "업무구분코드")
    BizTask getAproTaskClCd();

    @Schema(description = "업무구분코드명")
    String getAproTaskClCdNm();

    @Schema(description = "그룹ID")
    int getAproGroupId();

    @Schema(description = "그룹명")
    String getAproGroupClNm();

    @Schema(description = "APIID")
    int getApiId();

    @Schema(description = "API명")
    String getApiNm();

    @Schema(description = "API설명")
    String getApiDesc();

    @Schema(description = "API위치URL주소")
    String getApiLocUrladdr();

    @Schema(description = "HTT메소드값")
    String getHttMethodVal();

    @Schema(description = "호출수")
    Long getApiCallCnt();

    @Schema(description = "사용율")
    String getUseRate();
}
