package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.responseDto.AccountUpdateDto;
import com.skcc.ra.account.api.dto.responseDto.PasswordDto;
import com.skcc.ra.account.service.AccountService;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

@Tag(name = "[계정관리] 계정 관리(AccountResource)", description = "계정 정보 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/account/user")
@Slf4j
public class AccountResource {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "계정 정보 리스트 조회 - 조건별")
    @GetMapping
    public ResponseEntity<Page<AccountDto>> searchAccount(@RequestParam(required = false) List<String> deptcdList,
                                                          @RequestParam(required = false)String useridOrNm,
                                                          @RequestParam(required = false)String useridStsCd,
                                                          @RequestParam(required = false)String deptNm,
                                                          @RequestParam(required = false)String fstRegDtmdFrom,
                                                          @RequestParam(required = false)String fstRegDtmdTo,
                                                          Pageable pageable) {
        return new ResponseEntity<>(accountService.searchAccount(deptcdList, useridOrNm, useridStsCd, deptNm, fstRegDtmdFrom, fstRegDtmdTo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "계정 정보 수정")
    @PutMapping
    public ResponseEntity<Boolean> updateAccount(@RequestBody List<AccountUpdateDto> accountUpdateDtoList) {
        Boolean result = accountService.updateAccount(accountUpdateDtoList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "계정 삭제 - 사용여부 변경")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteAccount(@RequestBody List<String> userids) throws UnknownHostException {
        return new ResponseEntity<>(accountService.deleteAccount(userids, RequestUtil.getClientIP(),false), HttpStatus.OK);
    }

    @Operation(summary = "계정 정보 조회 - 사용자 ID 기준")
    @GetMapping("/userid")
    public ResponseEntity<AccountDto> searchAccountByUserid(@RequestParam String userid) {
        return new ResponseEntity<>(accountService.searchAccountByUserid(userid), HttpStatus.OK);
    }

    @Operation(summary = "계정 정보 조회 - 사용자 ID 기준, 이름 / 휴대폰 번호만")
    @GetMapping("/noAuth")
    public ResponseEntity<HashMap<String, String>> searchInfoByUserid(@RequestParam String userid) {
        return new ResponseEntity<>(accountService.searchInfoByUserid(userid), HttpStatus.OK);
    }

    @Operation(summary = "계정 정보 리스트 조회 - 조건별2")
    @GetMapping("/search")
    public ResponseEntity<List<AccountDto>> searchAccountByCondition(@RequestParam(required = false) List<String> deptcdList,
                                                                     @RequestParam(required = false) String userNm) {
        return new ResponseEntity<>(accountService.searchAccountByCondition(deptcdList, userNm), HttpStatus.OK);
    }

    @Operation(summary = "계정 잠금 상태 변경 - toggle")
    @PutMapping("/lock")
    public ResponseEntity<Boolean> updateUseridStsForLock(@RequestBody List<String> userids) {
        return new ResponseEntity<>(accountService.updateUseridStsForLock(userids), HttpStatus.OK);
    }

    @Operation(summary = "계정 비밀번호 찾기 - 임시비밀번호 부여 후 SMS 전송")
    @GetMapping("/connPsswd")
    public ResponseEntity<String> findConnPsswd(@RequestBody HashMap<String, String> params) {
        return new ResponseEntity<>(accountService.findConnPsswd(params.get("userid"), params.get("method")), HttpStatus.OK);
    }

    @Operation(summary = "계정 비밀번호 초기화 - 사용자 ID(사번)으로 비밀번호 초기화")
    @PutMapping("/connPsswd/init")
    public ResponseEntity<Void> connPsswdReset(@RequestParam String userid) {
        accountService.connPsswdReset(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "계정 비밀번호 변경 - 비밀번호 변경 주기에 의한 변경")
    @PutMapping("/connPsswd")
    public ResponseEntity<Boolean> connPsswdChange(@RequestBody PasswordDto passwordDto) {
        return new ResponseEntity<>(accountService.connPsswdChange(passwordDto), HttpStatus.OK);
    }

}