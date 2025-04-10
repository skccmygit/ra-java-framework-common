package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.domain.auth.RoleMenu;
import com.skcc.ra.account.domain.auth.pk.RoleMenuPK;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.domain.menu.Scren;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, RoleMenuPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT T.MENU_ID AS menuId                       \n" +
                  ",T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd         \n" +
                  ",T.MENU_TYPE_CD AS menuTypeCd                    \n" +
                  ",T.MENU_NM AS menuNm                             \n" +
                  ",T.SCREN_EXCUT_CL_CD AS screnExcutClCd           \n" +
                  ",T.MENU_DESC AS menuDesc                         \n" +
                  ",T.MENU_STEP_VAL AS menuStepVal                  \n" +
                  ",T.SORT_SEQN AS sortSeqn                         \n" +
                  ",T.USE_YN AS useYn                               \n" +
                  ",T.MENU_EXPSE_YN AS menuExpseYn                  \n" +
                  ",T.SUPER_MENU_ID AS superMenuId                  \n" +
                  ",E.MENU_NM AS superMenuNm                        \n" +
                  ",B.SCREN_ID AS screnId                           \n" +
                  ",B.SCREN_NM AS screnNm                           \n" +
                  ",B.SCREN_DESC AS screnDesc                       \n" +
                  ",B.SCREN_URLADDR AS screnUrladdr                 \n" +
                  ",B.SCREN_CL_CD AS screnClCd                      \n" +
                  ",B.SCREN_SIZE_CL_CD AS screnSizeClCd             \n" +
                  ",B.SCREN_WIDTH_SIZE AS screnWidthSize            \n" +
                  ",B.SCREN_VRTLN_SIZE AS screnVrtlnSize            \n" +
                  ",B.SCREN_START_TOP_CODN AS screnStartTopCodn     \n" +
                  ",B.SCREN_START_LEFT_CODN AS screnStartLeftCodn   \n" +
                  ",T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt   \n" +
                  ",D.CMMN_CD_VAL_NM AS chrgTaskGroupNm             \n" +
                  ",(CASE WHEN C.MENU_ID IS NULL THEN 'N' ELSE 'Y' END) AS bookmarkYN   \n" +
                  "  FROM OCO.OCO10210 T                                            \n" +
                  " INNER JOIN OCO.OCO10120 A ON (A.MENU_ID = T.MENU_ID AND A.USER_ROLE_ID IN :roles)   \n" +
                  " LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = T.SCREN_ID)                         \n" +
                  " LEFT OUTER JOIN OCO.OCO10104 C ON (C.MENU_ID = T.MENU_ID AND C.USERID = :userid)    \n" +
                  " LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND D.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD) \n" +
                  " LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = T.SUPER_MENU_ID)                     \n" +
                  "WHERE T.USE_YN = 'Y'                 \n" +
                  "  AND T.MENU_EXPSE_YN ='Y'           \n" +
                  "  AND T.MENU_TYPE_CD = '1'", nativeQuery = true)
    List<MenuIDto> findUserMenus(@Param("roles") List<String> roles, @Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.MenuDto(t, l, n.cmmnCdValNm, o.menuNm, CASE WHEN m.menuId IS NULL THEN 'N' ELSE 'Y' END, 'bookmark') " +
            "FROM Menu t " +
            "INNER JOIN RoleMenu k ON (k.menuId = t.menuId) " +
            "LEFT OUTER JOIN Scren l ON (l.useYn = 'Y'  AND t.screnId = l.screnId) " +
            "LEFT OUTER JOIN BookmarkMenu m ON (m.userid = :userid AND m.menuId = t.menuId) " +
            "LEFT OUTER JOIN CmmnCdDtl n ON (n.cmmnCd = 'CHRG_TASK_GROUP_CD' AND t.chrgTaskGroupCd = n.cmmnCdVal) " +
            "LEFT OUTER JOIN Menu o ON o.menuId = t.superMenuId " +
            "WHERE k.userRoleId IN :roles " +
            "  AND t.useYn = 'Y' " +
            "  AND t.menuId = :menuId " +
            "  AND t.menuTypeCd = '1' ")
    MenuDto findUserMenusByMenuId(@Param("roles") List<String> roles, @Param("userid") String userid, @Param("menuId") String menuId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT T.MENU_ID AS menuId                       \n" +
                  "      ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd         \n" +
                  "      ,T.MENU_TYPE_CD AS menuTypeCd                    \n" +
                  "      ,T.MENU_NM AS menuNm                             \n" +
                  "      ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd           \n" +
                  "      ,T.MENU_DESC AS menuDesc                         \n" +
                  "      ,T.MENU_STEP_VAL AS menuStepVal                  \n" +
                  "      ,T.SORT_SEQN AS sortSeqn                         \n" +
                  "      ,T.USE_YN AS useYn                               \n" +
                  "      ,T.MENU_EXPSE_YN AS menuExpseYn                  \n" +
                  "      ,T.SUPER_MENU_ID AS superMenuId                  \n" +
                  "      ,E.MENU_NM AS superMenuNm                        \n" +
                  "      ,B.SCREN_ID AS screnId                           \n" +
                  "      ,B.SCREN_NM AS screnNm                           \n" +
                  "      ,B.SCREN_DESC AS screnDesc                       \n" +
                  "      ,B.SCREN_URLADDR AS screnUrladdr                 \n" +
                  "      ,B.SCREN_CL_CD AS screnClCd                      \n" +
                  "      ,B.SCREN_SIZE_CL_CD AS screnSizeClCd             \n" +
                  "      ,B.SCREN_WIDTH_SIZE AS screnWidthSize            \n" +
                  "      ,B.SCREN_VRTLN_SIZE AS screnVrtlnSize            \n" +
                  "      ,B.SCREN_START_TOP_CODN AS screnStartTopCodn     \n" +
                  "      ,B.SCREN_START_LEFT_CODN AS screnStartLeftCodn   \n" +
                  "      ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt   \n" +
                  "      ,D.CMMN_CD_VAL_NM AS chrgTaskGroupNm             \n" +
                  "      ,'' AS bookmarkYN   \n" +
                  "      ,(CASE WHEN A.MENU_ID IS NULL THEN 'N' ELSE 'Y' END) AS authYn   \n" +
                  "  FROM OCO.OCO10210 T                                            \n" +
                  " LEFT OUTER JOIN OCO.OCO10120 A ON (A.MENU_ID = T.MENU_ID AND A.USER_ROLE_ID = :userRoleId)   \n" +
                  " LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = T.SCREN_ID)                         \n" +
                  " LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND D.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD) \n" +
                  " LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = T.SUPER_MENU_ID)                     \n" +
                  "WHERE T.USE_YN = 'Y'                 \n" +
                  "  AND T.MENU_TYPE_CD = '1'", nativeQuery = true)
    List<MenuIDto> findByUserRoleId(String userRoleId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.account.api.dto.domainDto.RoleDto(t, CASE WHEN k.userRoleId IS NULL THEN 'N' ELSE 'Y' END) " +
                "FROM Role t " +
                "LEFT OUTER JOIN RoleMenu k ON (t.userRoleId = k.userRoleId AND k.menuId = :menuId) " +
                "WHERE t.useYn = 'Y' ")
    List<RoleDto> findByMenuId(String menuId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.account.api.dto.domainDto.RoleDto(t, CASE WHEN k.userRoleId IS NULL THEN 'N' ELSE 'Y' END) " +
            "FROM Role t " +
            "LEFT OUTER JOIN RoleScrenBttn k ON (t.userRoleId = k.userRoleId AND k.screnId = :screnId) " +
            "WHERE t.useYn = 'Y' " )
    List<RoleDto> findByScrenId(String screnId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT k " +
            "FROM Menu t " +
            "INNER JOIN Scren k ON (t.screnId = k.screnId) " +
            "WHERE t.menuId = :menuId" +
            "  AND t.useYn = 'Y' " +
            "  AND k.useYn = 'Y' ")
    Scren findScrenByMenuId(String menuId);

}
