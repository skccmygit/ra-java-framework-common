package com.skcc.ra.account.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.UserActvyLogDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserActvyLogIDto;
import com.skcc.ra.account.service.UserActvyLogService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[이력관리] 사용 이력 관리(UseLogResource)", description = "사용 이력 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/account/useLog")
@Slf4j
public class UseLogResource {
    @Autowired
    private UserActvyLogService userActvyLogService;

    @Operation(summary = "사용 이력 등록")
    @PostMapping
    public ResponseEntity<Void> regUserActvyLog(@RequestBody UserActvyLogDto userActvyLogDto) {
        userActvyLogService.regUserActvyLog(userActvyLogDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    @Operation(summary = "사용 이력 조회 - 조건별")
    @GetMapping
    public ResponseEntity<Page<UserActvyLogIDto>> searchUserActvyLogByCondition(@RequestParam(required = false)String systmCtgryCd,
                                                                                @RequestParam(required = false)String userActvyTypeCd,
                                                                                @RequestParam(required = false)List<String> deptcdList,
                                                                                @RequestParam(required = false)String userNm,
                                                                                @RequestParam String userActvyDtmFrom,
                                                                                @RequestParam String userActvyDtmTo,
                                                                                Pageable pageable) {
        return new ResponseEntity<>(userActvyLogService.findUserActvyLog(systmCtgryCd, userActvyTypeCd, deptcdList, userNm, userActvyDtmFrom, userActvyDtmTo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "사용 이력 엑셀다운로드")
    @GetMapping("/excel")
    public ResponseEntity<Void> downloadUserActvyLog(@RequestParam(required = false)String systmCtgryCd,
                                               @RequestParam(required = false)String userActvyTypeCd,
                                               @RequestParam(required = false)List<String> deptcdList,
                                               @RequestParam(required = false)String userNm,
                                               @RequestParam String userActvyDtmFrom,
                                               @RequestParam String userActvyDtmTo) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        userActvyLogService.makeExcel(systmCtgryCd, userActvyTypeCd, deptcdList, userNm, userActvyDtmFrom, userActvyDtmTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
