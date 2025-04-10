package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.responseDto.ifDto.UserActvyLogIDto;
import com.skcc.ra.account.domain.hist.UserActvyLog;
import com.skcc.ra.account.domain.hist.pk.UserActvyLogPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserActvyLogRepository extends JpaRepository<UserActvyLog, UserActvyLogPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT T.USER_ACTVY_SEQ AS userActvySeq,                  \n"+
            "        DATE_FORMAT(T.USER_ACTVY_DTM, '%Y-%m-%d %H:%i:%s') AS userActvyDtm,   \n"+
            "        E.DEPT_NM AS deptNm,                                    \n"+
            "        D.CLOFP_NM AS clofpNm,                                  \n"+
            "        D.VCTN_NM AS vctnNm,                                    \n"+
            "        T.USERID AS userid,                                     \n"+
            "        T.LAST_CHNGR_ID AS lastChngrId,                         \n"+
            "        T.USER_ACTVY_TYPE_CD AS userActvyTypeCd,                \n"+
            "        T.CONN_IPADDR AS connIpaddr,                            \n"+
            "        B.MENU_ID AS menuId,                                    \n"+
            "        A.SCREN_ID AS screnId,                                  \n"+
            "        T.SYSTM_CTGRY_CD AS systmCtgryCd,                       \n"+
            "        B.MENU_NM AS menuNm,                                    \n"+
            "        A.SCREN_NM AS screnNm,                                  \n"+
            "        I.CMMN_CD_VAL_NM AS systmCtgryNm,                       \n"+
            "        H.CMMN_CD_VAL_NM AS chrgTaskGroupNm,                    \n"+
            "        G.CMMN_CD_VAL_NM AS userActvyTypeNm,                    \n"+
            "        C.USER_NM AS userNm,                                    \n"+
            "        T.DWNLD_RESON_CNTNT AS dwnldResonCntnt                 \n"+
            "  FROM OCO.OCO10190 T                                           \n"+
            "  LEFT OUTER JOIN OCO.OCO10220 A ON (A.SCREN_ID = T.SCREN_ID)   \n"+
            "  LEFT OUTER JOIN OCO.OCO10210 B ON (B.SCREN_ID = A.SCREN_ID)   \n"+
            "  LEFT OUTER JOIN OCO.OCO10100 C ON (C.USERID = T.USERID)       \n"+
            "  LEFT OUTER JOIN OCO.OCO50100 D ON (D.EMPNO = C.USER_IDENT_NO) \n"+
            "  LEFT OUTER JOIN OCO.OCO50200 E ON (E.DEPTCD = C.DEPTCD)       \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 G ON (G.CMMN_CD = 'USER_ACTVY_TYPE_CD' AND G.CMMN_CD_VAL = T.USER_ACTVY_TYPE_CD)  \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 H ON (H.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND H.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 I ON (I.CMMN_CD = 'SYSTM_CTGRY_CD' AND I.CMMN_CD_VAL = T.SYSTM_CTGRY_CD)          \n"+
            " WHERE T.USER_ACTVY_DTM BETWEEN :userActvyDtmFrom AND :userActvyDtmTo                                            \n"+
            "   AND ('' = :systmCtgryCd OR T.SYSTM_CTGRY_CD = :systmCtgryCd)                                                  \n"+
            "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN C.DEPTCD IS NULL THEN '' ELSE C.DEPTCD END) IN (:deptcdList))                   \n"+
            "   AND ('' = :userActvyTypeCd OR T.USER_ACTVY_TYPE_CD = :userActvyTypeCd)                                                                              \n"+
            "   AND ('' = :userNm OR (T.USERID = :userNm OR C.USER_NM LIKE CONCAT('%', :userNm, '%')))                                          \n"+
            " ORDER BY T.USER_ACTVY_DTM DESC	",
            countQuery = "SELECT COUNT(*)                                               \n" +
                    "      FROM OCO.OCO10190 T                                           \n" +
                    "      LEFT OUTER JOIN OCO.OCO10100 C ON (C.USERID = T.USERID)       \n" +
                    "     WHERE T.USER_ACTVY_DTM BETWEEN :userActvyDtmFrom AND :userActvyDtmTo                                            \n" +
                    "       AND ('' = :systmCtgryCd OR T.SYSTM_CTGRY_CD = :systmCtgryCd)                                                  \n" +
                    "       AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN C.DEPTCD IS NULL THEN '' ELSE C.DEPTCD END) IN (:deptcdList))                  \n" +
                    "       AND ('' = :userActvyTypeCd OR T.USER_ACTVY_TYPE_CD = :userActvyTypeCd)                                                                             \n" +
                    "       AND ('' = :userNm OR (T.USERID = :userNm OR C.USER_NM LIKE CONCAT('%', :userNm, '%')))",
            nativeQuery = true)
    Page<UserActvyLogIDto> findUserActvyLogList(@Param("systmCtgryCd") String systmCtgryCd,
                                                @Param("userActvyTypeCd") String userActvyTypeCd,
                                                @Param("deptcdList") List<String> deptcdList,
                                                @Param("deptcdListYn") String deptcdListYn,
                                                @Param("userNm") String userNm,
                                                @Param("userActvyDtmFrom") String userActvyDtmFrom,
                                                @Param("userActvyDtmTo") String userActvyDtmTo,
                                                Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT COUNT(*)                                                  \n" +
              "      FROM OCO.OCO10190 T                                            \n" +
              "      LEFT OUTER JOIN OCO.OCO10100 C ON (C.USERID = T.USERID)        \n" +
              "     WHERE T.USER_ACTVY_DTM BETWEEN :userActvyDtmFrom AND :userActvyDtmTo                                            \n" +
              "       AND ('' = :systmCtgryCd OR T.SYSTM_CTGRY_CD = :systmCtgryCd)                                                  \n" +
              "       AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN C.DEPTCD IS NULL THEN '' ELSE C.DEPTCD END) IN (:deptcdList))                  \n" +
              "       AND ('' = :userActvyTypeCd OR T.USER_ACTVY_TYPE_CD = :userActvyTypeCd)                                                                             \n" +
              "       AND ('' = :userNm OR (T.USERID = :userNm OR C.USER_NM LIKE CONCAT('%', :userNm, '%')))",
            nativeQuery = true)
    int excelDataCount(@Param("systmCtgryCd") String systmCtgryCd,
                       @Param("userActvyTypeCd") String userActvyTypeCd,
                       @Param("deptcdList") List<String> deptcdList,
                       @Param("deptcdListYn") String deptcdListYn,
                       @Param("userNm") String userNm,
                       @Param("userActvyDtmFrom") String userActvyDtmFrom,
                       @Param("userActvyDtmTo") String userActvyDtmTo);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT T.USER_ACTVY_SEQ AS userActvySeq,                  \n"+
            "        DATE_FORMAT(T.USER_ACTVY_DTM, '%Y-%m-%d %H:%i:%s') AS userActvyDtm,   \n"+
            "        E.DEPT_NM AS deptNm,                                    \n"+
            "        D.CLOFP_NM AS clofpNm,                                  \n"+
            "        D.VCTN_NM AS vctnNm,                                    \n"+
            "        T.USERID AS userid,                                     \n"+
            "        T.LAST_CHNGR_ID AS lastChngrId,                         \n"+
            "        T.USER_ACTVY_TYPE_CD AS userActvyTypeCd,                \n"+
            "        T.CONN_IPADDR AS connIpaddr,                            \n"+
            "        B.MENU_ID AS menuId,                                    \n"+
            "        A.SCREN_ID AS screnId,                                  \n"+
            "        T.SYSTM_CTGRY_CD AS systmCtgryCd,                       \n"+
            "        B.MENU_NM AS menuNm,                                    \n"+
            "        A.SCREN_NM AS screnNm,                                  \n"+
            "        I.CMMN_CD_VAL_NM AS systmCtgryNm,                       \n"+
            "        H.CMMN_CD_VAL_NM AS chrgTaskGroupNm,                    \n"+
            "        G.CMMN_CD_VAL_NM AS userActvyTypeNm,                    \n"+
            "        C.USER_NM AS userNm,                                    \n"+
            "        T.DWNLD_RESON_CNTNT AS dwnldResonCntnt                 \n"+
            "  FROM OCO.OCO10190 T                                           \n"+
            "  LEFT OUTER JOIN OCO.OCO10220 A ON (A.SCREN_ID = T.SCREN_ID)   \n"+
            "  LEFT OUTER JOIN OCO.OCO10210 B ON (B.SCREN_ID = A.SCREN_ID)   \n"+
            "  LEFT OUTER JOIN OCO.OCO10100 C ON (C.USERID = T.USERID)       \n"+
            "  LEFT OUTER JOIN OCO.OCO50100 D ON (D.EMPNO = C.USER_IDENT_NO) \n"+
            "  LEFT OUTER JOIN OCO.OCO50200 E ON (E.DEPTCD = C.DEPTCD)       \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 G ON (G.CMMN_CD = 'USER_ACTVY_TYPE_CD' AND G.CMMN_CD_VAL = T.USER_ACTVY_TYPE_CD)  \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 H ON (H.CMMN_CD = 'CHRG_TASK_GROUP_CD' AND H.CMMN_CD_VAL = A.CHRG_TASK_GROUP_CD)  \n"+
            "  LEFT OUTER JOIN OCO.OCO20101 I ON (I.CMMN_CD = 'SYSTM_CTGRY_CD' AND I.CMMN_CD_VAL = T.SYSTM_CTGRY_CD)          \n"+
            " WHERE T.USER_ACTVY_DTM BETWEEN :userActvyDtmFrom AND :userActvyDtmTo                                            \n"+
            "   AND ('' = :systmCtgryCd OR T.SYSTM_CTGRY_CD = :systmCtgryCd)                                                  \n"+
            "   AND ('' = (CASE WHEN :deptcdListYn = 'N' THEN '' END) OR (CASE WHEN C.DEPTCD IS NULL THEN '' ELSE C.DEPTCD END) IN (:deptcdList))                   \n"+
            "   AND ('' = :userActvyTypeCd OR T.USER_ACTVY_TYPE_CD = :userActvyTypeCd)                                                                              \n"+
            "   AND ('' = :userNm OR (T.USERID = :userNm OR C.USER_NM LIKE CONCAT('%', :userNm, '%')))                                          \n"+
            " ORDER BY T.USER_ACTVY_DTM DESC	\n"+
            " LIMIT :offset, :size ",
            nativeQuery = true)
    List<UserActvyLogIDto> findUserActvyLogListForExcel(@Param("systmCtgryCd") String systmCtgryCd,
                                                        @Param("userActvyTypeCd") String userActvyTypeCd,
                                                        @Param("deptcdList") List<String> deptcdList,
                                                        @Param("deptcdListYn") String deptcdListYn,
                                                        @Param("userNm") String userNm,
                                                        @Param("userActvyDtmFrom") String userActvyDtmFrom,
                                                        @Param("userActvyDtmTo") String userActvyDtmTo,
                                                        @Param("offset") int offset,
                                                        @Param("size") int size);
}
