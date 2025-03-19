package kr.co.skcc.oss.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.oss.com.common.api.dto.domainDto.AppGroupDto;
import kr.co.skcc.oss.com.common.domain.apiInfo.AppGroup;
import kr.co.skcc.oss.com.common.exception.ServiceException;
import kr.co.skcc.oss.com.common.repository.AppGroupRepository;
import kr.co.skcc.oss.com.common.service.AppGroupService;
import kr.co.skcc.oss.com.common.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppGroupServiceImpl implements AppGroupService {

    @Autowired
    private AppGroupRepository appGroupRepository;

    @Override
    public List<AppGroupDto> queryAppGroupSearch(String aproTaskClCd, String aproTypeClCd) {

        if(aproTaskClCd == null) aproTaskClCd = "";

        List<AppGroup> list = appGroupRepository.queryAppGroupSearch(aproTaskClCd, aproTypeClCd);
        return list.stream().map(AppGroup::toApi).collect(Collectors.toList());
    }

    @Override
    public void createAppGroups(List<AppGroupDto> appGroupDtos){
        List<AppGroup> appGroups = new ArrayList<AppGroup>();

        appGroupDtos.forEach(appGroupDto -> {
            AppGroup entity = appGroupDto.toEntity();
            if(appGroupRepository.existsById(entity.getAproGroupId())) throw new ServiceException("ONM.I0003");
            entity.setLastChngrId(RequestUtil.getLoginUserid());
            entity.setLastChngDtmd(LocalDateTime.now());
            appGroups.add(entity);
        });

        appGroupRepository.saveAll(appGroups);
    }

    @Override
    public void update(List<AppGroupDto> appGroupDtos) {
        List<AppGroup> appGroups = new ArrayList<AppGroup>();

        appGroupDtos.forEach(appGroupDto -> {
            AppGroup entity = appGroupDto.toEntity();
            if(!appGroupRepository.existsById(entity.getAproGroupId())) throw new ServiceException("ONM.I0001");
            entity.setLastChngrId(RequestUtil.getLoginUserid());
            entity.setLastChngDtmd(LocalDateTime.now());
            appGroups.add(entity);
        });

        appGroupRepository.saveAll(appGroups);
    }

    @Override
    public void multiAppGroups(List<AppGroupDto> appGroupDtos) {
        List<AppGroup> appGroups = new ArrayList<AppGroup>();

        appGroupDtos.forEach(appGroupDto -> {
            AppGroup entity = appGroupDto.toEntity();
            if(entity.getAproGroupId() != 0 && !appGroupRepository.existsById(entity.getAproGroupId())) throw new ServiceException("ONM.I0001");
            entity.setLastChngrId(RequestUtil.getLoginUserid());
            entity.setLastChngDtmd(LocalDateTime.now());
            appGroups.add(entity);
        });

        appGroupRepository.saveAll(appGroups);
    }

    @Override
    public void remove(List<Integer> aproGroupIds) {
        appGroupRepository.deleteAllById(aproGroupIds);
    }
}
