package com.skcc.ra.common.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.common.api.dto.responseDto.ApiUseTrendDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiUseHist2Dto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ApiUseTaskStatsDto;
import com.skcc.ra.common.domain.apiInfo.ApiMonitor;
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
public interface ApiUseRepository extends JpaRepository<ApiMonitor, Long> {

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiUseRepository.queryApiUseTaskStatsSearch 이제현"))
    @Query(value =
            "SELECT row_number() over() as rownum, t1.aproTaskClCd as aproTaskClCd, t1.aproGroupId as aproGroupId, " +
                    "t1.aproGroupClNm as aproGroupClNm, t1.groupCallCnt, " +
                    "format(t1.groupCallCnt/t2.taskTotalCnt * 100 ,2) as groupCallRate, " +
                    "format(t1.groupCallCnt/sum(groupCallCnt) over() * 100 ,2) as totalCallRate " +
                    "FROM " +
                    "(SELECT ag.apro_Task_Cl_Cd as aproTaskClCd, ag.apro_Group_Id as aproGroupId, " +
                    "ag.apro_Group_Cl_Nm as aproGroupClNm, SUM(am.API_EXECT_CCNT) as groupCallCnt " +
                    "FROM OCO.OCO40112 am " +
                    "INNER JOIN OCO.OCO40110 ai ON am.api_Id = ai.api_Id " +
                    "INNER JOIN OCO.OCO40100 ag ON ai.apro_Group_Id = ag.apro_Group_Id " +
                    "WHERE am.API_EXECT_DT >= :useDtmtFrom " +
                    "AND am.API_EXECT_DT <= :useDtmtTo " +
                    "GROUP BY ag.apro_Task_Cl_Cd, ag.apro_Group_Id) t1 " +
                    "INNER JOIN (" +
                    "SELECT ag.apro_Task_Cl_Cd as aproTaskClCd, SUM(am.API_EXECT_CCNT) as taskTotalCnt " +
                    "FROM OCO.OCO40112 am " +
                    "INNER JOIN OCO.OCO40110 ai ON am.api_Id = ai.api_Id " +
                    "INNER JOIN OCO.OCO40100 ag ON ai.apro_Group_Id = ag.apro_Group_Id " +
                    "WHERE (am.API_EXECT_DT BETWEEN :useDtmtFrom AND :useDtmtTo) " +
                    "GROUP BY ag.apro_Task_Cl_Cd) t2 on t1.aproTaskClCd = t2.aproTaskClCd " +
                    "ORDER BY groupCallCnt DESC, aproTaskClCd ",
            nativeQuery = true)
    List<ApiUseTaskStatsDto> queryApiUseTaskStatsDaily(String useDtmtFrom, String useDtmtTo);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiUseRepository.queryApiUseTrendsdDaily 이제현"))
    @Query(value =
            "SELECT new com.skcc.ra.common.api.dto.responseDto.ApiUseTrendDto(ag.aproGroupId, ag.aproGroupClNm, " +
                    "MAX(am.apiExectDt), " +
                    "SUM(am.apiExectCcnt)) " +
                    "FROM ApiStat am " +
                    "INNER JOIN ApiInfo ai ON am.apiId = ai.apiId " +
                    "INNER JOIN AppGroup ag ON ai.aproGroupId = ag.aproGroupId " +
                    "WHERE ag.aproGroupId IN (:aproGroupId) " +
                    "AND am.apiExectDt >= :useDtmtFrom " +
                    "AND am.apiExectDt <= :useDtmtTo " +
                    "GROUP BY ag.aproTaskClCd, ag.aproGroupId, " +
                    "DATE_FORMAT(STR_TO_DATE(am.apiExectDt, '%Y%m%d'),'%m/%d') " +
                    "ORDER BY 3")
    List<ApiUseTrendDto> queryApiUseTrendsdDaily(List<Integer> aproGroupId, String useDtmtFrom, String useDtmtTo);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiUseRepository.queryApiUseHistSearch 이제현"))
    @Query(value = "select /*+ JOIN_PREFIX(apimonitor0_) */ " +
            "    appgroup2_.APRO_TASK_CL_CD as aproTaskClCd, " +
            "    appgroup2_.APRO_GROUP_ID as aproGroupId, " +
            "    appgroup2_.APRO_GROUP_CL_NM as aproGroupClNm, " +
            "    apiinfo1_.API_ID as apiId, " +
            "    apiinfo1_.API_NM as apiNm, " +
            "    apiinfo1_.API_DESC as apiDesc, " +
            "    apiinfo1_.API_LOC_URLADDR as apiLocUrladdr, " +
            "    apiinfo1_.HTT_METHOD_VAL as httMethodVal, " +
            "    count(apiinfo1_.API_ID) as apiCallCnt  " +
            "from " +
            "    OCO.OCO40111 apimonitor0_  " +
            "inner join " +
            "    OCO.OCO40110 apiinfo1_  " +
            "        on ( " +
            "            apimonitor0_.API_ID=apiinfo1_.API_ID " +
            "        )  " +
            "inner join " +
            "    OCO.OCO40100 appgroup2_  " +
            "        on ( " +
            "            apiinfo1_.APRO_GROUP_ID=appgroup2_.APRO_GROUP_ID " +
            "        )  " +
            "where " +
            "    ( " +
            "        appgroup2_.APRO_TASK_CL_CD like concat('%', :taskClCd, '%') " +
            "    )  " +
            "    and ( " +
            "        appgroup2_.APRO_GROUP_CL_NM like concat('%', :aproGroupClNm, '%') " +
            "    )  " +
            "    and ( " +
            "        apiinfo1_.API_NM like concat('%', :apiNmUrladdr, '%')  " +
            "        or apiinfo1_.API_LOC_URLADDR like concat('%', :apiNmUrladdr, '%') " +
            "    )  " +
            "    and apimonitor0_.API_EXECT_START_DTMT>= :useDtmtFrom " +
            "    and apimonitor0_.API_EXECT_START_DTMT<= :useDtmtTo " +
            "    and ( " +
            "        apiinfo1_.HTT_METHOD_VAL like concat('%', :httMethodVal, '%') " +
            "    )  " +
            "    and ( " +
            "        apimonitor0_.API_RESP_STS_VAL between 200 and 299 " +
            "    )  " +
            "group by " +
            "    appgroup2_.APRO_TASK_CL_CD , " +
            "    appgroup2_.APRO_GROUP_ID , " +
            "    apiinfo1_.API_ID  " +
            "order by " +
            "    apiCallCnt DESC ",
            countQuery = "select count(*) from (select /*+ JOIN_PREFIX(apimonitor0_) */ 1 " +
                    "from " +
                    "    OCO.OCO40111 apimonitor0_  " +
                    "inner join " +
                    "    OCO.OCO40110 apiinfo1_  " +
                    "        on ( " +
                    "            apimonitor0_.API_ID=apiinfo1_.API_ID " +
                    "        )  " +
                    "inner join " +
                    "    OCO.OCO40100 appgroup2_  " +
                    "        on ( " +
                    "            apiinfo1_.APRO_GROUP_ID=appgroup2_.APRO_GROUP_ID " +
                    "        )  " +
                    "where " +
                    "    ( " +
                    "        appgroup2_.APRO_TASK_CL_CD like concat('%', :taskClCd, '%') " +
                    "    )  " +
                    "    and ( " +
                    "        appgroup2_.APRO_GROUP_CL_NM like concat('%', :aproGroupClNm, '%') " +
                    "    )  " +
                    "    and ( " +
                    "        apiinfo1_.API_NM like concat('%', :apiNmUrladdr, '%')  " +
                    "        or apiinfo1_.API_LOC_URLADDR like concat('%', :apiNmUrladdr, '%') " +
                    "    )  " +
                    "    and apimonitor0_.API_EXECT_START_DTMT>= :useDtmtFrom " +
                    "    and apimonitor0_.API_EXECT_START_DTMT<= :useDtmtTo " +
                    "    and ( " +
                    "        apiinfo1_.HTT_METHOD_VAL like concat('%', :httMethodVal, '%') " +
                    "    )  " +
                    "    and ( " +
                    "        apimonitor0_.API_RESP_STS_VAL between 200 and 299 " +
                    "    ) " +
                    "group by " +
                    "    appgroup2_.APRO_TASK_CL_CD , " +
                    "    appgroup2_.APRO_GROUP_ID , " +
                    "    apiinfo1_.API_ID ) a ",
            nativeQuery = true
    )
    Page<ApiUseHist2Dto> queryApiUseHistSearch(String taskClCd, String aproGroupClNm, String apiNmUrladdr, LocalDateTime useDtmtFrom, LocalDateTime useDtmtTo, String httMethodVal, Pageable pageable);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiUseRepository.queryApiUseHistSearchCnt 이제현"))
    @Query(value = "select /*+ JOIN_PREFIX(apimonitor0_) */ " +
            "    count(*) as totCnt " +
            "from " +
            "    OCO.OCO40111 apimonitor0_  " +
            "inner join " +
            "    OCO.OCO40110 apiinfo1_  " +
            "        on ( " +
            "            apimonitor0_.API_ID=apiinfo1_.API_ID " +
            "        )  " +
            "inner join " +
            "    OCO.OCO40100 appgroup2_  " +
            "        on ( " +
            "            apiinfo1_.APRO_GROUP_ID=appgroup2_.APRO_GROUP_ID " +
            "        )  " +
            "where " +
            "    ( " +
            "        appgroup2_.APRO_TASK_CL_CD like concat('%', :taskClCd, '%') " +
            "    )  " +
            "    and ( " +
            "        appgroup2_.APRO_GROUP_CL_NM like concat('%', :aproGroupClNm, '%') " +
            "    )  " +
            "    and ( " +
            "        apiinfo1_.API_NM like concat('%', :apiNmUrladdr, '%')  " +
            "        or apiinfo1_.API_LOC_URLADDR like concat('%', :apiNmUrladdr, '%') " +
            "    )  " +
            "    and apimonitor0_.API_EXECT_START_DTMT >= :useDtmtFrom " +
            "    and apimonitor0_.API_EXECT_START_DTMT <= :useDtmtTo " +
            "    and ( " +
            "        apiinfo1_.HTT_METHOD_VAL like concat('%', :httMethodVal, '%') " +
            "    )  " +
            "    and ( " +
            "        apimonitor0_.API_RESP_STS_VAL between 200 and 299 " +
            "    )",
            nativeQuery = true)
    Long queryApiUseHistSearchCnt(String taskClCd, String aproGroupClNm, String apiNmUrladdr, LocalDateTime useDtmtFrom, LocalDateTime useDtmtTo, String httMethodVal);
}
