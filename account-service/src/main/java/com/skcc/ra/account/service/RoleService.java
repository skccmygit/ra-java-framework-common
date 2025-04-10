package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.api.dto.requestDto.UserDeptRoleDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleMappedUserIDto;
import com.skcc.ra.account.domain.auth.Role;
import com.skcc.ra.account.domain.auth.RoleMenu;
import com.skcc.ra.account.domain.auth.RoleScrenBttn;
import com.skcc.ra.account.domain.baseAuth.UserRoleDept;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.List;

public interface RoleService {

    RoleDto findRole(String roleId);

    Page<RoleDto> findRoleList(String findRole, Pageable pageable);

    RoleDto createRole(Role role);

    RoleDto modifyRole(Role role);

    void createRoleUserList(List<UserRoleDto> userRoleDtoList) throws UnknownHostException;

    List<RoleMappedUserIDto> findRoleUser(String userRoleId, List<String> deptcdList, String userNm);

    List<MenuIDto> findRoleMenu(String userRoleId);

    RoleMenu createRoleMenu(RoleMenu roleMenu);

    void deleteRoleMenu(RoleMenu roleMenu);

    void modifyRoleMenu(List<RoleMenuDto> roleMenuDtoList) throws UnknownHostException;

    List<ScrenDto> findRoleScren(String userRoleId, String chrgTaskGroupCd, String screnClCd, String screnNm);

    List<BttnDto> findRoleBttn(String userRoleId, String screnId);

    RoleScrenBttn createRoleScrenBttn(RoleScrenBttn roleScrenBttn);

    void modifyRoleScrenBttn(List<RoleScrenBttnDto> roleScrenBttnDtoList) throws UnknownHostException;

    RoleScrenBttn deleteRoleScrenBttn(RoleScrenBttn roleScrenBttn);

    List<RoleDto> findUserRole(String userid);

    List<RoleDto> findMenuRole(String menuId);

    List<RoleDto> findScrenRole(String screnId);

    /*********************************************/
    List<UserRoleDeptDto> findMappedDept(String deptOrTeamNm);

    List<RoleDto> findMappedRole(String roleDeptTeamCd, String roleMappReofoCd);

//    void modifyRoleDeptList(UserRoleDeptDto userRoleDeptDto, List<UserRoleDeptMappingDto> userRoleDeptMappingDtoList) throws UnknownHostException;

    void modifyRoleDeptList(List<UserDeptRoleDto> userDeptRoleDto) throws UnknownHostException;

    /*********************************************/


    /*********************************************/
    List<RoleDto> searchRoleAuthByUserid(String userid);

    List<RoleDto> searchReqRoleAuthByAthrtyReqstSeq( Integer athrtyReqstSeq);

    void createUserRoleDept(List<UserRoleDept> userRoleDeptList);

    void roleMappingFormatExcelDownload() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    void roleMappingDataExcelDownload(String roleDeptTeamClCd) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    List<UserRoleDeptMappingDto> uploadDeptAndRole(MultipartFile file) throws IOException;

    void saveDeptAndRole(List<UserRoleDeptMappingDto> userRoleDeptMappingDto) throws UnknownHostException;

    String checkPriAuth(String clientIP, String screnId, String dwnldResonCntnt) throws UnknownHostException;

//    void saveAuthToRedis(String batchName);
//
//    void callBatch(JobDto JobData);

}
