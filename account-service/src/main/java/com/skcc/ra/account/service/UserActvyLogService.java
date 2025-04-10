package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.UserActvyLogDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserActvyLogIDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserActvyLogService {

    Page<UserActvyLogIDto> findUserActvyLog(String systmCtgryCd, String userActvyTypeCd, List<String> deptcdList, String userNm, String userActvyDtmFrom, String userActvyDtmTo, Pageable pageable);

    void regUserActvyLog(UserActvyLogDto userActvyLogDto);

    void makeExcel(String systmCtgryCd, String userActvyTypeCd, List<String> deptcdList, String userNm, String userActvyDtmFrom, String userActvyDtmTo) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
