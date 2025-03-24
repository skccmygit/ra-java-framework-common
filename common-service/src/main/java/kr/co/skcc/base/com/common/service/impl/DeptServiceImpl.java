package kr.co.skcc.oss.com.common.service.impl;

import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.oss.com.common.api.dto.constant.CommonConstant;
import kr.co.skcc.oss.com.common.api.dto.domainDto.DeptDto;
import kr.co.skcc.oss.com.common.domain.dept.Bssmacd;
import kr.co.skcc.oss.com.common.domain.dept.Dept;
import kr.co.skcc.oss.com.common.repository.BssmacdRepository;
import kr.co.skcc.oss.com.common.repository.DeptRepository;
import kr.co.skcc.oss.com.common.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    private BssmacdRepository bssmacdRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public List<DeptDto> searchDeptList(String superDeptcd) {

        String deptcd = StringUtils.isEmpty(superDeptcd) ? CommonConstant.SUPER_DEPT_CD : superDeptcd;

        List<Dept> allList = deptRepository.findByUseYn("Y", Sort.by("deptcd"));

        List<DeptDto> hList = new ArrayList<>();

        if(!allList.isEmpty()) {
            hList = allList.stream()
                    .filter(m -> deptcd.equals(m.getSuperDeptcd()))
                    .collect(Collectors.toList()).stream()
                    .map(Dept::toApi)
                    .collect(Collectors.toList());
        }

        for(DeptDto deptDto : hList){
            deptDto.setDepts(this.setSubDepts(allList, deptDto));
        }

        return hList;
    }

    public List<DeptDto> setSubDepts(List<Dept> allList, DeptDto deptDto){
        List<DeptDto> subList = new ArrayList<>();

        subList.addAll(allList.stream()
                                .filter(m->deptDto.getDeptcd().equals(m.getSuperDeptcd()))
                                .collect(Collectors.toList()).stream()
                                .map(Dept::toApi)
                                .collect(Collectors.toList()));

        deptDto.setDepts(subList);
        for(DeptDto superDeptDto : subList){
           this.setSubDepts(allList, superDeptDto);
        }

        return subList;
    }

    @Override
    public DeptDto searchDeptcd(String deptcd)
    {
        Dept dept = deptRepository.findByDeptcd(deptcd);
        return dept.toApi();
    }

    @Override
    public List<Bssmacd> searchUseBssmacd() {
        return searchBssmacd("Y");
    }

    public List<Bssmacd> searchBssmacd(String useYn){
        return StringUtils.isEmpty(useYn) ? bssmacdRepository.findAll() :  bssmacdRepository.findByUseYn(useYn);
    }

    @Override
    public Page<DeptDto> searchDeptPage(String bssmacd, String deptNm, String useYn, Pageable pageable) {

        if(bssmacd == null) bssmacd = "";
        if(deptNm == null) deptNm = "";
        if(useYn == null) useYn = "";

        Page<Dept> list = deptRepository.searchDeptPage(bssmacd, deptNm, useYn, pageable);
        List<Bssmacd> bssList = bssmacdRepository.findByUseYn("Y");
        List<DeptDto> dtoList = list.stream()
                                    .map(d -> new DeptDto(d, bssList.stream()
                                                                    .anyMatch(i -> i.getBssmacd().equals(d.getBssmacd())) ? bssList.stream()
                                                                                                        .filter(i -> i.getBssmacd().equals(d.getBssmacd()))
                                                                                                        .findFirst().get().getBssHqNm() : ""
                                    )).collect(Collectors.toList());

        return new PageImpl<>(dtoList, list.getPageable(), list.getTotalElements());
    }

}