package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.domainDto.ApiInfoDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelDto;
import com.skcc.ra.common.api.dto.requestDto.ApiDocsReqDto;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ApiMgtService {

    List<ApiInfoDto> queryApiInfoSearch(Integer aproGroupId);

    List<ExcelDto> makeExcel() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ApiInfoDto getApiInfo(Integer apiId);

    ApiInfoDto create(ApiInfoDto apiInfoDto);

    ApiInfoDto update(ApiInfoDto apiInfoDto);

    void remove(List<Integer> apiIds);

    List<ApiInfoDto> queryApiInfoByCondition(String taskClCd, String appType, String apiNmUrladdrDesc);

    void saveApiDocs(ApiDocsReqDto apiDocsReqDto);
}
