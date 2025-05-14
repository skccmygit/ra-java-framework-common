package com.skcc.ra.common.api.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiMonitorStatsResponseDto implements ApiMonitorStatsDto {
    private Long apiExectDtlSeq;
    private Integer apiRespStsVal;
    private Integer apiId;
    private Integer apiRespTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime apiExctStartDtmt;
    private BizTask aproTaskClCd;
    private String aproGroupClNm;
    private String apiNm;
    private String apiDesc;
    private String apiLocUrladdr;
    private Long delayCnt;
    private Long errCnt;

    @Override
    public String getApiREspTimeUnit() {
        return getApiRespTime() + "ms";
    }

    @Override
    public String getAproTaskClCdNm() {
        return getAproTaskClCd() != null ? getAproTaskClCd().getName() : null;
    }

    public static ApiMonitorStatsResponseDto from(ApiMonitorStatsDto dto) {
        ApiMonitorStatsResponseDto responseDto = new ApiMonitorStatsResponseDto();
        responseDto.setApiExectDtlSeq(dto.getApiExectDtlSeq());
        responseDto.setApiRespStsVal(dto.getApiRespStsVal());
        responseDto.setApiId(dto.getApiId());
        responseDto.setApiRespTime(dto.getApiRespTime());
        responseDto.setApiExctStartDtmt(dto.getApiExctStartDtmt());
        responseDto.setAproTaskClCd(dto.getAproTaskClCd());
        responseDto.setAproGroupClNm(dto.getAproGroupClNm());
        responseDto.setApiNm(dto.getApiNm());
        responseDto.setApiDesc(dto.getApiDesc());
        responseDto.setApiLocUrladdr(dto.getApiLocUrladdr());
        responseDto.setDelayCnt(dto.getDelayCnt());
        responseDto.setErrCnt(dto.getErrCnt());
        return responseDto;
    }

}
