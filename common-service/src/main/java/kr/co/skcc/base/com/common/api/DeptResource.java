package kr.co.skcc.base.com.common.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.base.com.common.api.dto.domainDto.DeptDto;
import kr.co.skcc.base.com.common.domain.dept.Bssmacd;
import kr.co.skcc.base.com.common.service.DeptService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[기타] 부서 조회(DeptResource)", description = "외부에서 연동받은 부서 정보 조회를 위한 API")
@RestController
@RequestMapping("/v1/com/common/dept")
@Slf4j
public class DeptResource {
    @Autowired
    private DeptService deptService;

    @Operation(summary = "부서 리스트 조회(tree) - 상위코드 기준")
    @GetMapping
    public ResponseEntity<List<DeptDto>> searchDeptList(@RequestParam(required = false) String superDeptcd) {
        return new ResponseEntity<>(deptService.searchDeptList(superDeptcd), HttpStatus.OK);
    }
    
    @Operation(summary = "본부 조회 - 사용 중인 본부")
    @GetMapping("/bssmacd/use")
    public ResponseEntity<List<Bssmacd>> searchUseBssmacd() {
        return new ResponseEntity<>(deptService.searchUseBssmacd(), HttpStatus.OK);
    }

    @Operation(summary = "부서 조회 - 조건별")
    @GetMapping("/search")
    public ResponseEntity<Page<DeptDto>> searchDeptPage(@RequestParam(required = false) String bssmacd,
                                                        @RequestParam(required = false) String deptNm,
                                                        @RequestParam(required = false) String useYn,
                                                        Pageable pageable) {
        return new ResponseEntity<>(deptService.searchDeptPage(bssmacd, deptNm, useYn, pageable), HttpStatus.OK);
    }

    @Operation(summary = "부서 조회 - 부서 코드 기준")
    @GetMapping("/deptcd")
    public ResponseEntity<DeptDto> searchDeptcd(@RequestParam String deptcd) {
        return new ResponseEntity<>(deptService.searchDeptcd(deptcd), HttpStatus.OK);
    }

}
