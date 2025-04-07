package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.responseDto.ApiUseHistDto;
import com.skcc.ra.common.api.dto.responseDto.ApiUseTrendDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiUseTaskStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ApiUseService {

    List<ApiUseTaskStatsDto> queryApiUseTaskStatsSearch(LocalDate useDtmtFrom, LocalDate useDtmtTo);

    List<ApiUseTrendDto> queryApiUseTrendsSearch(String aproGroupIds,LocalDate useDtmtFrom, LocalDate useDtmtTo);

    Page<ApiUseHistDto> queryApiUseHistSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, LocalDateTime useDtmtFrom, LocalDateTime useDtmtTo, String httMethodVal, Pageable pageable);
}
