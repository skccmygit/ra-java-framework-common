package com.skcc.ra.common.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.common.adaptor.client.ApiDocsClient;
import com.skcc.ra.common.api.dto.domainDto.ApiInfoDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelBodyDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelHeaderDto;
import com.skcc.ra.common.api.dto.requestDto.ApiDocsReqDto;
import com.skcc.ra.common.domain.apiInfo.ApiInfo;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.repository.ApiInfoRepository;
import com.skcc.ra.common.repository.AppGroupRepository;
import com.skcc.ra.common.service.ApiMgtService;
import com.skcc.ra.common.util.ExcelUtil;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ApiMgtServiceImpl implements ApiMgtService {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private AppGroupRepository appGroupRepository;

    @Autowired
    private ApiDocsClient apiDocsClient;

    @Override
    public List<ApiInfoDto> queryApiInfoSearch(Integer aproGroupId) {
        return apiInfoRepository.queryApiInfoSearch(aproGroupId);
    }

    @Override
    public List<ExcelDto> makeExcel() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        ExcelBodyDto body = new ExcelBodyDto();

        //파일명, 시트명 세팅
        String fileName = "API목록";
        String sheetName = "API목록";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm1 = {"업무구분", "자원그룹", "API명", "설명", "HTTP 메소드", "API URL"};

        // 헤더 List 생성
        List<String[]> headerList = new ArrayList<>();
        headerList.add(arrayHeaderNm1);

        // 헤더 길이
        header.setHLength(arrayHeaderNm1.length);

        // 헤더 List set
        header.setHeaderNm(headerList);

        // body 데이터 영문명
        String[] arrayBodyColNm = {"aproTaskClCd", "aproGroupClNm", "apiNm", "apiDesc", "httMethodVal", "apiLocUrladdr"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<ApiInfoDto> dataSet = excelApiInfoSearch();

        body.setBodyColNm(bodyColNm);
        body.setBody(dataSet);

        excelDto.add(new ExcelDto(sheetName, header, body));
        ExcelUtil.excelDownload(excelDto, fileName);

        return excelDto;
    }

    public List<ApiInfoDto> excelApiInfoSearch() {
        return apiInfoRepository.excelApiInfoSearch();
    }

    @Override
    public ApiInfoDto getApiInfo(Integer apiId) {
        Optional<ApiInfo> optional = apiInfoRepository.findById(apiId);
        if (optional.isPresent()) {
            ApiInfo apiInfo = optional.get();
            return apiInfo.toApi();
        } else {
            throw new ServiceException("ONM.I0009");
        }
    }

    @Override
    public ApiInfoDto create(ApiInfoDto apiInfoDto) {

        ApiInfo apiInfo = apiInfoDto.toEntity();
        if (apiInfoRepository.existsByApiLocUrladdrAndHttMethodVal(apiInfo.getApiLocUrladdr(), apiInfo.getHttMethodVal())) throw new ServiceException("ONM.I0010");

        apiInfo.setApiId(0);
        apiInfo.setLastChngrId(RequestUtil.getLoginUserid());
        apiInfo.setLastChngDtmd(LocalDateTime.now());
        ApiInfo entity = apiInfoRepository.save(apiInfo);

        return entity.toApi();
    }

    @Override
    public ApiInfoDto update(ApiInfoDto apiInfoDto) {
        ApiInfo apiInfo = apiInfoDto.toEntity();
        if (!apiInfoRepository.existsById(apiInfo.getApiId())) throw new ServiceException("ONM.I0002");

        ApiInfo checkApiInfo = apiInfoRepository.findByApiLocUrladdrAndHttMethodVal(apiInfo.getApiLocUrladdr(), apiInfo.getHttMethodVal());
        if (checkApiInfo != null && checkApiInfo.getApiId() != apiInfo.getApiId()) throw new ServiceException("ONM.I0010");

        apiInfo.setLastChngrId(RequestUtil.getLoginUserid());
        apiInfo.setLastChngDtmd(LocalDateTime.now());

        ApiInfo entity = apiInfoRepository.save(apiInfo);

        return entity.toApi();
    }

    @Override
    public void remove(List<Integer> apiIds) {
        apiInfoRepository.deleteAllById(apiIds);
    }

    @Override
    public List<ApiInfoDto> queryApiInfoByCondition(String taskClCd, String appType, String apiNmUrladdrDesc) {
        if (taskClCd == null) taskClCd = "";
        if (appType == null) appType = "";
        if (apiNmUrladdrDesc == null) apiNmUrladdrDesc = "";

        return apiInfoRepository.queryApiInfoByCondition(taskClCd, appType, apiNmUrladdrDesc);
    }

    @Override
    public void saveApiDocs(ApiDocsReqDto apiDocsReqDto) {
        String apiDocsUri = apiDocsReqDto.getApiDocsUri();
        int aproGroupId = apiDocsReqDto.getAproGroupId();

        if (!appGroupRepository.existsById(aproGroupId)) throw new ServiceException("ONM.I0001");

        try {
            URI uri = new URI(apiDocsUri);
            String response = apiDocsClient.getApiDocs(uri);

            JsonElement element = JsonParser.parseString(response);
            JsonObject apiDocsObject = element.getAsJsonObject();

            JsonArray serversObject = apiDocsObject.get("servers").getAsJsonArray();
            String serverUrl = serversObject.get(0).getAsJsonObject().get("url").getAsString();

            JsonObject pathsObject = apiDocsObject.get("paths").getAsJsonObject();
            Iterator<String> paths = pathsObject.keySet().iterator();

            while (paths.hasNext()) {
                String path = paths.next();
                JsonObject pathInfo = pathsObject.get(path).getAsJsonObject();
                Iterator<String> methods = pathInfo.keySet().iterator();
                while (methods.hasNext()) {
                    String method = methods.next();
                    log.info("method : " + method);
                    JsonObject methodInfo = pathInfo.get(method).getAsJsonObject();

                    String operationId = methodInfo.get("operationId").getAsString();
                    String summary = methodInfo.get("summary").getAsString();

                    String apiReqCntnt = null;
                    String apiRespCntnt = null;
                    if ("get".equals(method)) {
                        apiReqCntnt = methodInfo.get("parameters") != null ? methodInfo.get("parameters").toString() : null;
                        apiRespCntnt = methodInfo.get("responses") != null ? methodInfo.get("responses").toString() : null;
                    } else if ("put".equals(method)) {
                        apiReqCntnt = methodInfo.get("requestBody") != null ? methodInfo.get("requestBody").toString() : null;
                        apiRespCntnt = methodInfo.get("responses") != null ? methodInfo.get("responses").toString() : null;
                    } else if ("post".equals(method)) {
                        apiReqCntnt = methodInfo.get("requestBody") != null ? methodInfo.get("requestBody").toString() : null;
                        apiRespCntnt = methodInfo.get("responses") != null ? methodInfo.get("responses").toString() : null;
                    } else if ("delete".equals(method)) {
                        apiReqCntnt = methodInfo.get("requestBody") != null ? methodInfo.get("requestBody").toString() : null;
                        apiRespCntnt = methodInfo.get("responses") != null ? methodInfo.get("responses").toString() : null;
                    }

                    if (!apiInfoRepository.existsByApiLocUrladdrAndHttMethodVal(path, method.toUpperCase())) {
                        ApiInfo apiInfo = new ApiInfo();
                        apiInfo.setApiLocUrladdr(contextPath+path);
                        apiInfo.setApiNm(operationId);
                        apiInfo.setApiDesc(summary);
                        apiInfo.setHttMethodVal(method.toUpperCase());
                        apiInfo.setApiReqCntnt(apiReqCntnt);
                        apiInfo.setApiRespCntnt(apiRespCntnt);
                        apiInfo.setAproGroupId(aproGroupId);
                        apiInfo.setLastChngrId(RequestUtil.getLoginUserid());
                        apiInfo.setLastChngDtmd(LocalDateTime.now());

                        apiInfoRepository.save(apiInfo);
                    } else {
                        ApiInfo apiInfo = apiInfoRepository.findByApiLocUrladdrAndHttMethodVal(path, method.toUpperCase());
                        apiInfo.setApiLocUrladdr(path);
                        apiInfo.setApiNm(operationId.substring(0,operationId.lastIndexOf("Using")));
                        apiInfo.setApiDesc(summary);
                        apiInfo.setHttMethodVal(method.toUpperCase());
                        apiInfo.setApiReqCntnt(apiReqCntnt);
                        apiInfo.setApiRespCntnt(apiRespCntnt);
                        apiInfo.setAproGroupId(aproGroupId);
                        apiInfo.setLastChngrId(RequestUtil.getLoginUserid());
                        apiInfo.setLastChngDtmd(LocalDateTime.now());

                        apiInfoRepository.save(apiInfo);
                    }
                }
            }
        } catch (Exception e) {
            log.error("saveApiDocs error", e);
            throw new ServiceException("ONM.I0008");
        }
    }

}
