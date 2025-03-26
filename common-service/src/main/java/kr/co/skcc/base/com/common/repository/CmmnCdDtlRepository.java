package kr.co.skcc.base.com.common.repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto;
import kr.co.skcc.base.com.common.domain.cmmnCd.CmmnCdDtl;
import kr.co.skcc.base.com.common.domain.cmmnCd.pk.CmmnCdDtlPK;

@Repository
public interface CmmnCdDtlRepository extends JpaRepository<CmmnCdDtl, CmmnCdDtlPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto(t, m.cmmnCdNm, k.cmmnCdNm, l.cmmnCdValNm) " +
            "FROM CmmnCdDtl t " +
            "INNER JOIN CmmnCd m ON t.cmmnCd = m.cmmnCd " +
            "LEFT OUTER JOIN CmmnCd k ON t.superCmmnCd = k.cmmnCd " +
            "LEFT OUTER JOIN CmmnCdDtl l ON l.cmmnCd = t.superCmmnCd AND t.superCmmnCdVal = l.cmmnCdVal " +
            "WHERE t.superCmmnCd = :superCmmnCd AND t.superCmmnCdVal = :superCmmnCdVal " +
            "  AND t.cmmnCd = :cmmnCd " +
            "  AND t.useYn = 'Y' ")
    List<CmmnCdDtlDto> findSuperCmmnCd(String cmmnCd, String superCmmnCd, String superCmmnCdVal, Sort sort);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    // @Cacheable(value = "cmmnCd", key="#cmmnCd")
    @Query(value = "SELECT T.CMMN_CD AS cmmnCd,              \n" +
            "     M.CMMN_CD_NM AS cmmnCdNm,                 \n" +
            "     T.CMMN_CD_VAL AS cmmnCdVal,               \n" +
            "     T.LAST_CHNGR_ID AS lastChngrId,           \n" +
            "     T.LAST_CHNG_DTMD AS lastChngDtmd,         \n" +
            "     T.CMMN_CD_VAL_NM AS cmmnCdValNm,          \n" +
            "     T.CMMN_CD_VAL_DESC AS cmmnCdValDesc,      \n" +
            "     T.SORT_SEQN AS sortSeqn,                  \n" +
            "     T.USE_YN AS useYn,                        \n" +
            "     T.REFRN_ATTR_VAL1  AS refrnAttrVal1 ,     \n" +
            "     T.REFRN_ATTR_VAL2  AS refrnAttrVal2 ,     \n" +
            "     T.REFRN_ATTR_VAL3  AS refrnAttrVal3 ,     \n" +
            "     T.REFRN_ATTR_VAL4  AS refrnAttrVal4 ,     \n" +
            "     T.REFRN_ATTR_VAL5  AS refrnAttrVal5 ,     \n" +
            "     T.REFRN_ATTR_VAL6  AS refrnAttrVal6 ,     \n" +
            "     T.REFRN_ATTR_VAL7  AS refrnAttrVal7 ,     \n" +
            "     T.REFRN_ATTR_VAL8  AS refrnAttrVal8 ,     \n" +
            "     T.REFRN_ATTR_VAL9  AS refrnAttrVal9 ,     \n" +
            "     T.REFRN_ATTR_VAL10 AS refrnAttrVal10,     \n" +
            "     T.SUPER_CMMN_CD    AS superCmmnCd,        \n" +
            "     K.CMMN_CD_NM       AS superCmmnCdNm,      \n" +
            "     T.SUPER_CMMN_CD_VAL AS superCmmnCdVal,    \n" +
            "     L.CMMN_CD_VAL_NM   AS superCmmnCdValNm      \n" +
            " FROM OCO.OCO20101 T \n" +
            " INNER JOIN OCO.OCO20100 M ON (M.CMMN_CD = T.CMMN_CD)\n" +
            " LEFT OUTER JOIN OCO.OCO20100 K ON (K.CMMN_CD = T.SUPER_CMMN_CD)\n" +
            " LEFT OUTER JOIN OCO.OCO20101 L ON (L.CMMN_CD = T.SUPER_CMMN_CD AND L.CMMN_CD_VAL = T.SUPER_CMMN_CD_VAL) \n" +
            " WHERE T.CMMN_CD = :cmmnCd \n" +
            "  AND ('' = :useYn OR T.USE_YN = :useYn) \n" +
            " ORDER BY T.SORT_SEQN ", nativeQuery = true)
    List<Map> findByCmmnCd(String cmmnCd, String useYn);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    // @CacheEvict(value = "cmmnCd", key="#cmmnCdDtl.cmmnCd")
    CmmnCdDtl save(CmmnCdDtl cmmnCdDtl);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    // @CacheEvict(value = "cmmnCd", key="#cmmnCdDtl.cmmnCd")
    void deleteById(CmmnCdDtlPK cmmnCdDtlPK);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT " +
                "new kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto(k,t.cmmnCdNm) " +
                "FROM CmmnCd t INNER JOIN CmmnCdDtl k " +
                "ON (t.cmmnCd = k.cmmnCd) " +
                "WHERE ('' = :chrgTaskGroupCd OR t.chrgTaskGroupCd = :chrgTaskGroupCd) " +
                "AND t.cmmnCdNm LIKE CONCAT('%',:cmmnCdNm,'%') " +
                "ORDER BY t.chrgTaskGroupCd, k.cmmnCd, k.sortSeqn")
    List<CmmnCdDtlDto> findCmmnCdDtl(@Param("chrgTaskGroupCd") String chrgTaskGroupCd, @Param("cmmnCdNm") String cmmnCdNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    CmmnCdDtl findByCmmnCdAndCmmnCdVal(String cmmnCd, String cmmnCdVal);

}
