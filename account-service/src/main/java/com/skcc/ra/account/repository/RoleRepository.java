package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleHistDto;
import com.skcc.ra.account.domain.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT  t " +
                    "FROM Role t " +
                    "WHERE t.userRoleNm LIKE CONCAT('%',:roleNm,'%') ")
    Page<RoleDto> findRoleList(@Param("roleNm") String roleNm, Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<Role> findByUserRoleId(String userRoleId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value =  "SELECT A.USER_ROLE_ID AS userRoleId            \n" +
                    "      ,E.USER_ROLE_NM AS userRoleNm           \n" +
                    "      ,B.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd \n" +
                    "      ,F.CMMN_CD_VAL_NM AS chrgTaskGroupNm     \n" +
                    "      ,C.MENU_NM AS superMenuNm          \n" +
                    "      ,B.MENU_NM AS menuNm                     \n" +
                    "      ,G.CMMN_CD_VAL_NM AS crudClCd            \n" +
                    "      ,A.LAST_CHNGR_ID AS lastChngrId          \n" +
                    "      ,H.USER_NM AS lastChngrNm                \n" +
                    "      ,DATE_FORMAT(STR_TO_DATE(A.CHNG_DTM,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%i:%s') AS chngrDtm  \n" +
                    "      ,A.CHNG_USER_IPADDR AS ipAddr            \n" +
                    "      ,A.ATHRTY_REQST_SEQ AS athrtyReqstSeq    \n" +
                    "      ,A.CHNG_RESON_CNTNT AS chngResonCntnt    \n" +
                    "      ,D.GVBK_RESON_CNTNT AS apprvResonCntnt       \n" +
                    "  FROM OCO.OCO10122 A              \n" +
                    "  LEFT OUTER JOIN OCO.OCO10210 B ON (B.MENU_ID = A.MENU_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10210 C ON (C.MENU_ID = B.SUPER_MENU_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10131 D ON (D.ATHRTY_REQST_SEQ = A.ATHRTY_REQST_SEQ AND D.ATHRTY_REQST_STS_CD = 'A') \n" +
                    "  LEFT OUTER JOIN OCO.OCO10110 E ON (E.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO20101 F ON (F.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND F.CMMN_CD_VAL = B.CHRG_TASK_GROUP_CD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO20101 G ON (G.CMMN_CD = 'CRUD_CL_CD' AND G.CMMN_CD_VAL = A.CRUD_CL_CD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID) \n" +
                    " WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
                    "   AND (E.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR E.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
                    "   AND B.CHRG_TASK_GROUP_CD LIKE CONCAT('%', :chrgTaskGroupCd ,'%') \n" +
                    "   AND (H.USERID LIKE CONCAT('%',:chngrNm ,'%') OR H.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n" +
                    "   AND B.MENU_NM LIKE CONCAT('%',:menuNm ,'%') \n" +
                    " ORDER BY A.CHNG_DTM DESC, A.USER_ROLE_ID, B.CHRG_TASK_GROUP_CD  ",
       countQuery = "SELECT COUNT(*) \n" +
               "  FROM OCO.OCO10122 A              \n" +
               "  LEFT OUTER JOIN OCO.OCO10210 B ON (B.MENU_ID = A.MENU_ID) \n" +
               "  LEFT OUTER JOIN OCO.OCO10110 E ON (E.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
               "  LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID) \n" +
               " WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
               "   AND (E.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR E.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
               "   AND B.CHRG_TASK_GROUP_CD LIKE CONCAT('%', :chrgTaskGroupCd ,'%') \n" +
               "   AND (H.USERID LIKE CONCAT('%',:chngrNm ,'%') OR H.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n" +
               "   AND B.MENU_NM LIKE CONCAT('%',:menuNm ,'%') \n",
       nativeQuery = true)
    Page<RoleHistDto> findMenuRoleHist(String userRoleNm, String chrgTaskGroupCd, String menuNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value =  "SELECT A.USER_ROLE_ID AS userRoleId\n" +
                    "      ,E.USER_ROLE_NM AS userRoleNm\n" +
                    "      ,B.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd \n" +
                    "      ,F.CMMN_CD_VAL_NM AS chrgTaskGroupNm \n" +
                    "      ,B.SCREN_NM AS screnNm\n" +
                    "      ,C.BTTN_NM AS bttnNm\n" +
                    "      ,G.CMMN_CD_VAL_NM AS crudClCd\n" +
                    "      ,A.LAST_CHNGR_ID AS lastChngrId \n" +
                    "      ,H.USER_NM AS lastChngrNm\n" +
                    "      ,DATE_FORMAT(STR_TO_DATE(A.CHNG_DTM,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%i:%s') AS chngrDtm \n" +
                    "      ,A.CHNG_USER_IPADDR AS ipAddr \n" +
                    "      ,A.ATHRTY_REQST_SEQ AS athrtyReqstSeq    \n" +
                    "      ,A.CHNG_RESON_CNTNT AS chngResonCntnt    \n" +
                    "      ,D.GVBK_RESON_CNTNT AS apprvResonCntnt   \n" +
                    "  FROM OCO.OCO10123 A          \n" +
                    "  LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = A.SCREN_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10230 C ON (C.SCREN_ID = A.SCREN_ID AND C.BTTN_ID = A.BTTN_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10131 D ON (D.ATHRTY_REQST_SEQ = A.ATHRTY_REQST_SEQ AND D.ATHRTY_REQST_STS_CD = 'A')       \n" +
                    "  LEFT OUTER JOIN OCO.OCO10110 E ON (E.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO20101 F ON (F.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND F.CMMN_CD_VAL = B.CHRG_TASK_GROUP_CD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO20101 G ON (G.CMMN_CD = 'CRUD_CL_CD' AND G.CMMN_CD_VAL = A.CRUD_CL_CD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID) \n" +
                    " WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
                    "   AND (E.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR E.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
                    "   AND B.CHRG_TASK_GROUP_CD LIKE CONCAT('%', :chrgTaskGroupCd ,'%') \n" +
                    "   AND (H.USERID LIKE CONCAT('%',:chngrNm ,'%') OR H.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n" +
                    "   AND B.SCREN_NM LIKE CONCAT('%',:screnNm ,'%') \n" +
                    " ORDER BY A.CHNG_DTM DESC, A.USER_ROLE_ID, B.CHRG_TASK_GROUP_CD",
            countQuery = "SELECT COUNT(*) \n" +
                    "       FROM OCO.OCO10123 A          \n" +
                    "       LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = A.SCREN_ID) \n" +
                    "       LEFT OUTER JOIN OCO.OCO10110 E ON (E.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
                    "       LEFT OUTER JOIN OCO.OCO10100 H ON (H.USERID = A.LAST_CHNGR_ID) \n" +
                    "      WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
                    "        AND (E.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR E.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
                    "        AND B.CHRG_TASK_GROUP_CD LIKE CONCAT('%', :chrgTaskGroupCd ,'%') \n" +
                    "        AND (H.USERID LIKE CONCAT('%',:chngrNm ,'%') OR H.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n" +
                    "        AND B.SCREN_NM LIKE CONCAT('%',:screnNm ,'%') \n",
            nativeQuery = true)
    Page<RoleHistDto> findScrenRoleHist(String userRoleNm, String chrgTaskGroupCd, String screnNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value =  "SELECT A.USER_ROLE_ID AS userRoleId    \n" +
                    "      ,D.USER_ROLE_NM AS userRoleNm    \n" +
                    "      ,E.DEPT_NM AS deptNm             \n" +
                    "      ,A.USERID AS userid              \n" +
                    "      ,B.USER_NM AS userNm             \n" +
                    "      ,F.CMMN_CD_VAL_NM AS crudClCd    \n" +
                    "      ,A.LAST_CHNGR_ID AS lastChngrId  \n" +
                    "      ,IFNULL(G.USER_NM, A.LAST_CHNGR_ID) AS lastChngrNm \n" +
                    "      ,DATE_FORMAT(STR_TO_DATE(A.CHNG_DTM,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%i:%s') AS chngrDtm \n" +
                    "      ,A.CHNG_USER_IPADDR AS ipAddr          \n" +
                    "      ,A.ATHRTY_REQST_SEQ AS athrtyReqstSeq    \n" +
                    "      ,A.CHNG_RESON_CNTNT AS chngResonCntnt    \n" +
                    "      ,C.GVBK_RESON_CNTNT AS apprvResonCntnt   \n" +
                    "  FROM OCO.OCO10112 A      \n" +
                    "  LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10131 C ON (C.ATHRTY_REQST_SEQ = A.ATHRTY_REQST_SEQ AND C.ATHRTY_REQST_STS_CD = 'A')\n" +
                    "  LEFT OUTER JOIN OCO.OCO10110 D ON (D.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
                    "  LEFT OUTER JOIN OCO.OCO50200 E ON (E.DEPTCD = B.DEPTCD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO20101 F ON (F.CMMN_CD = 'CRUD_CL_CD' AND F.CMMN_CD_VAL = A.CRUD_CL_CD) \n" +
                    "  LEFT OUTER JOIN OCO.OCO10100 G ON (G.USERID = A.LAST_CHNGR_ID) \n" +
                    " WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
                    "   AND (B.USERID LIKE CONCAT('%',:userNm ,'%') OR B.USER_NM LIKE CONCAT('%',:userNm ,'%'))\n" +
                    "   AND (D.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR D.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
                    "   AND (G.LAST_CHNGR_ID LIKE CONCAT('%',:chngrNm ,'%') OR G.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n" +
                    " ORDER BY A.CHNG_DTM DESC, A.USER_ROLE_ID ",
            countQuery = "SELECT COUNT(*) \n" +
                    "       FROM OCO.OCO10112 A      \n" +
                    "       LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID) \n" +
                    "       LEFT OUTER JOIN OCO.OCO10110 D ON (D.USER_ROLE_ID = A.USER_ROLE_ID) \n" +
                    "       LEFT OUTER JOIN OCO.OCO10100 G ON (G.USERID = A.LAST_CHNGR_ID) \n" +
                    "      WHERE A.CHNG_DTM >= :startDt AND A.CHNG_DTM <= :endDt \n" +
                    "        AND (B.USERID LIKE CONCAT('%',:userNm ,'%') OR B.USER_NM LIKE CONCAT('%',:userNm ,'%'))\n" +
                    "        AND (D.USER_ROLE_ID LIKE CONCAT('%', :userRoleNm ,'%') OR D.USER_ROLE_NM LIKE CONCAT('%', :userRoleNm ,'%')) \n" +
                    "        AND (G.LAST_CHNGR_ID LIKE CONCAT('%',:chngrNm ,'%') OR G.USER_NM LIKE CONCAT('%',:chngrNm ,'%')) \n",
            nativeQuery = true)
    Page<RoleHistDto> findUserRoleHist(String userRoleNm, String userNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value = "SELECT  new com.skcc.ra.account.api.dto.domainDto.RoleDto(role, " +
                                                                    "(CASE WHEN (userRole = null) THEN 'N' ELSE 'Y' END)) " +
                     "FROM Role role  " +
                     "LEFT OUTER JOIN UserRole userRole ON role.userRoleId = userRole.userRoleId AND userRole.userid = :userid " +
                    "WHERE role.useYn = 'Y' ")
    List<RoleDto> searchRoleAuthByUserid(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value = "SELECT  new com.skcc.ra.account.api.dto.domainDto.RoleDto(role, userAuthReqHis.athrtyAddnYn) " +
                     "FROM Role role  " +
                    "RIGHT OUTER JOIN UserAuthReqHis userAuthReqHis ON role.userRoleId = userAuthReqHis.userRoleId " +
                                                          "AND userAuthReqHis.athrtyReqstSeq = :athrtyReqstSeq " +
                                                          "AND userAuthReqHis.userRoleId IS NOT NULL " +
                    "WHERE role.useYn = 'Y' ")
    List<RoleDto> searchReqRoleAuthByAthrtyReqstSeq( Integer athrtyReqstSeq);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value = "SELECT  new com.skcc.ra.account.api.dto.domainDto.RoleDto(role) " +
            "FROM Role role  " +
            "INNER JOIN UserRole userRole ON (userRole.userRoleId = role.userRoleId AND userRole.userid = :userid) " +
            " AND role.useYn = 'Y' ")
    List<RoleDto> findUserRole(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT t " +
                   "  FROM Role t  " +
                   " WHERE 1=1  " +
                   "   AND t.lockAthrtyClCd <> 'X' " +
                   "   AND t.useYn = 'Y' ")
    List<Role> findLockAthrtyRoleList();
}
