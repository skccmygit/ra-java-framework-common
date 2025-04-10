package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
import com.skcc.ra.account.domain.userSpecificMenu.ShortcutMenu;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface ShortcutMenuRepository extends JpaRepository<ShortcutMenu, UserMenuPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT T.MENU_ID AS menuId                      \n" +
            "            ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd           \n" +
            "            ,T.MENU_TYPE_CD AS menuTypeCd                      \n" +
            "            ,T.MENU_NM AS menuNm                               \n" +
            "            ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd             \n" +
            "            ,T.MENU_DESC AS menuDesc                           \n" +
            "            ,T.MENU_STEP_VAL AS menuStepVal                    \n" +
            "            ,T.SORT_SEQN AS sortSeqn                           \n" +
            "            ,T.USE_YN AS useYn                                 \n" +
            "            ,T.MENU_EXPSE_YN AS menuExpseYn                    \n" +
            "            ,T.SUPER_MENU_ID AS superMenuId                    \n" +
            "            ,D.MENU_NM AS superMenuNm                          \n" +
            "            ,B.SCREN_ID AS screnId                             \n" +
            "            ,B.SCREN_NM AS screnNm                             \n" +
            "            ,B.SCREN_DESC AS screnDesc                         \n" +
            "            ,B.SCREN_URLADDR AS screnUrladdr                   \n" +
            "            ,B.SCREN_CL_CD AS screnClCd                        \n" +
            "            ,B.SCREN_SIZE_CL_CD AS screnSizeClCd               \n" +
            "            ,B.SCREN_WIDTH_SIZE AS screnWidthSize              \n" +
            "            ,B.SCREN_VRTLN_SIZE AS screnVrtlnSize              \n" +
            "            ,B.SCREN_START_TOP_CODN AS screnStartTopCodn       \n" +
            "            ,B.SCREN_START_LEFT_CODN AS screnStartLeftCodn     \n" +
            "            ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt     \n" +
            "            ,C.CMMN_CD_VAL_NM AS chrgTaskGroupNm               \n" +
            "            ,A.SORT_SEQN as shortcutSortSeqn                   \n" +
            "        FROM OCO.OCO10210 T                                    \n" +
            "  INNER JOIN OCO.OCO10105 A ON (T.MENU_ID = A.MENU_ID)         \n" +
            "  LEFT OUTER JOIN OCO.OCO10220 B ON (T.SCREN_ID = B.SCREN_ID)  \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND C.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD)    \n" +
            "  LEFT OUTER JOIN OCO.OCO10210 D ON (D.MENU_ID = T.SUPER_MENU_ID)  \n" +
            "       WHERE T.USE_YN ='Y'             \n" +
            "         AND T.MENU_TYPE_CD = '1'      \n" +
            "         AND T.SCREN_ID IS NOT NULL    \n" +
            "         AND A.USERID = :userid        \n" +
            "       ORDER BY A.SORT_SEQN"
            ,nativeQuery = true)
    List<MenuIDto> findShortcutMenu(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Modifying
    @Query(value = "DELETE FROM ShortcutMenu t WHERE t.userid = :userid ")
    void deleteByUserid(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Modifying
    @Query(value = "DELETE FROM ShortcutMenu t WHERE t.userid = :userid \n" +
                    "   AND t.menuId NOT IN (SELECT k.menuId FROM RoleMenu k\n" +
                    "                          WHERE k.userRoleId IN (SELECT l.userRoleId FROM UserRole l\n" +
                    "                                                  WHERE l.userid = :userid ))\n")
    void deleteByRoleDeptTeamCdAndRoleMappReofoCd(String userid);
}
