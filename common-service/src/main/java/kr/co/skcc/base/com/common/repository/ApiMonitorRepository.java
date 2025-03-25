package kr.co.skcc.base.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.base.com.common.api.dto.responseDto.ApiMonitorDtlDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.ApiMonitorStatsDto;
import kr.co.skcc.base.com.common.domain.apiInfo.ApiMonitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

@Repository
public interface ApiMonitorRepository extends JpaRepository<ApiMonitor, Long> {

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiMonitorRepository.findByApiIdAndExectDtmtFromGreaterThanEqualAndExectDtmtToLessThanEqual 이제현"))
    @Query(value = "SELECT t " +
                    " FROM ApiMonitor t" +
            " WHERE t.apiId = :apiId " +
            "   AND t.apiExctStartDtmt BETWEEN :exectDtmtFrom AND :exectDtmtTo")
    List<ApiMonitor> findByApiIdAndapiExectUserIdGreaterThanEqualAndapiExectUserIdLessThanEqual(Integer apiId, String exectDtmtFrom, String exectDtmtTo);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiMonitorRepository.queryApiMonitorStatsSearch 이제현"))
    @Query(value =
            "SELECT t1.API_EXECT_DTL_SEQ as apiExectDtlSeq, t1.API_RESP_STS_VAL as apiRespStsVal, t1.API_ID as apiId, " +
                    "t1.API_RESP_TIME as apiRespTime, t1.API_EXECT_START_DTMT as apiExctStartDtmt, " +
                    "t2.APRO_TASK_CL_CD as aproTaskClCd, t2.APRO_GROUP_CL_NM as aproGroupClNm, t2.API_NM as apiNm, t2.API_DESC as apiDesc, " +
                    "t2. API_LOC_URLADDR as apiLocUrladdr, t2.DELAY_CNT as delayCnt, t2.ERR_CNT as errCnt " +
            "FROM " +
                "OCO.OCO40111 t1 "+
            "INNER JOIN " +
                "(SELECT /*+ JOIN_PREFIX(am, ai) */ " +
                    "max(am.API_EXECT_DTL_SEQ) AS API_EXECT_DTL_SEQ, " +
                    "am.API_ID, " +
                    "ag.APRO_TASK_CL_CD, ag.APRO_GROUP_CL_NM, ai.API_NM, ai.API_DESC, ai.API_LOC_URLADDR, " +
                    "count(case when am.API_RESP_TIME > :apiDelayTime then 1 end) as DELAY_CNT, " +
                    "count(case when am.API_RESP_STS_VAL >= 500 then 1 end) as ERR_CNT " +
                "FROM OCO.OCO40111 am " +
                "INNER JOIN OCO.OCO40110 ai on am.API_ID = ai.API_ID " +
                "INNER JOIN OCO.OCO40100 ag on ag.APRO_GROUP_ID = ai.APRO_GROUP_ID " +
                "WHERE ag.APRO_TASK_CL_CD LIKE CONCAT('%',:taskClCd,'%') " +
                "AND ag.APRO_GROUP_CL_NM LIKE CONCAT('%',:aproGroupClNm,'%') " +
                "AND (ai.API_NM LIKE CONCAT('%',:apiNmUrladdr,'%') OR ai.API_LOC_URLADDR LIKE CONCAT('%',:apiNmUrladdr,'%')) " +
                "AND am.API_RESP_TIME > :apiRespTime " +
                "AND am.API_EXECT_START_DTMT >= :exectDtmtFrom AND am.API_EXECT_START_DTMT <= :exectDtmtTo " +
                "AND (am.API_RESP_STS_VAL LIKE CONCAT(:nomalStsVal,'%') OR am.API_RESP_STS_VAL LIKE CONCAT(:delayStsVal,'%') OR am.API_RESP_STS_VAL LIKE CONCAT(:errStsVal,'%')) " +
                "group by am.api_id" +
                ") t2 " +
            "on t1.API_EXECT_DTL_SEQ = t2.API_EXECT_DTL_SEQ " +
            "ORDER BY t1.API_EXECT_START_DTMT DESC",
            countQuery =
                    "SELECT COUNT(*)" +
                    "FROM " +
                        "OCO.OCO40111 t1 "+
                    "INNER JOIN " +
                        "(SELECT /*+ JOIN_PREFIX(am, ai) */ " +
                        "max(am.API_EXECT_DTL_SEQ) AS API_EXECT_DTL_SEQ, " +
                        "am.API_ID, " +
                        "ag.APRO_TASK_CL_CD, ag.APRO_GROUP_CL_NM, ai.API_NM, ai.API_LOC_URLADDR, " +
                        "count(case when am.API_RESP_TIME > :apiDelayTime then 1 end) as DELAY_CNT, " +
                        "count(case when am.API_RESP_STS_VAL >= 500 then 1 end) as ERR_CNT " +
                        "FROM OCO.OCO40111 am " +
                        "INNER JOIN OCO.OCO40110 ai on am.API_ID = ai.API_ID " +
                        "INNER JOIN OCO.OCO40100 ag on ag.APRO_GROUP_ID = ai.APRO_GROUP_ID " +
                        "WHERE ag.APRO_TASK_CL_CD LIKE CONCAT('%',:taskClCd,'%') " +
                        "AND ag.APRO_GROUP_CL_NM LIKE CONCAT('%',:aproGroupClNm,'%') " +
                        "AND (ai.API_NM LIKE CONCAT('%',:apiNmUrladdr,'%') OR ai.API_LOC_URLADDR LIKE CONCAT('%',:apiNmUrladdr,'%')) " +
                        "AND am.API_RESP_TIME > :apiRespTime " +
                        "AND am.API_EXECT_START_DTMT >= :exectDtmtFrom AND am.API_EXECT_START_DTMT <= :exectDtmtTo " +
                        "AND (am.API_RESP_STS_VAL LIKE CONCAT(:nomalStsVal,'%') OR am.API_RESP_STS_VAL LIKE CONCAT(:delayStsVal,'%') OR am.API_RESP_STS_VAL LIKE CONCAT(:errStsVal,'%')) " +
                        "group by am.api_id" +
                        ") t2 " +
                    "on t1.API_EXECT_DTL_SEQ = t2.API_EXECT_DTL_SEQ " +
                    "ORDER BY t1.API_EXECT_START_DTMT DESC",
            nativeQuery = true)
    Page<ApiMonitorStatsDto> queryApiMonitorStatsSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, Integer apiRespTime, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo, String nomalStsVal, String delayStsVal, String errStsVal, Integer apiDelayTime, Pageable pageable);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiMonitorRepository.queryApiMonitorInfoByCondition 이제현"))
    @Query(value =
            "SELECT new kr.co.skcc.base.com.common.api.dto.responseDto.ApiMonitorDtlDto(" +
                    "am.apiExectDtlSeq, " +
                    "ag.aproTaskClCd, " +
                    "ag.aproGroupId, " +
                    "ag.aproGroupClNm, " +
                    "am.apiExctStartDtmt, " +
                    "ai.apiId, " +
                    "ai.apiNm, " +
                    "ai.apiLocUrladdr, " +
                    "ai.httMethodVal, " +
                    "am.apiExectUserId) " +
            "FROM ApiInfo ai " +
            "INNER JOIN ApiMonitor am " +
                "ON ai.apiId IN :apiIds " +
                "AND am.apiExctStartDtmt BETWEEN :exectDtmtFrom AND :exectDtmtTo " +
                "AND am.apiExectUserId = :userId " +
                "AND ai.apiId = am.apiId " +
            "INNER JOIN AppGroup ag ON ai.aproGroupId = ag.aproGroupId " +
            "ORDER BY am.apiExctStartDtmt")
    List<ApiMonitorDtlDto> queryApiMonitorInfoByCondition(String userId, LocalDateTime exectDtmtFrom, LocalDateTime exectDtmtTo, List<Integer> apiIds);

}
