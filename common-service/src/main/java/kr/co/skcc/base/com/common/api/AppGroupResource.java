package kr.co.skcc.base.com.common.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import kr.co.skcc.base.com.common.api.dto.domainDto.AppGroupDto;
import kr.co.skcc.base.com.common.service.AppGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[API관리] App 그룹 기본 정보 관리(AppGroupResource)", description = "Application 업무 그룹에 대한 기본 정보를 관리")
@RestController
@RequestMapping("/v1/com/common/appGroups")
@Transactional
public class AppGroupResource {
    @Autowired
    private AppGroupService appGroupService;

    @Operation(summary = "Application 그룹 리스트 조회")
    @GetMapping
    public ResponseEntity<List<AppGroupDto>> queryAppGroupSearch(@RequestParam(required = false) String aproTaskClCd,
                                                                 @RequestParam(required = false) String aproTypeClCd) {
        return new ResponseEntity<>(appGroupService.queryAppGroupSearch(aproTaskClCd, aproTypeClCd), HttpStatus.OK);
    }

    
    @Operation(summary = "Application 그룹 기본 정보 생성")
    @PostMapping
    public ResponseEntity<HttpStatus> createAppGroups(@RequestBody List<AppGroupDto> appGroupDtos) {
        appGroupService.createAppGroups(appGroupDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "Application 그룹 기본 정보 수정")
    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody List<AppGroupDto> appGroupDtos) {
        appGroupService.update(appGroupDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Application 그룹 기본 정보 삭제")
    @DeleteMapping
    public ResponseEntity<HttpStatus> remove(@RequestBody List<Integer> aproGroupIds) {
        appGroupService.remove(aproGroupIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Application 그룹 기본 정보 생성/수정 - 존재여부 기준")
    @PutMapping("/multi")
    public ResponseEntity<HttpStatus> multiAppGroups(@RequestBody List<AppGroupDto> appGroupDtos) {
        appGroupService.multiAppGroups(appGroupDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
