package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.UserAuthReqDto;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqHisDto;
import com.skcc.ra.account.api.dto.requestDto.UserAuthProcReqDto;
import com.skcc.ra.account.api.dto.responseDto.UserAuthReqListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface UserAuthReqService {

    Page<UserAuthReqListDto> searchUserAuthReqListByConditionForAdmin(String userNm,
                                                                      String athrtyReqstOpDtmFrom,
                                                                      String athrtyReqstOpDtmTo,
                                                                      String athrtyReqstStsCd,
                                                                      String indivInfoYn,
                                                                      Pageable pageable);

    Page<UserAuthReqListDto> searchUserAuthReqListByConditionForUser(String userNm,
                                                                     String athrtyReqstOpDtmFrom,
                                                                     String athrtyReqstOpDtmTo,
                                                                     String athrtyReqstStsCd,
                                                                     Pageable pageable);

    List<UserAuthReqHisDto> checkUserAuthReq(Integer athrtyReqstSeq);

    UserAuthReqDto createUserAuthReq(UserAuthReqDto userAuthReqDto) throws UnknownHostException;

    List<UserAuthReqDto> procUserAuthReq(UserAuthProcReqDto userAuthProcReqDto) throws UnknownHostException;

    List<UserAuthReqDto> approveUserAuthReq(List<UserAuthReqDto> userAuthReqDtoList) throws UnknownHostException;

    void createUserAuthByReqHis(UserAuthReqDto userAuthReqDto)  throws UnknownHostException;
}
