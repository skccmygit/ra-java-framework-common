package kr.co.skcc.base.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.base.com.common.api.dto.domainDto.ApiInfoDto;
import kr.co.skcc.base.com.common.domain.apiInfo.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.jpa.HibernateHints.HINT_COMMENT;

@Repository
public interface ApiInfoRepository extends JpaRepository<ApiInfo, Integer> {

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiInfoRepository.queryApiInfoSearch 이제현"))
    @Query(value = "SELECT new kr.co.skcc.base.com.common.api.dto.domainDto.ApiInfoDto(" +
                "ai.apiId, " +
                "ai.aproGroupId, " +
                "ai.apiNm, " +
                "ai.apiDesc, " +
                "ai.apiLocUrladdr, " +
                "ai.httMethodVal, " +
                "ai.apiReqCntnt, " +
                "ai.apiRespCntnt, " +
                "ag.aproTaskClCd, " +
                "ag.aproGroupClNm) " +
            "FROM ApiInfo ai " +
            "LEFT OUTER JOIN AppGroup ag ON ai.aproGroupId = ag. aproGroupId " +
            "WHERE ai.aproGroupId = :aproGroupId " +
            "ORDER BY ai.apiNm")
    List<ApiInfoDto> queryApiInfoSearch(Integer aproGroupId);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiInfoRepository.excelApiInfoSearch 이제현"))
    @Query(value = "SELECT new kr.co.skcc.base.com.common.api.dto.domainDto.ApiInfoDto(" +
            "ai.apiId, " +
            "ai.aproGroupId, " +
            "ag.aproTaskClCd, " +
            "ag.aproGroupClNm, " +
            "ai.apiNm, " +
            "ai.apiDesc, " +
            "ai.httMethodVal, " +
            "ai.apiLocUrladdr) " +
            "FROM ApiInfo ai " +
            "LEFT OUTER JOIN AppGroup ag ON ai.aproGroupId = ag.aproGroupId " +
            "ORDER BY ag.aproTaskClCd, ag.aproGroupClNm, ai.apiNm")
    List<ApiInfoDto> excelApiInfoSearch();

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiInfoRepository.queryApiInfoByCondition 이제현"))
    @Query(value = "SELECT new kr.co.skcc.base.com.common.api.dto.domainDto.ApiInfoDto(" +
                "ai.apiId, " +
                "ai.aproGroupId, " +
                "ai.apiNm, " +
                "ai.apiDesc, " +
                "ai.apiLocUrladdr, " +
                "ai.httMethodVal, " +
                "ai.apiReqCntnt, " +
                "ai.apiRespCntnt, " +
                "ag.aproTaskClCd, " +
                "ag.aproGroupClNm) " +
            "FROM ApiInfo ai " +
            "INNER JOIN AppGroup ag ON ai.aproGroupId = ag. aproGroupId " +
            "WHERE ag.aproTaskClCd LIKE CONCAT('%',:taskClCd,'%') " +
            "AND ag.aproTypeClCd LIKE CONCAT('%',:appType,'%') " +
            "AND (ai.apiNm LIKE CONCAT('%',:apiNmUrladdrDesc,'%') OR ai.apiLocUrladdr LIKE CONCAT('%',:apiNmUrladdrDesc,'%') OR ai.apiDesc LIKE CONCAT('%',:apiNmUrladdrDesc,'%')) " +
            "ORDER BY ai.apiNm")
    List<ApiInfoDto> queryApiInfoByCondition(String taskClCd, String appType, String apiNmUrladdrDesc);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiInfoRepository.existsByApiLocUrladdrAndHttMethodVal 이제현"))
    Boolean existsByApiLocUrladdrAndHttMethodVal(String apiLocURLaddr, String httMethodVal);

    @QueryHints(@QueryHint(name = HINT_COMMENT, value = "ONM ApiInfoRepository.findByApiLocUrladdrAndHttMethodVal 이제현"))
    ApiInfo findByApiLocUrladdrAndHttMethodVal(String apiLocURLaddr, String httMethodVal);

}
