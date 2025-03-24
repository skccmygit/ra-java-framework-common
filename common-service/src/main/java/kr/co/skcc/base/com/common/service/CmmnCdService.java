package kr.co.skcc.oss.com.common.service;

import kr.co.skcc.oss.com.common.api.dto.domainDto.CmmnCdDtlDto;
import kr.co.skcc.oss.com.common.api.dto.domainDto.CmmnCdDto;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public interface CmmnCdService {

    CmmnCdDto create(CmmnCdDto cmmnCdDto);
    CmmnCdDto update(CmmnCdDto cmmnCdDto);

    List<CmmnCdDto> findCmmnCdList(String systmClCd, String chrgTaskGroupCd, String cmmnCdNm);
    void excelDownloadCmmnCd(String systmClCd, String chrgTaskGroupCd, String cmmnCdNm) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    CmmnCdDtlDto createDtl(CmmnCdDtlDto cmmnCdDtlDto);

    CmmnCdDtlDto updateDtl(CmmnCdDtlDto cmmnCdDtlDto);

    List<CmmnCdDtlDto> findByCmmnCd(String cmmnCd);
    HashMap<String, Object> findByCmmnCdDtlList(List<String> cmmnCd);
    void updateDtlList(List<CmmnCdDtlDto> cmmnCdDtlDtoList);

    List<CmmnCdDtlDto> findCmmnCdDtlByCondition(String systmClCd, String chrgTaskGroupCd, String cmmnCdNm);
    List<CmmnCdDtlDto> searchCmmnCdDtlBySuperfindByCmmnCd(String cmmnCd, String superCmmnCd, String superCmmnCdVal);
}
