package kr.co.skcc.oss.com.common.service;

import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiMonitorDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ApiMonitorDtlDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ApiMonitorService {

    Page<ApiMonitorStatsDto> queryApiMonitorStatsSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, Integer apiRespTime, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo, Boolean nomalSts, Boolean delaySts, Boolean errSts, Pageable pageable);

    List<ApiMonitorDto> queryApiMonitorSearch(Integer apiId, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo);

    List<ApiMonitorDtlDto> queryApiMonitorInfoByCondition(String userId, LocalDate exectDtmt, List<Integer> apiIds);
}
