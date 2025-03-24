package kr.co.skcc.oss.com.common.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ApiUseHistDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ApiUseTrendDto;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.ApiUseTaskStatsDto;
import kr.co.skcc.oss.com.common.exception.ServiceException;
import kr.co.skcc.oss.com.common.service.ApiUseService;
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

@Tag(name = "[API관리] API 사용 통계 및 이력(ApiUseResource)", description = "API 사용이력 및 통계 조회")
@RestController
@RequestMapping("/v1/com/common/apiUse")
public class ApiUseResource {
    @Autowired
    private ApiUseService apiUseService;

    @Operation(summary = "API 사용이력 통계 조회 - 업무별")
    @GetMapping("/taskStats")
    public ResponseEntity<List<ApiUseTaskStatsDto>> queryApiUseTaskStatsSearch(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate useDtmtFrom,
                                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate useDtmtTo) {

        return new ResponseEntity<>(apiUseService.queryApiUseTaskStatsSearch(useDtmtFrom, useDtmtTo), HttpStatus.OK);
    }

    
    @Operation(summary = "API 사용 추이 조회")
    @GetMapping("/useTrends")
    public ResponseEntity<List<ApiUseTrendDto>> queryApiUseTrendsSearch(@RequestParam String aproGroupIds,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate useDtmtFrom,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate useDtmtTo) {

        List<ApiUseTrendDto> apiUseTrendDtos = apiUseService.queryApiUseTrendsSearch(aproGroupIds, useDtmtFrom, useDtmtTo);
        return new ResponseEntity<>(apiUseTrendDtos, HttpStatus.OK);
    }

    
    @Operation(summary = "API 사용 이력 조회")
    @GetMapping("/hist")
    public ResponseEntity<Page<ApiUseHistDto>> queryApiUseHistSearch(@RequestParam(required = false) String taskClCd,
                                                                     @RequestParam(required = false) String aproGroupClNm,
                                                                     @RequestParam(required = false) String apiNmUrladdr,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime useDtmtFrom,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam LocalDateTime useDtmtTo,
                                                                     @RequestParam(required = false) String httMethodVal,
                                                                     Pageable pageable) {
        return new ResponseEntity<>(apiUseService.queryApiUseHistSearch(taskClCd, aproGroupClNm, apiNmUrladdr, useDtmtFrom, useDtmtTo, httMethodVal, pageable), HttpStatus.OK);
    }
}
