package com.skcc.ra.common.service.impl;

import com.skcc.ra.common.api.dto.responseDto.ApiMonitorStatsResponseDto;
import com.skcc.ra.common.util.ResponseUtil;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.common.api.dto.domainDto.ApiMonitorDto;
import com.skcc.ra.common.api.dto.responseDto.ApiMonitorDtlDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import com.skcc.ra.common.domain.apiInfo.ApiMonitor;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.repository.ApiMonitorRepository;
import com.skcc.ra.common.service.ApiMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiMonitorServiceImpl implements ApiMonitorService {

    @Autowired
    private ApiMonitorRepository apiMonitorRepository;

    @Value("${app.api-delay-time}")
    private Integer apiDelayTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public Page<ApiMonitorStatsResponseDto> queryApiMonitorStatsSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, Integer apiRespTime, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo, Boolean nomalSts, Boolean delaySts, Boolean errSts, Pageable pageable) {

        if (exectDtmtFrom == null || exectDtmtFrom.toString().trim().isEmpty())    throw new ServiceException("ONM.I0006");
        if (exectDtmtTo == null || exectDtmtTo.toString().trim().isEmpty())        throw new ServiceException("ONM.I0007");

        exectDtmtTo = exectDtmtTo.withSecond(59);

        String nomalStsVal = "0000";
        String delayStsVal = "0000";
        String errStsVal = "0000";

        if (taskClCd == null) taskClCd = "";
        if (aproGroupClNm == null) aproGroupClNm = "";
        if (apiNmUrladdr == null) apiNmUrladdr = "";
        if (apiRespTime == null) apiRespTime = -1;
        if (nomalSts == null) nomalSts = false;
        if (delaySts == null) delaySts = false;
        if (errSts == null) errSts = false;

        if (nomalSts) nomalStsVal = "2";
        if (delaySts) delayStsVal = "4";
        if (errSts) errStsVal = "5";

        if (!nomalSts && !delaySts && !errSts) {
            nomalStsVal = "";
            delayStsVal = "";
            errStsVal = "";
        }
        Page<ApiMonitorStatsDto> page = apiMonitorRepository.queryApiMonitorStatsSearch(taskClCd, aproGroupClNm, apiNmUrladdr, apiRespTime, exectDtmtFrom, exectDtmtTo, nomalStsVal, delayStsVal, errStsVal, apiDelayTime, pageable);
        Function<ApiMonitorStatsDto, ApiMonitorStatsResponseDto> converter = ApiMonitorStatsDto::from;
        return ResponseUtil.convertPage(page, converter);
    }

    @Override
    public List<ApiMonitorDto> queryApiMonitorSearch(Integer apiId, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo) {
        if (exectDtmtTo != null) exectDtmtTo = exectDtmtTo.withSecond(59);
        List<ApiMonitor> list = apiMonitorRepository.findByApiIdAndapiExectUserIdGreaterThanEqualAndapiExectUserIdLessThanEqual(
                apiId,
                exectDtmtFrom,
                exectDtmtTo
        );
        return list.stream().map(ApiMonitor::toApi).collect(Collectors.toList());
    }

    @Override
    public List<ApiMonitorDtlDto> queryApiMonitorInfoByCondition(String userId, LocalDate exectDtmt, List<Integer> apiIds) {
        LocalDateTime exectDtmtFrom = null;
        LocalDateTime exectDtmtTo = null;

        if (exectDtmt != null) {
            exectDtmtFrom = exectDtmt.atStartOfDay();
            exectDtmtTo = exectDtmt.atTime(23,59,59);
        }
        List<ApiMonitorDtlDto> list = apiMonitorRepository.queryApiMonitorInfoByCondition(userId, exectDtmtFrom, exectDtmtTo, apiIds);
        return list;
    }
}
