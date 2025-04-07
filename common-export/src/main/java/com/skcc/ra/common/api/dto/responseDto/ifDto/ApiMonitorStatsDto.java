package com.skcc.ra.common.api.dto.responseDto.ifDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skcc.ra.common.domain.apiInfo.type.BizTask;

import java.time.LocalDateTime;

public interface ApiMonitorStatsDto {
     Long getApiExectDtlSeq();
     int getApiRespStsVal();
     int getApiId();
     int getApiRespTime();
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
}
