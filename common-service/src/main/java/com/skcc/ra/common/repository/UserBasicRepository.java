package com.skcc.ra.common.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.common.api.dto.domainDto.UserBasicDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.InnerCallInfoIDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.UserBasicScrenIDto;
import com.skcc.ra.common.domain.userBasic.UserBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserBasicRepository extends JpaRepository<UserBasic, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT B.BSSMACD AS bssmacd				                                                 \n" +
            "     , C.BSS_HQ_NM AS bssmacdNm                                                                 \n" +
            "     , B.DEPTCD AS deptcd                                                                       \n" +
            "     , B.DEPT_NM AS deptNm                                                                      \n" +
            "     , E.CMMN_CD_VAL_NM AS reofoNm                                                              \n" +
            "     , A.EMPNO AS empno                                                                         \n" +
            "     , A.EMP_KRN_NM AS empKrnNm                                                                 \n" +
            "     , A.CLOFP_CD AS clofpCd                                                                    \n" +
            "     , A.CLOFP_NM AS clofpNm                                                                    \n" +
            "     , A.VCTN_CD AS vctnCd                                                                    \n" +
            "     , A.VCTN_NM AS vctnNm                                                                      \n" +
            "     , A.MPHNO AS mphno                                                                        \n" +
            "     , A.TCOM_ENTCP_DT AS tcomEntcpDt                                                          \n" +
            "  FROM OCO.OCO50100 A                                                                           \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 B ON (A.DEPTCD = B.DEPTCD)                                       \n" +
            "  LEFT OUTER JOIN OCO.OCO50300 C ON (B.BSSMACD = C.BSSMACD)                                     \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 E ON (E.CMMN_CD = 'REOFO_CD' AND E.CMMN_CD_VAL = A.REOFO_CD)     \n" +
            " WHERE ('' = :bssmacd OR B.BSSMACD = :bssmacd)                                                  \n" +
            "   AND ('' = :deptcd OR B.DEPTCD = :deptcd)                                                     \n" +
            "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN A.DEPTCD IS NULL THEN '' ELSE A.DEPTCD END) IN (:deptcdList)) \n" +
            "   AND ('' = :reofoCd OR A.REOFO_CD = :reofoCd)                                                 \n" +
            "   AND ('' = :clofpNm OR A.CLOFP_NM LIKE CONCAT('%',:clofpNm,'%'))                              \n" +
            "   AND ('' = :vctnNm OR A.VCTN_NM LIKE CONCAT('%',:vctnNm,'%'))                               \n" +
            "   AND ('' = :deptNm OR B.DEPT_NM LIKE CONCAT('%',:deptNm,'%'))                               \n" +
            "   AND ('' = :empno OR A.EMPNO = :empno OR A.EMP_KRN_NM LIKE CONCAT('%',:empno,'%'))			" +
            " ORDER BY B.BSSMACD, B.DEPTCD, A.REOFO_CD, A.CLOFP_CD, A.VCTN_CD, A.EMPNO, A.EMP_KRN_NM "
            ,countQuery = "SELECT COUNT(*) FROM ( \n" +
            "SELECT DISTINCT B.BSSMACD AS bssmacd				                                                 \n" +
            "     , C.BSS_HQ_NM AS bssmacdNm                                                                 \n" +
            "     , B.DEPTCD AS deptcd                                                                       \n" +
            "     , B.DEPT_NM AS deptNm                                                                      \n" +
            "     , E.CMMN_CD_VAL_NM AS reofoNm                                                              \n" +
            "     , A.EMPNO AS empno                                                                         \n" +
            "     , A.EMP_KRN_NM AS empKrnNm                                                                 \n" +
            "     , A.CLOFP_CD AS clofpCd                                                                    \n" +
            "     , A.CLOFP_NM AS clofpNm                                                                    \n" +
            "     , A.VCTN_NM AS vctnCd                                                                    \n" +
            "     , A.VCTN_NM AS vctnNm                                                                      \n" +
            "     , A.MPHNO AS mphno \n" +
            "     , A.TCOM_ENTCP_DT AS tcomEntcpDt   \n" +
            "  FROM OCO.OCO50100 A                                                                           \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 B ON (A.DEPTCD = B.DEPTCD)                                       \n" +
            "  LEFT OUTER JOIN OCO.OCO50300 C ON (B.BSSMACD = C.BSSMACD)                                     \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 E ON (E.CMMN_CD = 'REOFO_CD' AND E.CMMN_CD_VAL = A.REOFO_CD)     \n" +
            " WHERE ('' = :bssmacd OR B.BSSMACD = :bssmacd)                                                  \n" +
            "   AND ('' = :deptcd OR B.DEPTCD = :deptcd)                                                     \n" +
            "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN A.DEPTCD IS NULL THEN '' ELSE A.DEPTCD END) IN (:deptcdList)) \n" +
            "   AND ('' = :reofoCd OR A.REOFO_CD = :reofoCd)                                                 \n" +
            "   AND ('' = :clofpNm OR A.CLOFP_NM LIKE CONCAT('%',:clofpNm,'%'))                              \n" +
            "   AND ('' = :vctnNm OR A.VCTN_NM LIKE CONCAT('%',:vctnNm,'%'))                               \n" +
            "   AND ('' = :deptNm OR B.DEPT_NM LIKE CONCAT('%',:deptNm,'%'))                               \n" +
            "   AND ('' = :empno OR A.EMPNO = :empno OR A.EMP_KRN_NM LIKE CONCAT('%',:empno,'%'))) A			"
            ,nativeQuery = true)
    Page<UserBasicScrenIDto> searchUserBasicForScren(String bssmacd, String deptcd, String reofoCd, String clofpNm, String vctnNm
            , String empno, String deptNm, String deptcdListYn, List<String> deptcdList, Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<UserBasic> findByEmpno(String empno);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new com.skcc.ra.common.api.dto.domainDto.UserBasicDto(t, k.deptNm,l.bssHqNm) \n" +
                "FROM UserBasic t \n" +
                "LEFT OUTER JOIN Dept k ON t.deptcd = k.deptcd \n" +
                "LEFT OUTER JOIN Bssmacd l ON k.bssmacd = l.bssmacd \n" +
                "WHERE (t.resgDt is null OR t.resgDt = '') \n" +
                "AND t.empKrnNm LIKE CONCAT('%',:userNm,'%') \n" +
                "AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN t.deptcd IS NULL THEN '' ELSE t.deptcd END) IN (:deptcdList)) \n" +
                "AND ('' = :empno OR t.empno = :empno)")
    List<UserBasicDto> searchUserBasic(String userNm, List<String> deptcdList, String deptcdListYn, String empno);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT t FROM UserBasic t " +
                  " WHERE t.deptcd = :deptcd " +
                  "   AND t.reofoCd IN ('200', '116', '112')")
    UserBasic searchTeamLeader(String deptcd);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT t " +
                  "  FROM UserBasic t " +
                  " INNER JOIN Dept a ON (t.deptcd = a.deptcd)" +
                  " INNER JOIN Dept b ON (a.deptcd = b.superDeptcd) " +
                  " WHERE b.deptcd = :deptcd " +
                  "   AND t.reofoCd IN ('200', '116', '112')")
    UserBasic searchSuperTeamLeader(String deptcd);


    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT B.BSSMACD AS bssmacd				                                                 \n" +
            "           , C.BSS_HQ_NM AS bssmacdNm                                                           \n" +
            "           , B.DEPTCD AS deptcd                                                                 \n" +
            "           , B.DEPT_NM AS deptNm                                                                \n" +
            "           , A.CLOFP_NM AS clofpNm                                                              \n" +
            "           , E.CMMN_CD_VAL_NM AS reofoNm                                                        \n" +
            "           , A.EMPNO AS empno                                                                   \n" +
            "           , A.EMP_KRN_NM AS empKrnNm                                                           \n" +
            "           , A.VCTN_NM AS vctnNm                                                                \n" +
            "           , A.MPHNO   AS mphno                                                                 \n" +
            "           , A.TCOM_ENTCP_DT AS tcomEntcpDt                                                     \n" +
            "  FROM OCO.OCO50100 A                                                                           \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 B ON (A.DEPTCD = B.DEPTCD)                                       \n" +
            "  LEFT OUTER JOIN OCO.OCO50300 C ON (B.BSSMACD = C.BSSMACD)                                     \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 E ON (E.CMMN_CD = 'REOFO_CD' AND E.CMMN_CD_VAL = A.REOFO_CD)     \n" +
            " WHERE 1 = 1                                                  \n" +
            "   AND ('' = (CASE WHEN :empnoListYn = 'N' THEN '' END) OR (CASE WHEN A.EMPNO IS NULL THEN '' ELSE A.EMPNO END) IN (:empnoList)) \n" +
            "   AND ('' = (CASE WHEN :empnmListYn = 'N' THEN '' END) OR (CASE WHEN A.EMP_KRN_NM IS NULL THEN '' ELSE A.EMP_KRN_NM END) IN (:empnmList)) \n" +
            "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN A.DEPTCD IS NULL THEN '' ELSE A.DEPTCD END) IN (:deptcdList)) \n" +
            "   AND ('' = :deptNm OR B.DEPT_NM LIKE CONCAT('%',:deptNm,'%'))  \n" +
            "   AND (A.EMP_KRN_NM LIKE CONCAT('%',:empNm,'%') OR ('' = :empNm OR A.EMPNO = :empNm)) \n"
            ,nativeQuery = true)
    List<UserBasicScrenIDto> searchUserList(String empnoListYn, List<String> empnoList, String empnmListYn, List<String> empnmList, String deptcdListYn, List<String> deptcdList, String empNm, String deptNm);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT DISTINCT B.BSSMACD AS bssmacd				                                         \n" +
            "     , C.BSS_HQ_NM AS bssmacdNm                                                                 \n" +
            "     , B.DEPTCD AS deptcd                                                                       \n" +
            "     , B.DEPT_NM AS deptNm                                                                      \n" +
            "     , A.EMPNO AS empno                                                                         \n" +
            "     , A.EMP_KRN_NM AS empKrnNm                                                                 \n" +
            "     , A.REOFO_CD AS reofoCd                                                                    \n" +
            "     , F.CMMN_CD_VAL_NM AS reofoNm                                                              \n" +
            "     , A.CLOFP_CD AS clofpCd                                                                    \n" +
            "     , A.CLOFP_NM AS clofpNm                                                                    \n" +
            "     , A.VCTN_CD AS vctnCd                                                                      \n" +
            "     , A.VCTN_NM AS vctnNm                                                                      \n" +
            "     , A.MPHNO AS mphno                                                                          \n" +
            "  FROM OCO.OCO50100 A                                                                           \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 B ON (A.DEPTCD = B.DEPTCD)                                       \n" +
            "  LEFT OUTER JOIN OCO.OCO50300 C ON (B.BSSMACD = C.BSSMACD)                                     \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 F ON (F.CMMN_CD = 'REOFO_CD' AND F.CMMN_CD_VAL = A.REOFO_CD)     \n" +
            " WHERE A.MPHNO = :telno \n"
            ,nativeQuery = true)
    List<InnerCallInfoIDto> searchUserByTelno(String telno);

}
