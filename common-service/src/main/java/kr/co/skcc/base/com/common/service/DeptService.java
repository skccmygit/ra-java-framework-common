package kr.co.skcc.oss.com.common.service;

import kr.co.skcc.oss.com.common.api.dto.domainDto.DeptDto;
import kr.co.skcc.oss.com.common.domain.dept.Bssmacd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeptService {

    List<Bssmacd> searchUseBssmacd();

    List<DeptDto> searchDeptList(String superDeptcd);

    DeptDto searchDeptcd(String deptcd);

    Page<DeptDto> searchDeptPage(String bssmacd, String deptNm, String useYn, Pageable pageable);

}
