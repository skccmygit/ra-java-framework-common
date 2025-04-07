package com.skcc.ra.common.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuExelDownloadIDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT T.MENU_ID AS menuId                                   \n" +
                  "      ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd               \n" +
                  "      ,T.MENU_TYPE_CD AS menuTypeCd                          \n" +
                  "      ,T.MENU_NM AS menuNm                                   \n" +
                  "      ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd                 \n" +
                  "      ,T.MENU_DESC AS menuDesc                               \n" +
                  "      ,T.MENU_STEP_VAL AS menuStepVal                        \n" +
                  "      ,T.SORT_SEQN AS sortSeqn                               \n" +
                  "      ,T.USE_YN AS useYn                                     \n" +
                  "      ,T.MENU_EXPSE_YN AS menuExpseYn                        \n" +
                  "      ,T.SUPER_MENU_ID AS superMenuId                        \n" +
                  "      ,C.MENU_NM AS superMenuNm                              \n" +
                  "      ,A.SCREN_ID AS screnId                                 \n" +
                  "      ,A.SCREN_NM AS screnNm                                 \n" +
                  "      ,A.SCREN_DESC AS screnDesc                             \n" +
                  "      ,A.SCREN_URLADDR AS screnUrladdr                       \n" +
                  "      ,A.SCREN_CL_CD AS screnClCd                            \n" +
                  "      ,A.SCREN_SIZE_CL_CD AS screnSizeClCd                   \n" +
                  "      ,A.SCREN_WIDTH_SIZE AS screnWidthSize                  \n" +
                  "      ,A.SCREN_VRTLN_SIZE AS screnVrtlnSize                  \n" +
                  "      ,A.SCREN_START_TOP_CODN AS screnStartTopCodn           \n" +
                  "      ,A.SCREN_START_LEFT_CODN AS screnStartLeftCodn         \n" +
                  "      ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt         \n" +
                  "      ,B.CMMN_CD_VAL_NM AS chrgTaskGroupNm                   \n" +
                  "  FROM OCO.OCO10210 T                                        \n" +
                  "  LEFT OUTER JOIN OCO.OCO10220 A ON (A.SCREN_ID = T.SCREN_ID) \n" +
                  "  LEFT OUTER JOIN OCO.OCO20101 B ON (B.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND B.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD)   \n" +
                  "  LEFT OUTER JOIN OCO.OCO10210 C ON (C.MENU_ID = T.SUPER_MENU_ID)" +
                  " WHERE ('' = :useYn OR T.USE_YN = :useYn) ", nativeQuery = true)
    List<MenuIDto> findAllMenu(@Param("useYn") String useYn);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT T.MENU_ID AS menuId                                   \n" +
            "      ,T.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd               \n" +
            "      ,T.MENU_TYPE_CD AS menuTypeCd                          \n" +
            "      ,T.MENU_NM AS menuNm                                   \n" +
            "      ,T.SCREN_EXCUT_CL_CD AS screnExcutClCd                 \n" +
            "      ,T.MENU_DESC AS menuDesc                               \n" +
            "      ,T.MENU_STEP_VAL AS menuStepVal                        \n" +
            "      ,T.SORT_SEQN AS sortSeqn                               \n" +
            "      ,T.USE_YN AS useYn                                     \n" +
            "      ,T.MENU_EXPSE_YN AS menuExpseYn                        \n" +
            "      ,T.SUPER_MENU_ID AS superMenuId                        \n" +
            "      ,C.MENU_NM AS superMenuNm                              \n" +
            "      ,A.SCREN_ID AS screnId                                 \n" +
            "      ,A.SCREN_NM AS screnNm                                 \n" +
            "      ,A.SCREN_DESC AS screnDesc                             \n" +
            "      ,A.SCREN_URLADDR AS screnUrladdr                       \n" +
            "      ,A.SCREN_CL_CD AS screnClCd                            \n" +
            "      ,A.SCREN_SIZE_CL_CD AS screnSizeClCd                   \n" +
            "      ,A.SCREN_WIDTH_SIZE AS screnWidthSize                  \n" +
            "      ,A.SCREN_VRTLN_SIZE AS screnVrtlnSize                  \n" +
            "      ,A.SCREN_START_TOP_CODN AS screnStartTopCodn           \n" +
            "      ,A.SCREN_START_LEFT_CODN AS screnStartLeftCodn         \n" +
            "      ,T.LINKA_SYSTM_TAG_CNTNT AS linkaSystmTagCntnt         \n" +
            "      ,B.CMMN_CD_VAL_NM AS chrgTaskGroupNm                   \n" +
            "  FROM OCO.OCO10210 T                                        \n" +
            "  LEFT OUTER JOIN OCO.OCO10220 A ON (A.SCREN_ID = T.SCREN_ID) \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 B ON (B.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND B.CMMN_CD_VAL = T.CHRG_TASK_GROUP_CD)   \n" +
            "  LEFT OUTER JOIN OCO.OCO10210 C ON (C.MENU_ID = T.SUPER_MENU_ID) \n" +
            " WHERE T.MENU_ID = :menuId ", nativeQuery = true)
    MenuIDto findMenuDtl(@Param("menuId") String menuId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT A.MENU_ID AS menuId1              \n" +
            "             ,A.MENU_NM AS menuNm1             \n" +
            "             ,A.USE_YN AS useYn1               \n" +
            "             ,B.MENU_ID AS menuId2             \n" +
            "             ,B.MENU_NM AS menuNm2             \n" +
            "             ,B.USE_YN AS useYn2               \n" +
            "             ,IFNULL(C.MENU_ID,'') AS menuId3  \n" +
            "             ,IFNULL(C.MENU_NM,'') AS menuNm3  \n" +
            "             ,IFNULL(C.USE_YN,'') AS useYn3    \n" +
            "             ,IFNULL(D.MENU_ID,'') AS menuId4  \n" +
            "             ,IFNULL(D.MENU_NM,'') AS menuNm4  \n" +
            "             ,IFNULL(D.USE_YN,'') AS useYn4    \n" +
            "             ,IFNULL(E.SCREN_ID,'') AS screnId \n" +
            "             ,IFNULL(E.SCREN_NM,'') AS screnNm \n" +
            "             ,IFNULL(E.SCREN_URLADDR,'') AS screnUrladdr \n" +
            "        FROM (SELECT A.MENU_ID, A.MENU_NM, A.SORT_SEQN, A.USE_YN FROM OCO.OCO10210 A \n" +
            "       WHERE A.MENU_STEP_VAL = 0               \n" +
            "       ORDER BY SORT_SEQN) A                   \n" +
            "        LEFT OUTER JOIN (SELECT A.MENU_ID, A.MENU_NM, A.SORT_SEQN AS SORT_SEQN, A.SUPER_MENU_ID, A.USE_YN, A.SCREN_ID FROM OCO.OCO10210 A \n" +
            "          WHERE A.MENU_STEP_VAL = 1                                \n" +
            "          ORDER BY SORT_SEQN) B ON (A.MENU_ID = B.SUPER_MENU_ID)   \n" +
            "        LEFT OUTER JOIN (SELECT A.MENU_ID, A.MENU_NM, A.SORT_SEQN AS SORT_SEQN, A.SUPER_MENU_ID, A.USE_YN, A.SCREN_ID FROM OCO.OCO10210 A \n" +
            "          WHERE A.MENU_STEP_VAL = 2                                \n" +
            "          ORDER BY SORT_SEQN) C ON (B.MENU_ID = C.SUPER_MENU_ID)   \n" +
            "        LEFT OUTER JOIN (SELECT A.MENU_ID, A.MENU_NM, A.SORT_SEQN AS SORT_SEQN, A.SUPER_MENU_ID, A.USE_YN, A.SCREN_ID FROM OCO.OCO10210 A \n" +
            "          WHERE A.MENU_STEP_VAL = 3                                \n" +
            "          ORDER BY SORT_SEQN) D ON (C.MENU_ID = D.SUPER_MENU_ID)   \n" +
            "        LEFT OUTER JOIN OCO.OCO10220 E ON IFNULL(D.SCREN_ID,IFNULL(C.SCREN_ID,IFNULL(B.SCREN_ID,''))) = E.SCREN_ID \n" +
            "       ORDER BY A.SORT_SEQN,B.SORT_SEQN,C.SORT_SEQN,D.SORT_SEQN", nativeQuery = true)
    List<MenuExelDownloadIDto> excelDownloadMenu();

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Menu findByScrenIdAndUseYn(String screnId, String useYn);
}

