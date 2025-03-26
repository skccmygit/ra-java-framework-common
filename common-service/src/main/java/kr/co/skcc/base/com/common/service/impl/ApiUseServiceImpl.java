package kr.co.skcc.base.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.base.com.common.api.dto.responseDto.ApiUseHistDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ApiUseTrendDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.ApiUseHist2Dto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.ApiUseTaskStatsDto;
import kr.co.skcc.base.com.common.exception.ServiceException;
import kr.co.skcc.base.com.common.repository.ApiUseRepository;
import kr.co.skcc.base.com.common.service.ApiUseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApiUseServiceImpl implements ApiUseService {

    @Autowired
    private ApiUseRepository apiUseRepository;

    @Override
    public List<ApiUseTaskStatsDto> queryApiUseTaskStatsSearch(LocalDate useDtmtFrom, LocalDate useDtmtTo) {
        if (useDtmtFrom == null || useDtmtFrom.toString().trim().isEmpty()) throw new ServiceException("ONM.I0006");
        if (useDtmtTo == null || useDtmtTo.toString().trim().isEmpty()) throw new ServiceException("ONM.I0007");

        String useDtmtFromS = useDtmtFrom.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String useDtmtToS = useDtmtTo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return apiUseRepository.queryApiUseTaskStatsDaily(useDtmtFromS, useDtmtToS);
    }

    @Override
    public List<ApiUseTrendDto> queryApiUseTrendsSearch(String aproGroupIds, LocalDate useDtmtFrom, LocalDate useDtmtTo) {
        if (aproGroupIds == null || aproGroupIds.isEmpty()) throw new ServiceException("ONM.I0004");
        if (useDtmtFrom == null || useDtmtFrom.toString().trim().isEmpty()) throw new ServiceException("ONM.I0006");
        if (useDtmtTo == null || useDtmtTo.toString().trim().isEmpty()) throw new ServiceException("ONM.I0007");

        List<Integer> idList = new ArrayList<>();
        String[] aproGroupIdList = aproGroupIds.split(",");

        for (String aproGroupId : aproGroupIdList) {
            if (!aproGroupId.chars().allMatch(Character::isDigit)) throw new ServiceException("ONM.I0005");
            idList.add(Integer.parseInt(aproGroupId));
        }
        String useDtmtFromL = useDtmtFrom.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String useDtmtToL = useDtmtTo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return apiUseRepository.queryApiUseTrendsdDaily(idList, useDtmtFromL, useDtmtToL);
    }

    @Override
    public Page<ApiUseHistDto> queryApiUseHistSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, LocalDateTime useDtmtFrom, LocalDateTime useDtmtTo, String httMethodVal, Pageable pageable) {
        if (taskClCd == null) taskClCd = "";
        if (aproGroupClNm == null) aproGroupClNm = "";
        if (apiNmUrladdr == null) apiNmUrladdr = "";
        if (httMethodVal == null) httMethodVal = "";

        if (useDtmtTo != null) useDtmtTo = useDtmtTo.withSecond(59);

        Long totalCnt = apiUseRepository.queryApiUseHistSearchCnt(taskClCd, aproGroupClNm, apiNmUrladdr, useDtmtFrom, useDtmtTo, httMethodVal);
        Page<ApiUseHist2Dto> apiUseHist2Dtos = apiUseRepository.queryApiUseHistSearch(taskClCd, aproGroupClNm, apiNmUrladdr, useDtmtFrom, useDtmtTo, httMethodVal, pageable);

        List<ApiUseHistDto> list = new ArrayList<>();
        apiUseHist2Dtos.stream().forEach(apiUseHist2Dto -> {
            ApiUseHistDto apiUseHistDto = new ApiUseHistDto();
            BeanUtils.copyProperties(apiUseHist2Dto, apiUseHistDto);
            apiUseHistDto.setUseRate(totalCnt);
            apiUseHistDto.setAproTaskClCdNm(apiUseHistDto.getAproTaskClCd().getName());
            list.add(apiUseHistDto);
        });

        return new PageImpl<>(list, apiUseHist2Dtos.getPageable(), apiUseHist2Dtos.getTotalElements());
    }
}
