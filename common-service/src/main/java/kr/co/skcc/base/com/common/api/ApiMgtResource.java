package kr.co.skcc.oss.com.common.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiInfoDto;
import kr.co.skcc.oss.com.common.api.dto.requestDto.ApiDocsReqDto;
import kr.co.skcc.oss.com.common.service.ApiMgtService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[API관리] API 기본정보 관리(ApiMgtResource)", description = "시스템에서 사용하는 API 기본정보에 대한 관리")
@RestController
@RequestMapping("/v1/com/common/apiMgt")
@Slf4j
public class ApiMgtResource {
    @Autowired
    private ApiMgtService apiMgtService;

    @Operation(summary = "API 기본목록 조회 - Application 그룹 ID 기준")
    @GetMapping
    public ResponseEntity<List<ApiInfoDto>> queryApiInfoSearch(@RequestParam Integer aproGroupId) {
        return new ResponseEntity<>(apiMgtService.queryApiInfoSearch(aproGroupId), HttpStatus.OK);
    }

    @Operation(summary = "API 기본목록 엑셀다운로드")
    @GetMapping("/excel")
    public ResponseEntity<List<ApiInfoDto>> excelApiInfoSearch() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        apiMgtService.makeExcel();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "API 상세정보 조회 - API ID 기준")
    @GetMapping("/apiId")
    public ResponseEntity<ApiInfoDto> getApiInfo(@RequestParam Integer apiId) {
        return new ResponseEntity<>(apiMgtService.getApiInfo(apiId), HttpStatus.OK);
    }

    @Operation(summary = "API 기본정보 생성")
    @PostMapping
    public ResponseEntity<ApiInfoDto> create(@RequestBody ApiInfoDto apiInfoDto) {
        return new ResponseEntity<>(apiMgtService.create(apiInfoDto), HttpStatus.OK);
    }

    @Operation(summary = "API 기본정보 수정")
    @PutMapping
    public ResponseEntity<ApiInfoDto> update(@RequestBody ApiInfoDto apiInfoDto) {
        return new ResponseEntity<>(apiMgtService.update(apiInfoDto), HttpStatus.OK);
    }

    @Operation(summary = "API 기본정보 삭제")
    @DeleteMapping
    public ResponseEntity<HttpStatus> remove(@RequestBody List<Integer> apiIds) {
        apiMgtService.remove(apiIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "API 리스트 조회 - 검색조건")
    @GetMapping("/apiInfoByCondition")
    public ResponseEntity<List<ApiInfoDto>> queryApiInfoByCondition(@RequestParam(required = false) String taskClCd,
                                                                    @RequestParam(required = false) String appType,
                                                                    @RequestParam(required = false) String apiNmUrladdrDesc) {
        return new ResponseEntity<>(apiMgtService.queryApiInfoByCondition(taskClCd, appType, apiNmUrladdrDesc), HttpStatus.OK);
    }

    @Operation(summary = "API 기본 정보 저장 - SwaggerDoc 활용")
    @PostMapping("/saveApiDocs")
    public ResponseEntity<HttpStatus> saveApiDocs(@RequestBody ApiDocsReqDto apiDocsReqDto) throws URISyntaxException {
        apiMgtService.saveApiDocs(apiDocsReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
