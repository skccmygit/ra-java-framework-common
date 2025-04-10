package com.skcc.ra.account.api;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto;
import com.skcc.ra.account.api.dto.requestDto.AccountRejectReqDto;
import com.skcc.ra.account.service.AccountRequestService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[계정관리] 계정 신청 관리(AccountRequestResource)", description = "계정 신청 시 사용하는 API ")
@RestController
@RequestMapping("/v1/com/account/request")
@Slf4j
public class AccountRequestResource {
    @Autowired
    private AccountRequestService accountRequestService;

    @Operation(summary = "계정 신청 내역 리스트 조회 - 조건별")
    @GetMapping
    public ResponseEntity<Page<AccountReqMgmtDto>> searchAccountRequestByCondition(@RequestParam(required = false) String userNm,
                                                                                   @RequestParam(required = false) String useridReqstStsCd,
                                                                                   @RequestParam(required = false) String useridReqstDtFrom,
                                                                                   @RequestParam(required = false)  String useridReqstDtTo,
                                                                                   Pageable pageable) {
        return new ResponseEntity<>(accountRequestService.searchAccountRequestByCondition(userNm, useridReqstStsCd, useridReqstDtFrom, useridReqstDtTo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "계정 신청")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Boolean> requestAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountRequestService.requestAccount(accountDto), HttpStatus.OK);
    }

    @Operation(summary = "신청 대상 사원 정보 조회 - 사용자 ID(사번) 기준")
    @GetMapping("/userid")
    public ResponseEntity<AccountDto> searchUserBasicByUserid(@RequestParam String userid) {
        return new ResponseEntity<>(accountRequestService.searchUserBasicByUserid(userid), HttpStatus.OK);
    }

    @Operation(summary = "계정 신청 건 검토")
    @PutMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> confirmAccountRequest(@RequestBody List<Integer> useridReqstSeqs) {
        return new ResponseEntity<>(accountRequestService.confirmAccountRequest(useridReqstSeqs), HttpStatus.OK);
    }

    @Operation(summary = "계정 신청 건 승인")
    @PutMapping("/approve")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> approveAccountRequest( @RequestBody List<Integer> useridReqstSeqs) throws UnknownHostException {
        return new ResponseEntity<>(accountRequestService.approveAccountRequest(useridReqstSeqs), HttpStatus.OK);
    }

    @Operation(summary = "계정 신청 건 반려")
    @PutMapping("/reject")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> rejectAccountRequest(@RequestBody List<AccountRejectReqDto> accountRejectReqDtos) {
        return new ResponseEntity<>(accountRequestService.rejectAccountRequest(accountRejectReqDtos), HttpStatus.OK);
    }

}