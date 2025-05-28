package com.skcc.ra.common.repository;

import com.skcc.ra.common.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.Map;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value=  "SELECT DISTINCT A.ANNCE_NO AS annceNo,                            \n" +
            "             A.ANNCE_TITLE_NM AS annceTitleNm,                 \n" +
            "             '' AS annceCntnt,                      \n" +
            "             A.ANNCE_TASK_CL_CD AS annceTaskClCd,              \n" +
            "             C.CMMN_CD_VAL_NM AS annceTaskClNm,                \n" +
            "             A.ANNCE_CTGR_CL_CD AS annceCtgrClCd,              \n" +
            "             D.CMMN_CD_VAL_NM AS annceCtgrClNm,                \n" +
            "             A.ANNCE_START_DTM AS annceStartDtm,               \n" +
            "             A.ANNCE_END_DTM AS annceEndDtm,                   \n" +
            "             A.SUPER_ANNCE_START_DTM AS superAnnceStartDtm,    \n" +
            "             A.SUPER_ANNCE_END_DTM AS superAnnceEndDtm,        \n" +
            "             A.REG_USERID AS regUserid,                        \n" +
            "             B.USER_NM AS regUserNm,                           \n" +
            "             DATE_FORMAT(A.REG_DTMD, '%Y-%m-%d %H:%i:%s') AS regDtmd,                            \n" +
            "             CASE WHEN (DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM) = 0 THEN 'Y' \n" +
            "                  ELSE 'N' END AS expirationYn,                              \n" +
            "             A.DEL_YN AS delYn,                                \n" +
            "             IFNULL(E.CNT,0) AS cnt,                           \n" +
            "             CASE WHEN (DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') BETWEEN A.SUPER_ANNCE_START_DTM AND A.SUPER_ANNCE_END_DTM) = 1 THEN 0 \n" +
            "                  ELSE 1 END AS seqNum,                              \n" +
            "             DATE_FORMAT(A.LAST_CHNG_DTMD, '%Y-%m-%d %H:%i:%s') AS lastChngDtmd   \n" +
            "        FROM OCO.OCO30100 A                                          \n" +
            "        LEFT OUTER JOIN OCO.OCO10100 B ON (A.REG_USERID = B.USERID)    \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'ANNCE_TASK_CL_CD' AND C.CMMN_CD_VAL = A.ANNCE_TASK_CL_CD)    \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'ANNCE_CTGR_CL_CD' AND D.CMMN_CD_VAL = A.ANNCE_CTGR_CL_CD)    \n" +
            "        LEFT OUTER JOIN (SELECT ANNCE_NO, COUNT(*) AS CNT FROM OCO.OCO30101 GROUP BY ANNCE_NO) E ON (E.ANNCE_NO = A.ANNCE_NO)\n" +
            "        INNER JOIN OCO.OCO30102 G ON (A.ANNCE_NO = G.ANNCE_NO) \n" +
            "       WHERE ('' = :annceTaskClCd OR A.ANNCE_TASK_CL_CD = :annceTaskClCd)  \n" +
            "         AND ('' = :annceCtgrClCd OR A.ANNCE_CTGR_CL_CD = :annceCtgrClCd)  \n" +
            "         AND (A.ANNCE_TITLE_NM LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%') " +
            "               OR A.ANNCE_CNTNT LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%'))  \n" +
            "         AND A.REG_DTMD BETWEEN :regDtmdFromL AND :regDtmdToL  \n" +
            "         AND (A.REG_USERID = :loginUserid OR G.ANNCE_OBJ_DEPT_TEAM_CD IN (SELECT DEPTCD FROM OCO.OCO10100 WHERE USERID = :loginUserid \n" +
            "                                                                           UNION ALL \n" +
            "                                                                          SELECT CUCEN_TEAM_CD FROM OCO.OCO10100 WHERE USERID = :loginUserid))\n" +
            "         AND ('Y' = :delYn OR A.DEL_YN = :delYn)                                            \n" +
            "         AND ('Y' = :expirationYn OR DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM)       \n" +
            "        ORDER BY seqNum, annceNo DESC "
            ,countQuery = "" +
            "      SELECT COUNT(*)                                                  \n" +
            "        FROM (SELECT DISTINCT A.* FROM OCO.OCO30100 A                                            \n" +
            "                LEFT OUTER JOIN OCO.OCO10100 B ON (A.REG_USERID = B.USERID)    \n" +
            "                LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'ANNCE_TASK_CL_CD' AND C.CMMN_CD_VAL = A.ANNCE_TASK_CL_CD)    \n" +
            "                LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'ANNCE_CTGR_CL_CD' AND D.CMMN_CD_VAL = A.ANNCE_CTGR_CL_CD)    \n" +
            "                LEFT OUTER JOIN (SELECT ANNCE_NO, COUNT(*) AS CNT FROM OCO.OCO30101 GROUP BY ANNCE_NO) E ON (E.ANNCE_NO = A.ANNCE_NO)\n" +
            "               INNER JOIN OCO.OCO30102 G ON (A.ANNCE_NO = G.ANNCE_NO)          \n" +
            "               WHERE ('' = :annceTaskClCd OR A.ANNCE_TASK_CL_CD = :annceTaskClCd)          \n" +
            "                 AND ('' = :annceCtgrClCd OR A.ANNCE_CTGR_CL_CD = :annceCtgrClCd)                      \n" +
            "                 AND (A.ANNCE_TITLE_NM LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%') " +
            "                      OR A.ANNCE_CNTNT LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%'))  \n" +
            "                 AND A.REG_DTMD BETWEEN :regDtmdFromL AND :regDtmdToL              \n" +
            "                 AND (A.REG_USERID = :loginUserid OR G.ANNCE_OBJ_DEPT_TEAM_CD IN (SELECT DEPTCD FROM OCO.OCO10100 WHERE USERID = :loginUserid \n" +
            "                                                                                   UNION ALL \n" +
            "                                                                                  SELECT CUCEN_TEAM_CD FROM OCO.OCO10100 WHERE USERID = :loginUserid))\n" +
            "                 AND ('Y' = :delYn OR A.DEL_YN = :delYn)                                            \n" +
            "                 AND ('Y' = :expirationYn OR DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM)) A       \n"
            ,nativeQuery = true)
    Page<Map> searchBoardByAuthCondition(@Param("annceTaskClCd") String annceTaskClCd,
                                              @Param("annceCtgrClCd") String annceCtgrClCd,
                                              @Param("annceTitleNmOrAnnceCntnt") String annceTitleNmOrAnnceCntnt,
                                              @Param("regDtmdFromL") LocalDateTime regDtmdFromL,
                                              @Param("regDtmdToL") LocalDateTime regDtmdToL,
                                              @Param("loginUserid") String loginUserid,
                                              @Param("expirationYn") String expirationYn,
                                               @Param("delYn") String delYn,
                                              Pageable pageable);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT A.ANNCE_NO AS annceNo,                            \n" +
            "             A.ANNCE_TITLE_NM AS annceTitleNm,                 \n" +
            "             '' AS annceCntnt,                                 \n" +
            "             A.ANNCE_TASK_CL_CD AS annceTaskClCd,              \n" +
            "             C.CMMN_CD_VAL_NM AS annceTaskClNm,                \n" +
            "             A.ANNCE_CTGR_CL_CD AS annceCtgrClCd,              \n" +
            "             D.CMMN_CD_VAL_NM AS annceCtgrClNm,                \n" +
            "             A.ANNCE_START_DTM AS annceStartDtm,               \n" +
            "             A.ANNCE_END_DTM AS annceEndDtm,                   \n" +
            "             A.SUPER_ANNCE_START_DTM AS superAnnceStartDtm,    \n" +
            "             A.SUPER_ANNCE_END_DTM AS superAnnceEndDtm,        \n" +
            "             A.REG_USERID AS regUserid,                        \n" +
            "             B.USER_NM AS regUserNm,                           \n" +
            "             DATE_FORMAT(A.REG_DTMD, '%Y-%m-%d %H:%i:%s') AS regDtmd,                            \n" +
            "             CASE WHEN (DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM) = 0 THEN 'Y' \n" +
            "                  ELSE 'N' END AS expirationYn,                              \n" +
            "             A.DEL_YN AS delYn,                                \n" +
            "             IFNULL(E.CNT,0) AS cnt,                           \n" +
            "             CASE WHEN (DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') BETWEEN A.SUPER_ANNCE_START_DTM AND A.SUPER_ANNCE_END_DTM) = 1 THEN 0 \n" +
            "                  ELSE 1 END AS seqNum,                              \n" +
            "             DATE_FORMAT(A.LAST_CHNG_DTMD, '%Y-%m-%d %H:%i:%s') AS lastChngDtmd                        \n" +
            "        FROM OCO.OCO30100 A                                          \n" +
            "        LEFT OUTER JOIN OCO.OCO10100 B ON (A.REG_USERID = B.USERID)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'ANNCE_TASK_CL_CD' AND C.CMMN_CD_VAL = A.ANNCE_TASK_CL_CD)    \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'ANNCE_CTGR_CL_CD' AND D.CMMN_CD_VAL = A.ANNCE_CTGR_CL_CD)    \n" +
            "        LEFT OUTER JOIN (SELECT ANNCE_NO, COUNT(*) AS CNT FROM OCO.OCO30101 GROUP BY ANNCE_NO) E ON (E.ANNCE_NO = A.ANNCE_NO)\n" +
            "       WHERE ('' = :annceTaskClCd OR A.ANNCE_TASK_CL_CD = :annceTaskClCd)  \n" +
            "         AND ('' = :annceCtgrClCd OR A.ANNCE_CTGR_CL_CD = :annceCtgrClCd)  \n" +
            "         AND (A.ANNCE_TITLE_NM LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%') " +
            "               OR A.ANNCE_CNTNT LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%'))  \n" +
            "         AND A.REG_DTMD BETWEEN :regDtmdFromL AND :regDtmdToL  \n" +
            "         AND ('Y' = :delYn OR A.DEL_YN = :delYn)                                            \n" +
            "         AND ('Y' = :expirationYn OR DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM)       \n" +
            "        ORDER BY seqNum, annceNo DESC "
            ,countQuery = "" +
            "      SELECT COUNT(*)                                               \n" +
            "        FROM OCO.OCO30100 A                                          \n" +
            "        LEFT OUTER JOIN OCO.OCO10100 B ON (A.REG_USERID = B.USERID)  \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'ANNCE_TASK_CL_CD' AND C.CMMN_CD_VAL = A.ANNCE_TASK_CL_CD)    \n" +
            "        LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'ANNCE_CTGR_CL_CD' AND D.CMMN_CD_VAL = A.ANNCE_CTGR_CL_CD)    \n" +
            "        LEFT OUTER JOIN (SELECT ANNCE_NO, COUNT(*) AS CNT FROM OCO.OCO30101 GROUP BY ANNCE_NO) E ON (E.ANNCE_NO = A.ANNCE_NO)\n" +
            "       WHERE ('' = :annceTaskClCd OR A.ANNCE_TASK_CL_CD = :annceTaskClCd)  \n" +
            "         AND ('' = :annceCtgrClCd OR A.ANNCE_CTGR_CL_CD = :annceCtgrClCd)  \n" +
            "         AND (A.ANNCE_TITLE_NM LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%') " +
            "               OR A.ANNCE_CNTNT LIKE CONCAT('%',:annceTitleNmOrAnnceCntnt,'%'))  \n" +
            "         AND A.REG_DTMD BETWEEN :regDtmdFromL AND :regDtmdToL  \n" +
            "         AND ('Y' = :delYn OR A.DEL_YN = :delYn)                                            \n" +
            "         AND ('Y' = :expirationYn OR DATE_FORMAT(NOW(),'%Y%m%d%H%i%s') BETWEEN A.ANNCE_START_DTM AND A.ANNCE_END_DTM)       \n"
            ,nativeQuery = true)
    Page<Map> searchBoardByCondition(@Param("annceTaskClCd") String annceTaskClCd,
                                     @Param("annceCtgrClCd") String annceCtgrClCd,
                                     @Param("annceTitleNmOrAnnceCntnt") String annceTitleNmOrAnnceCntnt,
                                     @Param("regDtmdFromL") LocalDateTime regDtmdFromL,
                                     @Param("regDtmdToL") LocalDateTime regDtmdToL,
                                     @Param("expirationYn") String expirationYn,
                                     @Param("delYn") String delYn,
                                     Pageable pageable);

    @Query(value= "SELECT A.ANNCE_NO AS annceNo,                            \n" +
            "             A.ANNCE_TITLE_NM AS annceTitleNm,                 \n" +
            "             A.ANNCE_CNTNT AS annceCntnt,                      \n" +
            "             A.ANNCE_TASK_CL_CD AS annceTaskClCd,              \n" +
            "             C.CMMN_CD_VAL_NM AS annceTaskClNm,                \n" +
            "             A.ANNCE_CTGR_CL_CD AS annceCtgrClCd,              \n" +
            "             D.CMMN_CD_VAL_NM AS annceCtgrClNm,                \n" +
            "             A.ANNCE_START_DTM AS annceStartDtm,               \n" +
            "             A.ANNCE_END_DTM AS annceEndDtm,                   \n" +
            "             A.SUPER_ANNCE_START_DTM AS superAnnceStartDtm,    \n" +
            "             A.SUPER_ANNCE_END_DTM AS superAnnceEndDtm,        \n" +
            "             A.REG_USERID AS regUserid,                        \n" +
            "             B.USER_NM AS regUserNm,                           \n" +
            "             DATE_FORMAT(A.REG_DTMD, '%Y-%m-%d %H:%i:%s') AS regDtmd,                            \n" +
            "             A.DEL_YN AS delYn,                                \n" +
            "             IFNULL(E.CNT,0) AS cnt,                           \n" +
            "             CASE WHEN (DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') BETWEEN A.SUPER_ANNCE_START_DTM AND A.SUPER_ANNCE_END_DTM) = 1 THEN 0 \n" +
            "                  ELSE 1 END AS seqNum,                        \n" +
            "             DATE_FORMAT(A.LAST_CHNG_DTMD, '%Y-%m-%d %H:%i:%s') AS lastChngDtmd                  \n" +
            "  FROM OCO.OCO30100 A                                          \n" +
            "  LEFT OUTER JOIN OCO.OCO10100 B ON (A.REG_USERID = B.USERID)  \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 C ON (C.CMMN_CD = 'ANNCE_TASK_CL_CD' AND C.CMMN_CD_VAL = A.ANNCE_TASK_CL_CD)    \n" +
            "  LEFT OUTER JOIN OCO.OCO20101 D ON (D.CMMN_CD = 'ANNCE_CTGR_CL_CD' AND D.CMMN_CD_VAL = A.ANNCE_CTGR_CL_CD)    \n" +
            "  LEFT OUTER JOIN (SELECT ANNCE_NO, COUNT(*) AS CNT FROM OCO.OCO30101 GROUP BY ANNCE_NO) E ON (E.ANNCE_NO = A.ANNCE_NO) \n" +
            " WHERE A.ANNCE_NO = :annceNo ", nativeQuery = true )
    Map searchBoardByAnnceNo(@Param("annceNo") Long annceNo);
    }
