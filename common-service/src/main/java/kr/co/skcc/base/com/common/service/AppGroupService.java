package kr.co.skcc.base.com.common.service;

import kr.co.skcc.base.com.common.api.dto.domainDto.AppGroupDto;

import java.util.List;

public interface AppGroupService {

    List<AppGroupDto> queryAppGroupSearch(String aproTaskClCd, String aproTypeClCd);

    void createAppGroups(List<AppGroupDto> appGroupDtos);

    void update(List<AppGroupDto> appGroupDtos);

    void remove(List<Integer> aproGroupIds);

    void multiAppGroups(List<AppGroupDto> appGroupDtos);

}
