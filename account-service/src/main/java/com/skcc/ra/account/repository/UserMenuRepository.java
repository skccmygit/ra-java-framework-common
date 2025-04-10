package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.auth.UserMenu;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserMenuRepository extends JpaRepository<UserMenu, UserMenuPK> {
    @QueryHints(@QueryHint(name = COMMENT, value = "공통, common, 나태관"))
    @Query(value = "SELECT t FROM UserMenu t " +
            "WHERE t.userid = :userid " +
            "AND t.menuId = :menuId")
    Optional<UserMenu> findByUseridAndMenuId(@Param("userid") String userid, @Param("menuId") String menuId);

    @QueryHints(@QueryHint(name = COMMENT, value = "공통, common, 나태관"))
    @Query(value = "SELECT new com.skcc.ra.common.api.dto.domainDto.MenuDto(t, l, n.cmmnCdValNm, o.menuNm, CASE WHEN (userAuthReqHis.menuId IS NULL OR userAuthReqHis.athrtyAddnYn = 'N') THEN 'N' ELSE 'Y' END, 'auth') " +
            "FROM Menu t " +
            "LEFT OUTER JOIN Scren l ON (l.useYn = 'Y' AND t.screnId = l.screnId) " +
            "LEFT OUTER JOIN CmmnCdDtl n ON (n.cmmnCd = 'CHRG_TASK_GROUP_CD' AND t.chrgTaskGroupCd = n.cmmnCdVal) " +
            "LEFT OUTER JOIN Menu o ON o.menuId = t.superMenuId " +
            "LEFT OUTER JOIN UserAuthReqHis userAuthReqHis ON userAuthReqHis.menuId = t.menuId " +
                                                         "And userAuthReqHis.athrtyReqstSeq = :athrtyReqstSeq " +
            "WHERE t.useYn = 'Y' AND t.menuId = :menuId ")
    MenuDto findSuperMenuId(@Param("athrtyReqstSeq") Integer athrtyReqstSeq, @Param("menuId") String menuId );

    @QueryHints(@QueryHint(name = COMMENT, value = "공통, common, 나태관"))
    @Query(value = "SELECT DISTINCT T.MENU_ID AS menuId                               \n" +
            "         ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd           \n" +
            "         ,T.MENU_TYPE_CD AS menuTypeCd                      \n" +
            "         ,T.MENU_NM AS menuNm                               \n" +
            "         ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd             \n" +
            "         ,T.MENU_DESC AS menuDesc                           \n" +
            "         ,T.MENU_STEP_VAL AS menuStepVal                    \n" +
            "         ,T.SORT_SEQN AS sortSeqn                           \n" +
            "         ,T.USE_YN AS useYn                                 \n" +
            "         ,T.MENU_EXPSE_YN AS menuExpseYn                    \n" +
            "         ,T.SUPER_MENU_ID AS superMenuId                    \n" +
            "         ,E.MENU_NM AS superMenuNm                          \n" +
            "         ,B.SCREN_ID AS screnId                             \n" +
            "         ,B.SCREN_NM AS screnNm                             \n" +
            "         ,B.SCREN_DESC AS screnDesc                         \n" +
            "         ,B.SCREN_URLADDR AS screnUrladdr                   \n" +
            "         ,B.SCREN_CL_CD AS screnClCd                        \n" +
            "         ,B.SCREN_SIZE_CL_CD AS screnSizeClCd               \n" +
            "         ,B.SCREN_WIDTH_SIZE AS screnWidthSize              \n" +
            "         ,B.SCREN_VRTLN_SIZE AS screnVrtlnSize              \n" +
            "         ,B.SCREN_START_TOP_CODN AS screnStartTopCodn       \n" +
            "         ,B.SCREN_START_LEFT_CODN AS screnStartLeftCodn     \n" +
            "         ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt     \n" +
            "         ,D.CMMN_CD_VAL_NM AS chrgTaskGroupNm               \n" +
            "         ,(CASE WHEN A.MENU_ID IS NULL THEN 'N' ELSE 'Y' END) AS authYn                 \n" +
            "  FROM OCO.OCO10210 T                                                              \n" +
            " LEFT OUTER JOIN OCO.OCO10106 A ON (A.MENU_ID = T.MENU_ID AND A.USERID = :userid)       \n" +
            " LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = T.SCREN_ID)                       \n" +
            " LEFT OUTER JOIN OCO.OCO10104 C ON (C.MENU_ID = T.MENU_ID AND C.USERID = :userid)  \n" +
            " LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND D.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD) \n" +
            " LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = T.SUPER_MENU_ID)       \n" +
            "WHERE T.USE_YN = 'Y'                                                   \n" +
            "  AND T.MENU_NM LIKE CONCAT('%',:menuNm,'%') \n", nativeQuery = true)
    List<MenuIDto> searchMenuAuthByUserid(String userid, String menuNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value = "SELECT new com.skcc.ra.common.api.dto.domainDto.MenuDto(t, l, n.cmmnCdValNm, o.menuNm, userAuthReqHis.athrtyAddnYn, 'auth') " +
            "FROM UserAuthReqHis userAuthReqHis " +
            "LEFT OUTER JOIN Menu t ON t.menuId = userAuthReqHis.menuId  " +
            "LEFT OUTER JOIN Scren l ON (l.useYn = 'Y' AND t.screnId = l.screnId) " +
            "LEFT OUTER JOIN CmmnCdDtl n ON (n.cmmnCd = 'CHRG_TASK_GROUP_CD' AND t.chrgTaskGroupCd = n.cmmnCdVal) " +
            "LEFT OUTER JOIN Menu o ON o.menuId = t.superMenuId " +
            "WHERE t.useYn = 'Y' " +
            "AND userAuthReqHis.athrtyReqstSeq = :athrtyReqstSeq " +
            "AND userAuthReqHis.menuId IS NOT NULL " +
            "AND t.menuNm LIKE CONCAT('%',:menuNm,'%') ")
    List<MenuDto> searchReqMenuAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String menuNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT T.MENU_ID AS menuId                               \n" +
                  "         ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd           \n" +
                  "         ,T.MENU_TYPE_CD AS menuTypeCd                      \n" +
                  "         ,T.MENU_NM AS menuNm                               \n" +
                  "         ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd             \n" +
                  "         ,T.MENU_DESC AS menuDesc                           \n" +
                  "         ,T.MENU_STEP_VAL AS menuStepVal                    \n" +
                  "         ,T.SORT_SEQN AS sortSeqn                           \n" +
                  "         ,T.USE_YN AS useYn                                 \n" +
                  "         ,T.MENU_EXPSE_YN AS menuExpseYn                    \n" +
                  "         ,T.SUPER_MENU_ID AS superMenuId                    \n" +
                  "         ,E.MENU_NM AS superMenuNm                          \n" +
                  "         ,B.SCREN_ID AS screnId                             \n" +
                  "         ,B.SCREN_NM AS screnNm                             \n" +
                  "         ,B.SCREN_DESC AS screnDesc                         \n" +
                  "         ,B.SCREN_URLADDR AS screnUrladdr                   \n" +
                  "         ,B.SCREN_CL_CD AS screnClCd                        \n" +
                  "         ,B.SCREN_SIZE_CL_CD AS screnSizeClCd               \n" +
                  "         ,B.SCREN_WIDTH_SIZE AS screnWidthSize              \n" +
                  "         ,B.SCREN_VRTLN_SIZE AS screnVrtlnSize              \n" +
                  "         ,B.SCREN_START_TOP_CODN AS screnStartTopCodn       \n" +
                  "         ,B.SCREN_START_LEFT_CODN AS screnStartLeftCodn     \n" +
                  "         ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt     \n" +
                  "         ,D.CMMN_CD_VAL_NM AS chrgTaskGroupNm               \n" +
                  "         ,(CASE WHEN C.MENU_ID IS NULL THEN 'N' ELSE 'Y' END) AS bookmarkYN                 \n" +
                  "  FROM OCO.OCO10210 T                                                              \n" +
                  " INNER JOIN OCO.OCO10106 A ON (A.MENU_ID = T.MENU_ID AND A.USERID = :userid)       \n" +
                  " LEFT OUTER JOIN OCO.OCO10220 B ON (B.SCREN_ID = T.SCREN_ID)                       \n" +
                  " LEFT OUTER JOIN OCO.OCO10104 C ON (C.MENU_ID = T.MENU_ID AND C.USERID = :userid)  \n" +
                  " LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND D.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD) \n" +
                  " LEFT OUTER JOIN OCO.OCO10210 E ON (E.MENU_ID = T.SUPER_MENU_ID)       \n" +
                  "WHERE T.USE_YN = 'Y'                                                   \n" +
                  "  AND T.MENU_EXPSE_YN ='Y'                                             \n" +
                  "  AND T.MENU_TYPE_CD = '1'", nativeQuery = true)
    List<MenuIDto> findUserMenus(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.MenuDto(t, l, n.cmmnCdValNm, o.menuNm, CASE WHEN m.menuId IS NULL THEN 'N' ELSE 'Y' END, 'bookmark') " +
            "FROM Menu t " +
            "INNER JOIN UserMenu k ON (k.menuId = t.menuId) " +
            "LEFT OUTER JOIN Scren l ON (l.useYn = 'Y' AND t.screnId = l.screnId) " +
            "LEFT OUTER JOIN BookmarkMenu m ON (m.userid = :userid AND m.menuId = t.menuId) " +
            "LEFT OUTER JOIN CmmnCdDtl n ON (n.cmmnCd = 'CHRG_TASK_GROUP_CD' AND t.chrgTaskGroupCd = n.cmmnCdVal) " +
            "LEFT OUTER JOIN Menu o ON (o.menuId = t.superMenuId) " +
            "WHERE t.useYn = 'Y' " +
            "  AND k.userid = :userid " +
            "  AND t.menuId = :menuId " +
            "  AND t.menuTypeCd = '1' ")
    MenuDto findUserMenuByMenuId(@Param("userid") String userid, @Param("menuId") String menuId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<UserMenu> findByUserid(String userid);
}
