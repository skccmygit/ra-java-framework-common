package kr.co.skcc.oss.com.common.service;

import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiInfoDto;
import kr.co.skcc.oss.com.common.api.dto.excelDto.ExcelDto;
import kr.co.skcc.oss.com.common.api.dto.requestDto.ApiDocsReqDto;

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
