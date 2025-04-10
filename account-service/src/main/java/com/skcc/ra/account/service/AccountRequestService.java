package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto;
import com.skcc.ra.account.api.dto.requestDto.AccountRejectReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface AccountRequestService {

    AccountDto searchUserBasicByUserid(String empno);
//
//    String getAuthNum(String userid, String method);
//
//    Boolean authByAuthNum(String userid, String authNumber);

    Boolean requestAccount(AccountDto accountDto);

    Page<AccountReqMgmtDto> searchAccountRequestByCondition(String userNm, String useridReqstStsCd, String useridReqstDtFrom, String useridReqstDtTo, Pageable pageable);

    Boolean confirmAccountRequest(List<Integer> useridReqstSeqs);

    Boolean approveAccountRequest(List<Integer> useridReqstSeqs) throws UnknownHostException;

    Boolean rejectAccountRequest(List<AccountRejectReqDto> accountRejectReqDtos);

}
