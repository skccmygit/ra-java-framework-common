package kr.co.skcc.oss.com.common.repository;


import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.CmmnCdIDto;
import kr.co.skcc.oss.com.common.domain.cmmnCd.CmmnCd;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface CmmnCdRepository extends JpaRepository<CmmnCd, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT t " +
                "FROM CmmnCd t " +
                "WHERE 1=1  " +
                "AND ('' = :systmClCd OR t.systmClCd = :systmClCd) " +
                "AND ('' = :chrgTaskGroupCd OR t.chrgTaskGroupCd = :chrgTaskGroupCd) " +
                "AND (t.cmmnCdNm LIKE CONCAT('%',:cmmnCdNm,'%') OR t.cmmnCd LIKE CONCAT('%',:cmmnCdNm,'%')) ")
    List<CmmnCd> findCmmnCdNm(@Param("systmClCd") String systmClCd,
                                 @Param("chrgTaskGroupCd") String chrgTaskGroupCd,
                                 @Param("cmmnCdNm") String cmmnCdNm, Sort sort);

    List<CmmnCd> findBySystmClCdAndBssCmmnCd(String systmClCd, String bssCmmnCd);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT C.CMMN_CD_VAL_NM AS systmClNm         \n" +
                  "     , D.CMMN_CD_VAL_NM AS chrgTaskGroupNm   \n" +
                  "     , A.CMMN_CD AS cmmnCd                   \n" +
                  "     , A.CMMN_CD_NM AS cmmnCdNm              \n" +
                  "     , A.BSS_CMMN_CD AS bssCmmnCd            \n" +
                  "     , A.CMMN_CD_DESC AS cmmnCdDesc          \n" +
                  "     , A.CMMN_CD_VAL_LENTH AS cmmnCdValLenth \n" +
                  "     , A.USE_YN AS useYn                     \n" +
                  "     , B.CMMN_CD_VAL AS cmmnCdVal            \n" +
                  "     , B.CMMN_CD_VAL_NM AS cmmnCdValNm       \n" +
                  "     , B.CMMN_CD_VAL_DESC AS cmmnCdValDesc   \n" +
                  "     , B.SORT_SEQN AS sortSeqn               \n" +
                  "     , B.USE_YN AS dtlUseYn                  \n" +
                  "     , B.SUPER_CMMN_CD AS superCmmnCd        \n" +
                  "     , E.CMMN_CD_NM AS superCmmnCdNm         \n" +
                  "     , B.SUPER_CMMN_CD_VAL AS superCmmnCdVal \n" +
                  "     , F.CMMN_CD_VAL_NM AS superCmmnCdValNm  \n" +
                  "     , A.REFRN_ATTR_NM1  AS refrnAttrNm1     \n" +
                  "     , A.REFRN_ATTR_NM2  AS refrnAttrNm2     \n" +
                  "     , A.REFRN_ATTR_NM3  AS refrnAttrNm3     \n" +
                  "     , A.REFRN_ATTR_NM4  AS refrnAttrNm4     \n" +
                  "     , A.REFRN_ATTR_NM5  AS refrnAttrNm5     \n" +
                  "     , A.REFRN_ATTR_NM6  AS refrnAttrNm6     \n" +
                  "     , A.REFRN_ATTR_NM7  AS refrnAttrNm7     \n" +
                  "     , A.REFRN_ATTR_NM8  AS refrnAttrNm8     \n" +
                  "     , A.REFRN_ATTR_NM9  AS refrnAttrNm9     \n" +
                  "     , A.REFRN_ATTR_NM10 AS refrnAttrNm10    \n" +
                  "     , B.REFRN_ATTR_VAL1  AS refrnAttrVal1   \n" +
                  "     , B.REFRN_ATTR_VAL2  AS refrnAttrVal2   \n" +
                  "     , B.REFRN_ATTR_VAL3  AS refrnAttrVal3   \n" +
                  "     , B.REFRN_ATTR_VAL4  AS refrnAttrVal4   \n" +
                  "     , B.REFRN_ATTR_VAL5  AS refrnAttrVal5   \n" +
                  "     , B.REFRN_ATTR_VAL6  AS refrnAttrVal6   \n" +
                  "     , B.REFRN_ATTR_VAL7  AS refrnAttrVal7   \n" +
                  "     , B.REFRN_ATTR_VAL8  AS refrnAttrVal8   \n" +
                  "     , B.REFRN_ATTR_VAL9  AS refrnAttrVal9   \n" +
                  "     , B.REFRN_ATTR_VAL10 AS refrnAttrVal10  \n" +
                  "  FROM OCO.OCO20100 A    \n" +
                  "  LEFT OUTER JOIN OCO.OCO20101 B ON (A.CMMN_CD = B.CMMN_CD)      \n" +
                  "  LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'SKSH_SYSTM_CD' AND C.CMMN_CD_VAL = A.SKSH_SYSTM_CD) \n" +
                  "  LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND D.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)   \n" +
                  "  LEFT OUTER JOIN OCO.OCO20100 E ON (E.CMMN_CD = B.SUPER_CMMN_CD)   \n" +
                  "  LEFT OUTER JOIN OCO.OCO20101 F ON (F.CMMN_CD = B.SUPER_CMMN_CD AND F.CMMN_CD_VAL = B.SUPER_CMMN_CD_VAL)   \n" +
                  " WHERE ('' = :systmClCd OR A.SKSH_SYSTM_CD = :systmClCd)                  \n" +
                  "   AND ('' = :chrgTaskGroupCd OR A.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)        \n" +
                  "   AND (A.CMMN_CD LIKE CONCAT('%',:cmmnCdNm,'%') OR A.CMMN_CD_NM LIKE CONCAT('%',:cmmnCdNm,'%')) \n" +
                  " ORDER BY A.CMMN_CD, B.SORT_SEQN" +
                  " LIMIT :offset, :size   ", nativeQuery = true)
    List<CmmnCdIDto> findCmmnCdAndCmmnCdDtl(@Param("systmClCd") String systmClCd,
                                            @Param("chrgTaskGroupCd") String chrgTaskGroupCd,
                                            @Param("cmmnCdNm") String cmmnCdNm,
                                            @Param("offset") int offset,
                                            @Param("size") int size);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT COUNT(*)    \n" +
            "  FROM OCO.OCO20100 A    \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 B ON (A.CMMN_CD = B.CMMN_CD)      \n" +
            " WHERE ('' = :systmClCd OR A.SKSH_SYSTM_CD = :systmClCd)                  \n" +
            "   AND ('' = :chrgTaskGroupCd OR A.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)        \n" +
            "   AND (A.CMMN_CD LIKE CONCAT('%',:cmmnCdNm,'%') OR A.CMMN_CD_NM LIKE CONCAT('%',:cmmnCdNm,'%')) \n" +
            " ORDER BY A.CMMN_CD, B.SORT_SEQN", nativeQuery = true)
    int excelDataCount(@Param("systmClCd") String systmClCd,
                                            @Param("chrgTaskGroupCd") String chrgTaskGroupCd,
                                            @Param("cmmnCdNm") String cmmnCdNm);
}
