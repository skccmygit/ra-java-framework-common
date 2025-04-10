package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.auth.RoleScrenBttn;
import com.skcc.ra.account.domain.auth.pk.RoleScrenBttnPK;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ScrenIDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface RoleScrenBttnRepository extends JpaRepository<RoleScrenBttn, RoleScrenBttnPK> {

    /* 역할-화면,버튼 매핑 화면에서 사용(화면별 버튼리스트) */
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t, CASE WHEN k.bttnId IS NULL THEN 'N' ELSE 'Y' END) " +
                "FROM Bttn t " +
                "LEFT OUTER JOIN RoleScrenBttn k ON (k.screnId = t.screnId AND k.bttnId = t.bttnId AND k.userRoleId IN :userRoleId) " +
                "WHERE t.screnId = :screnId " +
                "AND t.useYn = 'Y' ")
    List<BttnDto> findByScrenId(List<String> userRoleId, String screnId);

    /* 역할-화면,버튼 매핑 화면에서 사용(화면별 버튼리스트) */
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t, CASE WHEN (k.bttnId IS NULL AND u.bttnId IS NULL) THEN 'N' ELSE 'Y' END) " +
            "FROM Bttn t " +
            "LEFT OUTER JOIN RoleScrenBttn k ON (k.screnId = t.screnId AND k.bttnId = t.bttnId AND k.userRoleId IN :userRoleId) " +
            "LEFT OUTER JOIN UserScrenBttn u ON (u.screnId = t.screnId AND u.bttnId = t.bttnId AND u.userid = :userid) " +
            "WHERE t.screnId = :screnId " +
            "AND t.useYn = 'Y' ")
    List<BttnDto> findByScrenId(List<String> userRoleId, String userid, String screnId);

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
            "       INNER JOIN OCO.OCO10121 B ON (A.SCREN_ID = B.SCREN_ID)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND C.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'SCREN_CL_CD' AND D.CMMN_CD_VAL = A.SCREN_CL_CD)                \n" +
            "       WHERE A.USE_YN = 'Y'                \n" +
            "         AND B.USER_ROLE_ID IN (:roles)    \n" +
            "       ORDER BY A.SCREN_ID "
            ,nativeQuery = true)
    List<ScrenIDto> findUserScren(List<String> roles);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t) " +
                "FROM Bttn t " +
                "INNER JOIN RoleScrenBttn k ON k.screnId = t.screnId AND k.bttnId = t.bttnId AND k.userRoleId IN :roles " +
                "WHERE t.screnId = :screnId " +
                "AND t.useYn = 'Y' ")
    List<BttnDto> findUserScrenBttns(List<String> roles, String screnId);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT new com.skcc.ra.common.api.dto.domainDto.BttnDto(t) " +
                  "  FROM Bttn t \n" +
                  " WHERE t.screnId = :screnId \n" +
                  "   AND t.bttnId = :bttnId \n" +
                  "   AND t.useYn = 'Y' ")
    BttnDto findBttnInfo(String screnId, String bttnId);

}
