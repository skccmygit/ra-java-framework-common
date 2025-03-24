package kr.co.skcc.oss.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.ScrenIDto;
import kr.co.skcc.oss.com.common.domain.menu.Scren;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface ScrenRepository extends JpaRepository<Scren, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT A.SCREN_ID AS screnId,                            \n" +
            "             A.CHRG_TASK_GROUP_CD AS chrgTaskGroupCd,          \n" +
            "             A.SCREN_NM AS screnNm,                            \n" +
            "             A.SCREN_DESC AS screnDesc,                        \n" +
            "             A.SCREN_URLADDR AS screnUrladdr,                  \n" +
            "             A.USE_YN AS useYn,                                \n" +
            "             A.SCREN_CL_CD AS screnClCd,                       \n" +
            "             A.SCREN_SIZE_CL_CD AS screnSizeClCd,              \n" +
            "             A.SCREN_WIDTH_SIZE AS screnWidthSize,             \n" +
            "             A.SCREN_VRTLN_SIZE AS screnVrtlnSize,             \n" +
            "             A.SCREN_START_TOP_CODN AS screnStartTopCodn,      \n" +
            "             A.SCREN_START_LEFT_CODN AS screnStartLeftCodn,    \n" +
            "             '' AS authYn,                                     \n" +
            "             B.CMMN_CD_VAL_NM AS chrgTaskGroupNm,              \n" +
            "             C.CMMN_CD_VAL_NM AS screnClNm                     \n" +
            "        FROM OCO.OCO10220 A                                    \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 B ON (B.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND B.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'SCREN_CL_CD' AND C.CMMN_CD_VAL = A.SCREN_CL_CD)                \n" +
            "       WHERE ( '' = :chrgTaskGroupCd OR A.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)                                       \n" +
            "         AND ( '' = :screnClCd OR A.SCREN_CL_CD = :screnClCd)                                                          \n" +
            "         AND ( '' = :useYn OR A.USE_YN = :useYn)                                                                       \n" +
            "         AND ( A.SCREN_NM LIKE CONCAT('%',:screnNm,'%')) "
            ,countQuery =  "SELECT COUNT(*)                     \n" +
            "        FROM OCO.OCO10220 A               \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 B ON (B.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND B.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'SCREN_CL_CD' AND C.CMMN_CD_VAL = A.SCREN_CL_CD)                \n" +
            "       WHERE ( '' = :chrgTaskGroupCd OR A.CHRG_TASK_GROUP_CD = :chrgTaskGroupCd)                                       \n" +
            "         AND ( '' = :screnClCd OR A.SCREN_CL_CD = :screnClCd)                                                          \n" +
            "         AND ( '' = :useYn OR A.USE_YN = :useYn)                                                                       \n" +
            "         AND ( A.SCREN_NM LIKE CONCAT('%',:screnNm,'%')) "
            ,nativeQuery = true)
    Page<ScrenIDto> findAllScren(@Param("chrgTaskGroupCd")String chrgTaskGroupCd,
                                 @Param("screnClCd")String screnClCd,
                                 @Param("screnNm")String screnNm,
                                 @Param("useYn")String useYn,
                                 Pageable pageable);

}
