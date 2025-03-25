package kr.co.skcc.base.com.common.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDto;
import kr.co.skcc.base.com.common.service.CmmnCdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@Tag(name = "[기타] 코드 관리(CmmnCdResource)", description = "코드 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/common/cmmnCd")
@Slf4j
public class CmmnCdResource {
    @Autowired
    private CmmnCdService cmmnCdService;

    @Operation(summary = "코드 마스터 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CmmnCdDto> create(@RequestBody CmmnCdDto cmmnCdDto) {
        return new ResponseEntity<>(cmmnCdService.create(cmmnCdDto), HttpStatus.OK);
    }


    @Operation(summary = "코드 마스터 수정")
    @PutMapping
    public ResponseEntity<CmmnCdDto> update(@RequestBody CmmnCdDto cmmnCdDto) {
        return new ResponseEntity<>(cmmnCdService.update(cmmnCdDto), HttpStatus.OK);
    }


    @Operation(summary = "코드 마스터 조회 - 조건별")
    @GetMapping("/condition")
    public ResponseEntity<List<CmmnCdDto>> findByCmmnCdNm(@RequestParam(required = false) String chrgTaskGroupCd,
                                                          @RequestParam(required = false) String cmmnCdNm) {
        return new ResponseEntity<>(cmmnCdService.findCmmnCdList(chrgTaskGroupCd, cmmnCdNm), HttpStatus.OK);
    }


    @Operation(summary = "코드 마스터 + 상세 엑셀 다운로드")
    @GetMapping("/excel")
    public ResponseEntity<HttpStatus> excelDownloadCmmnCd(@RequestParam(required = false) String chrgTaskGroupCd,
                                                          @RequestParam(required = false) String cmmnCdNm) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        cmmnCdService.excelDownloadCmmnCd(chrgTaskGroupCd, cmmnCdNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 등록")
    @PostMapping("/dtl")
    public ResponseEntity<CmmnCdDtlDto> createDtl(@RequestBody CmmnCdDtlDto cmmnCdDtlDto) {
        return new ResponseEntity<>(cmmnCdService.createDtl(cmmnCdDtlDto), HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 수정 - 단건")
    @PutMapping("/dtl")
    public ResponseEntity<CmmnCdDtlDto> updateDtl(@RequestBody CmmnCdDtlDto cmmnCdDtlDto) {
        return new ResponseEntity<>(cmmnCdService.updateDtl(cmmnCdDtlDto), HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 수정 - 다건")
    @PutMapping("/dtl/multi")
    public ResponseEntity<HttpStatus> updateDtlList(@RequestBody List<CmmnCdDtlDto> cmmnCdDtlDtoList) {
        cmmnCdService.updateDtlList(cmmnCdDtlDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 조회 - 단건")
    @GetMapping("/dtl")
    public ResponseEntity<List<CmmnCdDtlDto>> findByCmmnCd(@RequestParam String cmmnCd) {
        return new ResponseEntity<>(cmmnCdService.findByCmmnCd(cmmnCd), HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 조회 - 다건")
    @GetMapping("/dtl/multi")
    public ResponseEntity<HashMap<String, Object>> findByCmmnCdList(@RequestParam List<String> cmmnCdList) {
        return new ResponseEntity<>(cmmnCdService.findByCmmnCdDtlList(cmmnCdList), HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 조회 - 조건별")
    @GetMapping("/dtl/condition")
    public ResponseEntity<List<CmmnCdDtlDto>> searchCmmnCdDtlByCondition(
            @RequestParam(required = false) String chrgTaskGroupCd,
            @RequestParam(required = false) String cmmnCdNm) {
        return new ResponseEntity<>(cmmnCdService.findCmmnCdDtlByCondition(chrgTaskGroupCd, cmmnCdNm), HttpStatus.OK);
    }


    @Operation(summary = "코드 상세 조회 - 상위코드 기준")
    @GetMapping("/dtl/superCd")
    public ResponseEntity<List<CmmnCdDtlDto>> searchCmmnCdDtlBySuperfindByCmmnCd(@RequestParam String cmmnCd,
                                                                                 @RequestParam(required = false) String superCmmnCd,
                                                                                 @RequestParam(required = false) String superCmmnCdVal) {
        return new ResponseEntity<>(cmmnCdService.searchCmmnCdDtlBySuperfindByCmmnCd(cmmnCd, superCmmnCd, superCmmnCdVal), HttpStatus.OK);
    }

}
