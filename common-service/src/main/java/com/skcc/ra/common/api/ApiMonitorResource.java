package com.skcc.ra.common.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.common.api.dto.domainDto.ApiMonitorDto;
import com.skcc.ra.common.api.dto.responseDto.ApiMonitorDtlDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import com.skcc.ra.common.service.ApiMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "[API관리] API 모니터링(ApiMonitorResource)", description = "API 사용에 대한 모니터링")
@RestController
@RequestMapping("/v1/com/common/apiMonitor")
public class ApiMonitorResource {

    @Autowired
    private ApiMonitorService apiMonitorService;
    @Operation(summary = "API 모니터링 조회 - 상태값 기준")
    @GetMapping("/stats")
    public ResponseEntity<Page<ApiMonitorStatsDto>> queryApiMonitorStatsSearch(@RequestParam(required = false) String taskClCd,
                                                                               @RequestParam(required = false) String aproGroupClNm,
                                                                               @RequestParam(required = false) String apiNmUrladdr,
                                                                               @RequestParam(required = false) Integer apiRespTime,
                                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime exectDtmtFrom,
                                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime exectDtmtTo,
                                                                               @RequestParam(required = false) Boolean nomalSts,
                                                                               @RequestParam(required = false) Boolean delaySts,
                                                                               @RequestParam(required = false) Boolean errSts,
                                                                               Pageable pageable) {
        return new ResponseEntity<>(apiMonitorService.queryApiMonitorStatsSearch(taskClCd, aproGroupClNm, apiNmUrladdr, apiRespTime, exectDtmtFrom, exectDtmtTo, nomalSts, delaySts, errSts, pageable), HttpStatus.OK);
    }

    @Operation(summary = "API 모니터링 조회 - API ID 기준")
    @GetMapping
    public ResponseEntity<List<ApiMonitorDto>> queryApiMonitorSearch(@RequestParam Integer apiId,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime exectDtmtFrom,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime exectDtmtTo) {
        return new ResponseEntity<>(apiMonitorService.queryApiMonitorSearch(apiId, exectDtmtFrom, exectDtmtTo), HttpStatus.OK);
    }

    @Operation(summary = "API 모니터링 조회 - 검색조건")
    @GetMapping("/condition")
    public ResponseEntity<List<ApiMonitorDtlDto>> queryApiMonitorInfoByCondition(@RequestParam String userId,
                                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate exectDtmt,
                                                                                 @RequestParam List<Integer> apiIds) {
        return new ResponseEntity<>(apiMonitorService.queryApiMonitorInfoByCondition(userId, exectDtmt, apiIds), HttpStatus.OK);
    }
}
