package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.domainDto.DeptDto;
import com.skcc.ra.common.domain.dept.Bssmacd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeptService {

    List<Bssmacd> searchUseBssmacd();

    List<DeptDto> searchDeptList(String superDeptcd);

    DeptDto searchDeptcd(String deptcd);

    Page<DeptDto> searchDeptPage(String bssmacd, String deptNm, String useYn, Pageable pageable);

}
