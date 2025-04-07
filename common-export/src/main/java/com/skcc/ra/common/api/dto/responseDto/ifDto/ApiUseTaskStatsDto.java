package com.skcc.ra.common.api.dto.responseDto.ifDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;

@Schema(name = "ApiUseTaskStatsDto", description = "사용 업무별 통계")
public interface ApiUseTaskStatsDto {

    @Schema(description = "순위")
    int getRownum();

    @Schema(description = "업무구분코드")
    BizTask getAproTaskClCd();

    @Schema(description = "업무구분코드명")
    default String getAproTaskClcdNm() {
        return getAproTaskClCd() != null ? getAproTaskClCd().getName() : null;};

    @Schema(description = "그룹ID")
    int getAproGroupId();

    @Schema(description = "그룹명")
    String getAproGroupClNm();

    @Schema(description = "그룹 호출 갯수")
    Long getGroupCallCnt();

    @Schema(description = "그룹별 사용율")
    String getGroupCallRate();

    @Schema(description = "총 사용율")
    String getTotalCallRate();

}
