package com.skcc.ra.common.api.dto.responseDto.ifDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skcc.ra.common.api.dto.responseDto.ApiMonitorStatsResponseDto;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;

import java.time.LocalDateTime;

public interface ApiMonitorStatsDto {
     Long getApiExectDtlSeq();
     Integer getApiRespStsVal();
     Integer getApiId();
     Integer getApiRespTime();
     default String getApiREspTimeUnit() {
          return getApiRespTime() + "ms";
     }
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
     LocalDateTime getApiExctStartDtmt();
     BizTask getAproTaskClCd();
     default String getAproTaskClCdNm() {
          return getAproTaskClCd() != null ? getAproTaskClCd().getName() : null;
     }
     String getAproGroupClNm();
     String getApiNm();
     String getApiDesc();
     String getApiLocUrladdr();
     Long getDelayCnt();
     Long getErrCnt();

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
