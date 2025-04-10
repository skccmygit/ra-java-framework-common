package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.responseDto.AccountUpdateDto;
import com.skcc.ra.account.api.dto.responseDto.PasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface AccountService {

    AccountDto searchAccountByUserid(String userid);

    HashMap<String, String>searchInfoByUserid(String userid);

    Page<AccountDto> searchAccount(List<String> deptcdList, String useridOrNm, String useridStsCd, String deptNm
                                , String fstRegDtmdFrom, String fstRegDtmdTo, Pageable pageable);

    List<AccountDto> searchAccountByCondition(List<String> deptcdList, String userNm);

    Boolean updateAccount(List<AccountUpdateDto> accountUpdateDtoList);

    Boolean updateUseridStsForLock(List<String> userids);

    Boolean deleteAccount(List<String> userids, String ipAddr, boolean isCtc);

    String findConnPsswd(String userid, String method);

    //비밀번호 변경
    Boolean connPsswdChange(PasswordDto passwordDto);

    void connPsswdReset(String userid);

    void accountStsChngLog(String userid, String chngColEngshNm, String chngColVal);

    String generateTempPassword();
}
