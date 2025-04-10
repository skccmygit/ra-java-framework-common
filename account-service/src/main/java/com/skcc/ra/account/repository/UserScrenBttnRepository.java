package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.auth.UserScrenBttn;
import com.skcc.ra.account.domain.auth.pk.UserScrenBttnPK;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ScrenIDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserScrenBttnRepository extends JpaRepository<UserScrenBttn, UserScrenBttnPK> {
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<UserScrenBttn> findByUseridAndScrenIdAndBttnId(@Param("userid") String userid,
                                                            @Param("screnId") String screnId,
                                                            @Param("bttnId") String bttnId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t, CASE WHEN k.bttnId IS NULL THEN 'N' ELSE 'Y' END) " +
                    "FROM Bttn t " +
                    "LEFT OUTER JOIN UserScrenBttn k ON (k.screnId = t.screnId AND k.bttnId = t.bttnId AND k.userid = :userid) " +
                   "WHERE t.screnId = :screnId " +
                     "AND t.useYn = 'Y' ")
    List<BttnDto> searchBttnAuthByUserid(@Param("userid")String userid,
                                         @Param("screnId")String screnId);


    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.ScrenDto(t, l.cmmnCdValNm,  userAuthReqHis.athrtyAddnYn)  " +
            "FROM UserAuthReqHis userAuthReqHis " +
            "LEFT OUTER JOIN Scren t ON userAuthReqHis.screnId = t.screnId " +
            "LEFT OUTER JOIN CmmnCdDtl l ON (l.cmmnCd = 'CHRG_TASK_GROUP_CD' AND t.chrgTaskGroupCd = l.cmmnCdVal) " +
            "WHERE t.useYn = 'Y' " +
            "AND userAuthReqHis.athrtyReqstSeq = :athrtyReqstSeq " +
            "AND userAuthReqHis.screnId IS NOT NULL " +
            "  AND t.chrgTaskGroupCd LIKE CONCAT('%',:chrgTaskGroupCd,'%') " +
            "  AND t.screnNm LIKE CONCAT('%',:screnNm,'%') ")
    List<ScrenDto> searchReqScrenAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String chrgTaskGroupCd, String screnNm);


    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t, userAuthReqHis.athrtyAddnYn) " +
            "FROM UserAuthReqHis userAuthReqHis " +
            "LEFT OUTER JOIN Bttn t ON userAuthReqHis.bttnId = t.bttnId " +
            "WHERE t.screnId = :screnId " +
            "AND userAuthReqHis.athrtyReqstSeq = :athrtyReqstSeq " +
            "AND userAuthReqHis.bttnId IS NOT NULL " +
            "AND t.useYn = 'Y' ")
    List<BttnDto> searchReqBttnAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String screnId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<UserScrenBttn> findByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT A.SCREN_ID AS screnId,                   \n" +
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
            "             C.CMMN_CD_VAL_NM AS chrgTaskGroupNm,              \n" +
            "             D.CMMN_CD_VAL_NM AS screnClNm                     \n" +
            "        FROM OCO.OCO10220 A                                    \n" +
            "       INNER JOIN OCO.OCO10107 B ON (A.SCREN_ID = B.SCREN_ID)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND C.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'SCREN_CL_CD' AND D.CMMN_CD_VAL = A.SCREN_CL_CD)                \n" +
            "       WHERE A.USE_YN = 'Y'                \n" +
            "         AND B.USERID IN (:userid)    \n" +
            "       ORDER BY A.SCREN_ID "
            ,nativeQuery = true)
    List<ScrenIDto> searchScrenByUserid(@Param("userid")String userid);
}
