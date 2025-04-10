package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.responseDto.UserAuthReqListDto;
import com.skcc.ra.account.domain.addAuth.UserAuthReq;
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
public interface UserAuthReqRepository extends JpaRepository<UserAuthReq, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT t FROM UserAuthReq t WHERE t.athrtyReqstSeq = :athrtyReqstSeq" )
    Optional<UserAuthReq> findByAthrtyReqstSeq(@Param("athrtyReqstSeq") Integer athrtyReqstSeq);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT new com.skcc.ra.account.api.dto.responseDto.UserAuthReqListDto( \n" +
                   "       T.athrtyReqstSeq,        \n" +
                   "       A.deptcd,                \n" +
                   "       B.deptNm,                \n" +
                   "       C3.cmmnCdValNm,          \n" +
                   "       E.clofpNm,               \n" +
                   "       E.vctnNm,                \n" +
                   "       A.userid,                \n" +
                   "       A.userNm,                \n" +
                   "       T.reqstResonCntnt,       \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'Q' THEN H.athrtyReqstOpDtm ELSE '' END)  AS Q_athrtyReqstOpDtm,  \n" +
                   "       T.athrtyReqstStsCd,      \n" +
                   "       C1.cmmnCdValNm,          \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'C' THEN H.settlUserid ELSE '' END)       AS C_settlUserid,       \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'C' THEN A1.userNm ELSE '' END)           AS C_userNm,            \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'C' THEN H.gvbkResonCntnt ELSE '' END)    AS C_gvbkResonCntnt,    \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'C' THEN H.athrtyReqstOpDtm ELSE '' END)  AS C_athrtyReqstOpDtm,  \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'A' THEN H.settlUserid ELSE '' END)       AS A_settlUserid,       \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'A' THEN A1.userNm ELSE '' END)           AS A_userNm,            \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'A' THEN H.settlUserIpaddr ELSE '' END)   AS settlUserIpaddr,     \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'A' THEN H.athrtyReqstOpDtm ELSE '' END)  AS A_athrtyReqstOpDtm,  \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'R' THEN H.settlUserid ELSE '' END)       AS R_settlUserid,       \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'R' THEN A1.userNm ELSE '' END)           AS R_userNm,            \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd IN ('R','A') THEN H.gvbkResonCntnt ELSE '' END) AS gvbkResonCntnt,  \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'R' THEN H.athrtyReqstOpDtm ELSE '' END)  AS R_athrtyReqstOpDtm,  \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'S' THEN H.settlUserid ELSE '' END)       AS S_settlUserid,       \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'S' THEN A1.userNm ELSE '' END)           AS S_userNm,            \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'S' THEN H.gvbkResonCntnt ELSE '' END)    AS S_gvbkResonCntnt,    \n" +
                   "       MAX(CASE WHEN H.athrtyReqstStsCd = 'S' THEN H.athrtyReqstOpDtm ELSE '' END)  AS S_athrtyReqstOpDtm,  \n" +
                   "       T.indivInfoYn,            \n" +
                   "       T.rpaUserYn,               \n" +
                   "       T.reqstUserIpaddr )            \n" +
                   "  FROM UserAuthReq T                                    \n" +
                   "  LEFT OUTER JOIN Account A ON (A.userid = T.userid)    \n" +
                   "  LEFT OUTER JOIN Dept B    ON (B.deptcd = A.deptcd)    \n" +
                   "  LEFT OUTER JOIN CmmnCdDtl C1 ON C1.cmmnCd = 'ATHRTY_REQST_STS_CD' AND C1.cmmnCdVal = T.athrtyReqstStsCd \n" +
                   "  LEFT OUTER JOIN CmmnCdDtl C3 ON C3.cmmnCd = 'REOFO_CD' AND C3.cmmnCdVal = A.reofoCd   \n" +
                   "  LEFT OUTER JOIN UserAuthReqProcHis H ON H.athrtyReqstSeq = T.athrtyReqstSeq           \n" +
                   "  LEFT OUTER JOIN Account A1 ON A1.userid = H.settlUserid                               \n" +
                   "  LEFT OUTER JOIN UserBasic E ON E.empno = A.userIdentNo                                \n" +
                   " WHERE A.innerUserClCd IN :innerUserClCd                                                              \n" +
                   "   AND A.userNm LIKE CONCAT('%',:userNm,'%') \n" +
                   "   AND T.athrtyReqstStsCd LIKE CONCAT('%',:athrtyReqstStsCd,'%') \n" +
                   "   AND T.userid LIKE CONCAT('%',:userid,'%') \n" +
                   "   AND ('' = :indivInfoYn OR T.indivInfoYn = :indivInfoYn) \n" +
                   "   AND (''= :myDeptcd OR B.deptcd = :myDeptcd OR B.superDeptcd = :myDeptcd OR B.deptcd IN :deptList) \n" +
                   " GROUP BY T.athrtyReqstSeq, A.deptcd, B.deptNm, C3.cmmnCdValNm, E.clofpNm, E.vctnNm, A.userid, A.userNm, \n" +
                   "          T.reqstResonCntnt, T.athrtyReqstStsCd, C1.cmmnCdValNm, T.indivInfoYn, T.rpaUserYn, T.reqstUserIpaddr \n" +
                   "HAVING MAX(CASE WHEN H.athrtyReqstStsCd = 'Q' THEN H.athrtyReqstOpDtm ELSE '' END) BETWEEN :athrtyReqstOpDtmFrom AND :athrtyReqstOpDtmTo \n" +
                   " ORDER BY T.athrtyReqstSeq DESC \n")
    Page<UserAuthReqListDto> searchUserAuthReqList(@Param("userNm") String userNm,
                                                   @Param("athrtyReqstOpDtmFrom") String athrtyReqstOpDtmFrom,
                                                   @Param("athrtyReqstOpDtmTo") String athrtyReqstOpDtmTo,
                                                   @Param("athrtyReqstStsCd") String athrtyReqstStsCd,
                                                   @Param("userid")String userid,
                                                   @Param("myDeptcd")String myDeptcd,
                                                   @Param("deptList")List<String> deptLst,
                                                   @Param("innerUserClCd")List<String> innerUserClCd,
                                                   @Param("indivInfoYn")String indivInfoYn,
                                                   Pageable pageable);

}
