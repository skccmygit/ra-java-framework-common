package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleMappedUserIDto;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.common.api.dto.domainDto.CmmnCdDtlDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<Account> findByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<Account> findByUseridAndUseridStsCdNot(String userid, String useridStsCd);

    /*이력저장을 위해 사용자ID로 부서정보만 가져오기*/
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountDto(t.userid, t.deptcd, d.deptNm) " +
            "FROM Account t " +
            "LEFT OUTER JOIN Dept d ON t.deptcd = d.deptcd " +
            "WHERE t.userid = :userid ")
    AccountDto findUserDeptInfo(String userid);


    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountDto(t, d.deptNm, l.cmmnCdValNm, n.clofpNm, n.vctnNm, o.cmmnCdValNm) \n" +
            "FROM Account t \n" +
            "LEFT OUTER JOIN Dept d ON t.deptcd = d.deptcd \n" +
            "LEFT OUTER JOIN CmmnCdDtl l ON (t.useridStsCd = l.cmmnCdVal AND l.cmmnCd = 'USERID_STS_CD') \n" +
            "LEFT OUTER JOIN UserBasic n ON (n.empno = t.userIdentNo) \n" +
            "LEFT OUTER JOIN CmmnCdDtl o ON (t.reofoCd = o.cmmnCdVal AND o.cmmnCd = 'REOFO_CD') \n" +
            "WHERE ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN t.deptcd IS NULL THEN '' ELSE t.deptcd END) IN :deptcdList) \n" +
            "  AND ('' = :useridOrNm OR t.userid = :useridOrNm OR t.userNm LIKE CONCAT('%',:useridOrNm,'%')) \n" +
            "  AND ('' = :useridStsCd OR t.useridStsCd = :useridStsCd) \n" +
            "  AND d.deptNm LIKE CONCAT('%',:deptNm,'%') \n" +
            "  AND t.fstRegDtmd BETWEEN :fstRegDtmdFrom AND :fstRegDtmdTo \n" +
            "ORDER BY d.deptNm, o.cmmnCdValNm, n.clofpNm, n.vctnNm, t.userid")
    Page<AccountDto> searchAccount(@Param("deptcdList") List<String> deptcdList,
                                   @Param("deptcdListYn") String deptcdListYn,
                                   @Param("useridOrNm") String useridOrNm,
                                   @Param("useridStsCd") String useridStsCd,
                                   @Param("deptNm") String deptcd,
                                   @Param("fstRegDtmdFrom") LocalDateTime fstRegDtmdFrom,
                                   @Param("fstRegDtmdTo") LocalDateTime fstRegDtmdTo,
                                   Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountDto(t, d.deptNm, l.cmmnCdValNm, n.clofpNm, n.vctnNm, o.cmmnCdValNm) " +
            "FROM Account t " +
            "LEFT OUTER JOIN Dept d ON t.deptcd = d.deptcd " +
            "LEFT OUTER JOIN CmmnCdDtl l ON (t.useridStsCd = l.cmmnCdVal AND l.cmmnCd = 'USERID_STS_CD') " +
            "LEFT OUTER JOIN UserBasic n ON (n.empno = t.userIdentNo) " +
            "LEFT OUTER JOIN CmmnCdDtl o ON (t.reofoCd = o.cmmnCdVal AND o.cmmnCd = 'REOFO_CD') " +
            "WHERE t.userid = :userid ")
    AccountDto searchAccountDtoInfo(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.common.api.dto.domainDto.CmmnCdDtlDto(t) " +
                  "FROM CmmnCdDtl t " +
                  "WHERE t.cmmnCd = :cmmnCd " +
                  "AND t.cmmnCdVal = :cmmnCdVal")
    CmmnCdDtlDto findCmmnCdValNm(@Param("cmmnCd") String cmmnCd,
                                 @Param("cmmnCdVal") String cmmnCdVal);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountDto(t, d.deptNm, l.cmmnCdValNm, n.clofpNm, n.vctnNm, o.cmmnCdValNm) " +
                  "FROM Account t " +
                  "LEFT OUTER JOIN Dept d ON t.deptcd = d.deptcd " +
                  "LEFT OUTER JOIN CmmnCdDtl l ON (t.useridStsCd = l.cmmnCdVal AND l.cmmnCd = 'USERID_STS_CD') " +
                  "LEFT OUTER JOIN UserBasic n ON (n.empno = t.userIdentNo) " +
                  "LEFT OUTER JOIN CmmnCdDtl o ON (t.reofoCd = o.cmmnCdVal AND o.cmmnCd = 'REOFO_CD') " +
                  "WHERE ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN t.deptcd IS NULL THEN '' ELSE t.deptcd END) IN :deptcdList) " +
                    "AND ('' = :userNm OR userid = :userNm OR t.userNm LIKE CONCAT(:userNm,'%')) " +
                    "AND t.useridStsCd <> 'D' ")
    List<AccountDto> searchAccountByCondition(@Param("deptcdList") List<String> deptcdList,
                                              @Param("deptcdListYn") String deptcdListYn,
                                              @Param("userNm") String userNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT A.DEPTCD AS deptcd                                                    \n" +
                  "      ,B.DEPT_NM AS deptNm                                                   \n" +
                  "      ,E.CLOFP_NM AS clofpNm                                                 \n" +
                  "      ,E.VCTN_NM AS vctnNm                                                   \n" +
                  "      ,T.USERID AS userid                                                    \n" +
                  "      ,A.USER_NM AS userNm                                                   \n" +
                  "  FROM OCO.OCO10101 T                                                        \n" +
                  "  INNER JOIN OCO.OCO10100 A ON (A.USERID = T.USERID)                         \n" +
                  "  LEFT OUTER JOIN OCO.OCO50200 B ON (B.DEPTCD = A.DEPTCD)                    \n" +
                  "  LEFT OUTER JOIN OCO.OCO50100 E ON (E.EMPNO = A.USER_IDENT_NO)              \n" +
                  " WHERE T.USER_ROLE_ID = :userRoleId                                                                 \n" +
                  "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN A.DEPTCD IS NULL THEN '' ELSE A.DEPTCD END) IN (:deptcdList))                 \n" +
                  "   AND A.USER_NM LIKE CONCAT('%',:userNm,'%')         \n" +
                  "   AND A.USER_IDENT_NO <> 'D' ", nativeQuery = true)
    List<RoleMappedUserIDto> findRoleAccountList(@Param("userRoleId") String userRoleId,
                                                 @Param("deptcdList") List<String> deptcdList,
                                                 @Param("deptcdListYn") String deptcdListYn,
                                                 @Param("userNm") String userNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Modifying
    @Query(value="UPDATE OCO.OCO10100 \n" +
                 "   SET CONN_PSSWD = :cryptoConnPwd, \n" +
                 "       PSSWD_EXPIR_DT = :expiredDt, \n" +
                 "       USERID_STS_CD = :useridStsCd, \n" +
                 "       PSSWD_ERR_FRQY = :psswdErrFrqy, \n" +
                 "       LAST_CHNGR_ID = :sysNm, \n" +
                 "       LAST_CHNG_DTMD = NOW() \n" +
                 " WHERE USER_IDENT_NO = :userid ",
           nativeQuery = true)
    void updateConnPwd(String userid, String cryptoConnPwd, String useridStsCd, Integer psswdErrFrqy, String expiredDt, String sysNm);

}
