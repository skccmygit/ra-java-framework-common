package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserRoleDeptMappingIDto;
import com.skcc.ra.account.domain.baseAuth.UserRoleDept;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserRoleDeptRepository  extends JpaRepository<UserRoleDept, UserRoleDeptPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value="SELECT A.ROLE_DEPT_TEAM_CL_CD AS roleDeptTeamClCd,                        \n" +
            "            '부서' AS roleDeptTeamClNm,                                         \n" +
            "            A.ROLE_DEPT_TEAM_CD AS roleDeptTeamCd,                             \n" +
            "            C.DEPT_NM AS roleDeptTeamNm,                                       \n" +
            "            A.ROLE_MAPP_REOFO_CD AS roleMappReofoCd,                           \n" +
            "            E.CMMN_CD_VAL_NM AS roleMappReofoNm,                               \n" +
            "            A.ACCNT_CRAT_AUTO_YN AS accntCratAutoYn,                           \n" +
            "            A.USE_YN AS useYn                                                  \n" +
            "       FROM OCO.OCO10111 A                                                     \n" +
            "       LEFT OUTER JOIN OCO.OCO50200 C ON (A.ROLE_DEPT_TEAM_CD = C.DEPTCD)      \n" +
            "       LEFT OUTER JOIN OCO.OCO20101 E ON (E.CMMN_CD = 'REOFO_CD' AND E.CMMN_CD_VAL = A.ROLE_MAPP_REOFO_CD) \n" +
            "      WHERE 1=1                         \n" +
            "        AND (C.DEPT_NM LIKE CONCAT('%',:dept,'%')) ", nativeQuery = true)
    List<UserRoleDeptMappingIDto> findMappedDept(@Param("dept") String dept);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value="SELECT new com.skcc.ra.account.api.dto.domainDto.RoleDto(t, CASE WHEN r.userRoleId IS NULL THEN 'N' ELSE 'Y' END) " +
                 "FROM Role t " +
                 "LEFT OUTER JOIN UserRoleDeptMapping r on (r.userRoleId = t.userRoleId " +
                 "                                          AND r.roleDeptTeamCd = :roleDeptTeamCd " +
                 "                                          AND r.roleMappReofoCd = :roleMappReofoCd) " +
                 "WHERE t.useYn = 'Y' " )
    List<RoleDto> findMappedRole(@Param("roleDeptTeamCd") String roleDeptTeamCd, @Param("roleMappReofoCd") String roleMappReofoCd);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT A.ROLE_DEPT_TEAM_CL_CD AS roleDeptTeamClCd,                                                  \n" +
            "      '부서' AS roleDeptTeamClNm,         \n" +
            "       A.ROLE_DEPT_TEAM_CD AS roleDeptTeamCd,                                                              \n" +
            "       C.DEPT_NM AS roleDeptTeamNm, \n" +
            "       A.ROLE_MAPP_REOFO_CD AS roleMappReofoCd,                                                            \n" +
            "       E.CMMN_CD_VAL_NM AS roleMappReofoNm,                                                                \n" +
            "       B.USER_ROLE_ID AS userRoleId,                                                                       \n" +
            "       F.USER_ROLE_NM AS userRoleNm,                                                                       \n" +
            "       A.ACCNT_CRAT_AUTO_YN AS accntCratAutoYn,                                                            \n" +
            "       A.USE_YN AS useYn,                                                                                   \n" +
            "       'Y' AS authYn                                                                                       \n" +
            "  FROM OCO.OCO10111 A                                                                                      \n" +
            " INNER JOIN OCO.OCO10113 B ON (A.ROLE_DEPT_TEAM_CD = B.ROLE_DEPT_TEAM_CD AND A.ROLE_MAPP_REOFO_CD = B.ROLE_MAPP_REOFO_CD)                                  \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 C ON (A.ROLE_DEPT_TEAM_CD = C.DEPTCD)                                       \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 E ON (E.CMMN_CD = 'REOFO_CD' AND E.CMMN_CD_VAL = A.ROLE_MAPP_REOFO_CD) \n" +
            "  LEFT OUTER JOIN OCO.OCO10110 F ON (B.USER_ROLE_ID = F.USER_ROLE_ID) \n" +
            " WHERE 1=1 \n" +
            "   AND A.ROLE_DEPT_TEAM_CL_CD = :roleDeptTeamClCd ", nativeQuery = true)
    List<UserRoleDeptMappingIDto> findMappedDeptRole(@Param("roleDeptTeamClCd")String roleDeptTeamClCd);

}
