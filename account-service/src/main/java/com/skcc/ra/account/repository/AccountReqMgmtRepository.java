package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto;
import com.skcc.ra.account.domain.account.AccountReqMgmt;
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
public interface AccountReqMgmtRepository extends JpaRepository<AccountReqMgmt, Integer> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<AccountReqMgmt> findByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value =  "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountReqMgmtDto(t, a.userNm, a.deptcd, d.deptNm, c.cmmnCdValNm, \n" +
                                                                                          "a2.userNm, a3.userNm, n.clofpNm, n.vctnNm, c1.cmmnCdValNm) \n" +
                    "  FROM AccountReqMgmt t \n" +
                    "  LEFT OUTER JOIN Account a ON t.userid = a.userid \n" +
                    "  LEFT OUTER JOIN Dept d ON a.deptcd = d.deptcd \n" +
                    "  LEFT OUTER JOIN CmmnCdDtl c ON t.useridReqstStsCd = c.cmmnCdVal AND c.cmmnCd = 'USERID_REQST_STS_CD' \n" +
                    "  LEFT OUTER JOIN CmmnCdDtl c1 ON a.reofoCd = c1.cmmnCdVal AND c1.cmmnCd = 'REOFO_CD' \n" +
                    "  LEFT OUTER JOIN Account a2 ON a2.userid = t.rvwUserid  \n" +
                    "  LEFT OUTER JOIN Account a3 ON a3.userid = t.authzUserid \n" +
                    "  LEFT OUTER JOIN UserBasic n ON (n.empno = a.userIdentNo) \n" +
                    " WHERE 1=1  \n" +
                    "   AND a.innerUserClCd IN :innerUserClCd \n" +
                    "   AND a.userNm LIKE CONCAT('%',:userNm,'%') \n" +
                    "   AND (''= :myDeptcd OR d.deptcd = :myDeptcd OR d.superDeptcd = :myDeptcd OR d.deptcd IN :deptList) \n" +
                    "   AND ('' = :useridReqstStsCd OR t.useridReqstStsCd = :useridReqstStsCd) \n" +
                    "   AND t.useridReqstDt >= :useridReqstDtFrom AND t.useridReqstDt <= :useridReqstDtTo \n" +
                    " ORDER BY t.useridReqstSeq DESC")
    Page<AccountReqMgmtDto> searchAccountRequestByCondition(@Param("userNm") String userNm,
                                                            @Param("useridReqstStsCd") String useridReqstStsCd,
                                                            @Param("useridReqstDtFrom") String useridReqstDtFrom,
                                                            @Param("useridReqstDtTo") String useridReqstDtTo,
                                                            @Param("innerUserClCd")List<String> innerUserClCd,
                                                            @Param("myDeptcd") String myDeptcd,
                                                            @Param("deptList") List<String> deptList,
                                                            Pageable pageable);
}
