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
import kr.co.skcc.base.com.common.api.dto.domainDto.UserBasicDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.InnerCallInfoDto;
import kr.co.skcc.base.com.common.service.UserBasicService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[기타] 사원 조회 (UserBasicResource)", description = "외부에서 연동받은 사원 정보 조회를 위한 API")
@RestController
@RequestMapping("/v1/com/common/userBasic")
@Slf4j
public class UserBasicResource {

    @Autowired
    private UserBasicService userBasicService;

    @Operation(summary = "사원 조회 - 조건별")
    @GetMapping("/search")
    public ResponseEntity<Page<UserBasicDto>> searchUserBasicPage(@RequestParam(required = false) String bssmacd,
                                                                  @RequestParam(required = false) String deptcd,
                                                                  @RequestParam(required = false) String reofoCd,
                                                                  @RequestParam(required = false) String clofpNm,
                                                                  @RequestParam(required = false) String vctnNm,
                                                                  @RequestParam(required = false) String empno,
                                                                  @RequestParam(required = false) String deptNm,
                                                                  @RequestParam(required = false) List<String> deptcdList,
                                                                  Pageable pageable) {
        return new ResponseEntity<>(userBasicService.searchUserBasicPage(bssmacd, deptcd, reofoCd, clofpNm, vctnNm, empno, deptNm, deptcdList, pageable), HttpStatus.OK);
    }

    
    @Operation(summary = "사원 조회 - 사번, 사원명 기준 다건(개인정보 마스킹)")
    @GetMapping("/list")
    public ResponseEntity<List<UserBasicDto>> searchUserList(@RequestParam(required = false) List<String> empnoList,
                                                             @RequestParam(required = false) List<String> empNmList,
                                                             @RequestParam(required = false) List<String> deptcdList,
                                                             @RequestParam(required = false) String empNm,
                                                             @RequestParam(required = false) String deptNm) {
        return new ResponseEntity<>(userBasicService.searchUserList(empnoList, empNmList, deptcdList, empNm, deptNm), HttpStatus.OK);
    }

    
    @Operation(summary = "사원 조회 - 사번, 사원명 기준 다건(개인정보 마스킹 해제)")
    @GetMapping("/list/noMasking")
    public ResponseEntity<List<UserBasicDto>> searchUserListNoMasking(@RequestParam(required = false) List<String> empnoList,
                                                                      @RequestParam(required = false) List<String> empNmList,
                                                                      @RequestParam(required = false) List<String> deptcdList,
                                                                      @RequestParam(required = false) String empNm,
                                                                      @RequestParam(required = false) String deptNm) {
        return new ResponseEntity<>(userBasicService.searchUserListNoMasking(empnoList, empNmList, deptcdList, empNm, deptNm), HttpStatus.OK);
    }

    
    @Operation(summary = "사원 전화번호 조회 - 단건")
    @GetMapping("/mphno")
    public ResponseEntity<String> searchUserPhone(@RequestParam String empno) {
        return new ResponseEntity<>(userBasicService.searchUserPhone(empno), HttpStatus.OK);
    }

    
    @Operation(summary = "사원 조회 - 전화번호 기준")
    @GetMapping("/telno")
    public ResponseEntity<List<InnerCallInfoDto>> searchUserByTelno(@RequestParam String telno) {
        return new ResponseEntity<>(userBasicService.searchUserByTelno(telno), HttpStatus.OK);
    }

    
    @Operation(summary = "사원 조회 - 사번, 부서코드, 사원명 기준")
    @GetMapping
    public ResponseEntity<List<UserBasicDto>> searchUserBasic(@RequestParam(required = false) String userNm,
                                                              @RequestParam(required = false) List<String> deptcdList,
                                                              @RequestParam(required = false) String empno){
        return new ResponseEntity<>(userBasicService.searchUserBasic(userNm, deptcdList, empno), HttpStatus.OK);
    }

    
    @Operation(summary = "부서장 조회 - 부서코드, 사번 기준")
    @GetMapping("/teamLeader")
    public ResponseEntity<UserBasicDto> searchTeamLeader(@RequestParam(required = false) String deptcd,
                                                         @RequestParam(required = false) String empno){
        return new ResponseEntity<>(userBasicService.searchTeamLeader(deptcd, empno), HttpStatus.OK);
    }
}
