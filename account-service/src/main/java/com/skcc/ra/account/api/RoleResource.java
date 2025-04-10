package com.skcc.ra.account.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.api.dto.domainDto.RoleMenuDto;
import com.skcc.ra.account.api.dto.domainDto.RoleScrenBttnDto;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDeptDto;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDeptMappingDto;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDto;
import com.skcc.ra.account.api.dto.requestDto.UserDeptRoleDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleMappedUserIDto;
import com.skcc.ra.account.domain.baseAuth.UserRoleDept;
import com.skcc.ra.account.service.RoleService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[권한관리] 역할 관리(RoleResource)", description = "역할 및 역할별 매핑 권한에 대한 API")
@RestController
@RequestMapping("/v1/com/account/role")
@Slf4j
public class RoleResource {

    @Autowired
    private RoleService roleService;

    /***********************************************************************************************************************/

    @Operation(summary = "역할 조회 - 전체 역할")
    @GetMapping
    public ResponseEntity<Page<RoleDto>> findRole(@RequestParam(required = false) String roleNm,
                                                  Pageable pageable){
        return new ResponseEntity<>(roleService.findRoleList(roleNm, pageable), HttpStatus.OK);
    }

    @Operation(summary = "역할 수정")
    @PutMapping
    public  ResponseEntity<RoleDto> modifyRole(@RequestBody RoleDto roleDto){
        return new ResponseEntity<>(roleService.modifyRole(roleDto.toEntity()), HttpStatus.OK);
    }

    @Operation(summary = "역할 등록")
    @PostMapping
    public  ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto){
        return new ResponseEntity<>(roleService.createRole(roleDto.toEntity()), HttpStatus.OK);
    }

    @Operation(summary = "역할 조회 - 단건")
    @GetMapping("/userRoleId")
    public ResponseEntity<RoleDto> findRoleByUserRoleId(@RequestParam String userRoleId){
        return new ResponseEntity<>(roleService.findRole(userRoleId), HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "역할 기준 사용자 조회 - 역할 ID 기준 매핑된 사용자 조회 ")
    @GetMapping("/user")
    public ResponseEntity<List<RoleMappedUserIDto>> findRoleUser(@RequestParam String userRoleId,
                                                                 @RequestParam(required = false) List<String> deptcdList,
                                                                 @RequestParam(required = false) String userNm){
        List<RoleMappedUserIDto> result = roleService.findRoleUser(userRoleId, deptcdList, userNm);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "역할 기준 사용자 등록 - 역할 ID 기준 사용자 매핑")
    @PostMapping("/user")
    public ResponseEntity<HttpStatus> createRoleUser(@RequestBody List<UserRoleDto> userRoleDtoList) throws UnknownHostException {
        roleService.createRoleUserList(userRoleDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "역할 기준 메뉴 조회 - 역할 ID 기준 매핑된 메뉴 조회")
    @GetMapping("/menu")
    public ResponseEntity<List<MenuIDto>> findRoleMenu(@RequestParam String userRoleId){
        List<MenuIDto> result = roleService.findRoleMenu(userRoleId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 메뉴 수정 - 역할 ID를 기준으로 메뉴를 매핑")
    @PutMapping("/menu")
    public ResponseEntity modifyRoleMenu(@RequestBody List<RoleMenuDto> roleMenuDtoList) throws UnknownHostException {
        roleService.modifyRoleMenu(roleMenuDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "역할 기준 화면 조회 - 역할 ID 기준 - 역할과 화면은 매핑관계 X -> 전체 화면 조회 ")
    @GetMapping("/scren")
    public ResponseEntity<List<ScrenDto>> findRoleScren(@RequestParam String userRoleId,
                                                        @RequestParam(required = false) String chrgTaskGroupCd,
                                                        @RequestParam(required = false) String screnClCd,
                                                        @RequestParam(required = false) String screnNm){
        return new ResponseEntity<>(roleService.findRoleScren(userRoleId, chrgTaskGroupCd, screnClCd, screnNm), HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 버튼 조회 - 역할 ID 기준 매핑된 버튼 조회")
    @GetMapping("/bttn")
    public ResponseEntity<List<BttnDto>> findRoleBttn(@RequestParam String userRoleId,
                                                      @RequestParam String screnId){
        return new ResponseEntity<>(roleService.findRoleBttn(userRoleId, screnId), HttpStatus.OK);
    }

    @Operation(summary = "역할 기준 버튼 수정 - 역할 ID를 기준으로 버튼을 매핑")
    @PutMapping("/bttn")
    public ResponseEntity modifyRoleScrenBttn(@RequestBody List<RoleScrenBttnDto> roleScrenBttnDto) throws UnknownHostException {
        roleService.modifyRoleScrenBttn(roleScrenBttnDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "사용자 기준 역할 조회 - 사용자 ID 기준")
    @GetMapping("/user/userid")
    public ResponseEntity<List<RoleDto>> searchUserRole(@RequestParam(required = false) String userid){
        return new ResponseEntity<>(roleService.findUserRole(userid), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 기준 역할 조회 - 메뉴 ID 기준")
    @GetMapping("/menu/menuId")
    public ResponseEntity<List<RoleDto>> searchMenuRole(@RequestParam(required = false) String menuId){
        return new ResponseEntity<>(roleService.findMenuRole(menuId), HttpStatus.OK);
    }

    @Operation(summary = "화면 기준 역할 조회 - 화면 ID 기준 - 화면 내 버튼과 매핑된 역할 조회")
    @GetMapping("/scren/screnId")
    public ResponseEntity<List<RoleDto>> searchScrenRole(@RequestParam(required = false) String screnId){
        return new ResponseEntity<>(roleService.findScrenRole(screnId), HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "기본 매핑 대상 부서/팀 조회")
    @GetMapping("/dept")
    public ResponseEntity<List<UserRoleDeptDto>> searchMappedDept(@RequestParam(required = false) String deptOrTeamNm){
        return new ResponseEntity<>(roleService.findMappedDept(deptOrTeamNm), HttpStatus.OK);
    }
    
    @Operation(summary = "기본 매핑 대상 부서/팀 수정")
    @PostMapping("/dept")
    public ResponseEntity<Void> createUserRoleDept(@RequestBody List<UserRoleDept> userRoleDeptList){
        roleService.createUserRoleDept(userRoleDeptList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 조회 - 기본 역할 매핑")
    @GetMapping("/deptRole")
    public ResponseEntity<List<RoleDto>> searchMappedRole(@RequestParam String roleDeptTeamCd,
                                                          @RequestParam String roleMappReofoCd){
        return new ResponseEntity<>(roleService.findMappedRole(roleDeptTeamCd, roleMappReofoCd), HttpStatus.OK);
    }

    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 수정 - 기본 역할 매핑")
    @PostMapping("/deptRole")
    public ResponseEntity<Void> modifyMappedRole(@RequestBody List<UserDeptRoleDto> userDeptRoleDto) throws UnknownHostException {
        roleService.modifyRoleDeptList(userDeptRoleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 엑셀다운로드")
    @GetMapping("/deptRole/excel")
    public ResponseEntity<Void> roleMappingDataExcelDownload(@RequestParam String roleDeptTeamClCd) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        roleService.roleMappingDataExcelDownload(roleDeptTeamClCd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 일괄 업로드 양식 엑셀다운로드")
    @GetMapping("/deptRole/excel/template")
    public ResponseEntity<Void> roleMappingFormatExcelDownload() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        roleService.roleMappingFormatExcelDownload();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 일괄 엑셀 업로드")
    @PostMapping(path = "/deptRole/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<UserRoleDeptMappingDto>> uploadDeptAndRole(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(roleService.uploadDeptAndRole(file), HttpStatus.OK);
    }

    @Operation(summary = "부서/팀 기준 매핑된 역할 내역 일괄 엑셀 업로드 내용 적용")
    @PostMapping(path = "/deptRole/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> saveDeptAndRole(@RequestBody List<UserRoleDeptMappingDto> userRoleDeptMappingDto) throws UnknownHostException {
        roleService.saveDeptAndRole(userRoleDeptMappingDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***********************************************************************************************************************/
    
    @Operation(summary = "개인 정보 다운로드 권한 보유 여부 조회 - 개인 정보 다운로드 역할 보유 여부 + IP 체크")
    @GetMapping("/checkPriAuth")
    public ResponseEntity<String> checkPriAuth(@RequestParam String clientIp,
                                               @RequestParam String screnId,
                                               @RequestParam String dwnldResonCntnt) throws UnknownHostException {
        return new ResponseEntity<>(roleService.checkPriAuth(clientIp, screnId, dwnldResonCntnt), HttpStatus.OK);
    }
}
