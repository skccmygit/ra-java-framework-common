package kr.co.skcc.oss.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiMonitorDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ApiMonitorDtlDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import kr.co.skcc.oss.com.common.domain.apiInfo.ApiMonitor;
import kr.co.skcc.oss.com.common.exception.ServiceException;
import kr.co.skcc.oss.com.common.repository.ApiMonitorRepository;
import kr.co.skcc.oss.com.common.service.ApiMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiMonitorServiceImpl implements ApiMonitorService {

    @Autowired
    private ApiMonitorRepository apiMonitorRepository;

    @Value("${app.api-delay-time}")
    private Integer apiDelayTime;

    @Override
    public Page<ApiMonitorStatsDto> queryApiMonitorStatsSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, Integer apiRespTime, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo, Boolean nomalSts, Boolean delaySts, Boolean errSts, Pageable pageable) {

        if (exectDtmtFrom == null || "".equals(exectDtmtFrom.toString().trim()))    throw new ServiceException("ONM.I0006");
        if (exectDtmtTo == null || "".equals(exectDtmtTo.toString().trim()))        throw new ServiceException("ONM.I0007");

        if (exectDtmtTo != null) exectDtmtTo = exectDtmtTo.withSecond(59);

        String nomalStsVal = "0000";
        String delayStsVal = "0000";
        String errStsVal = "0000";

        if(taskClCd == null) taskClCd = "";
        if(aproGroupClNm == null) aproGroupClNm = "";
        if(apiNmUrladdr == null) apiNmUrladdr = "";
        if(apiRespTime == null) apiRespTime = -1;
        if(nomalSts == null) nomalSts = false;
        if(delaySts == null) delaySts = false;
        if(errSts == null) errSts = false;

        if(nomalSts) nomalStsVal = "2";
        if(delaySts) delayStsVal = "4";
        if(errSts) errStsVal = "5";

        if(!nomalSts && !delaySts && !errSts){
            nomalStsVal = "";
            delayStsVal = "";
            errStsVal = "";
        }

        return apiMonitorRepository.queryApiMonitorStatsSearch(taskClCd, aproGroupClNm, apiNmUrladdr, apiRespTime, exectDtmtFrom, exectDtmtTo, nomalStsVal, delayStsVal, errStsVal, apiDelayTime, pageable);
    }

    @Override
    public List<ApiMonitorDto> queryApiMonitorSearch(Integer apiId, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo) {
        if (exectDtmtTo != null) exectDtmtTo = exectDtmtTo.withSecond(59);
        List<ApiMonitor> list = apiMonitorRepository.findByApiIdAndapiExectUserIdGreaterThanEqualAndapiExectUserIdLessThanEqual(apiId, String.valueOf(exectDtmtFrom), String.valueOf(exectDtmtTo));
        return list.stream().map(ApiMonitor::toApi).collect(Collectors.toList());
    }

    @Override
    public List<ApiMonitorDtlDto> queryApiMonitorInfoByCondition(String userId, LocalDate exectDtmt, List<Integer> apiIds) {
        LocalDateTime exectDtmtFrom = null;
        LocalDateTime exectDtmtTo = null;

        if(exectDtmt != null) {
            exectDtmtFrom = exectDtmt.atStartOfDay();
            exectDtmtTo = exectDtmt.atTime(23,59,59);
        }

        return apiMonitorRepository.queryApiMonitorInfoByCondition(userId, exectDtmtFrom, exectDtmtTo, apiIds);
    }
}
