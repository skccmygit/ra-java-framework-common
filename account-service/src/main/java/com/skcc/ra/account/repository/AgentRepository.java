package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.responseDto.ifDto.AgentIDto;
import com.skcc.ra.account.domain.auth.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface AgentRepository  extends JpaRepository<Agent, Integer> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<Agent> findByAgentRegSeqNotAndUseridAndEndYn(Integer agentRegSeq, String userid, String endYn);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<Agent> findByAgentIdAndEndYn(String agentId, String endYn);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value =  "SELECT A.AGENT_REG_SEQ AS agentRegSeq          \n" +
            "      ,B.DEPTCD AS userDeptcd                  \n" +
            "      ,E.DEPT_NM AS userDeptNm                 \n" +
            "      ,A.USERID AS userid                      \n" +
            "      ,B.USER_NM AS userNm                     \n" +
            "      ,C.DEPTCD AS agentDeptcd                 \n" +
            "      ,F.DEPT_NM AS agentDeptNm                \n" +
            "      ,A.AGENT_ID AS agentId                   \n" +
            "      ,C.USER_NM AS agentNm                    \n" +
            "      ,A.DEPTCD AS deptcd                      \n" +
            "      ,D.DEPT_NM AS deptNm                     \n" +
            "      ,A.AGENT_START_DT AS agentStartDt        \n" +
            "      ,A.AGENT_END_SCHDL_DT AS agentEndSchdlDt \n" +
            "      ,A.AGENT_END_DT AS agentEndDt            \n" +
            "      ,A.END_YN AS endYn                       \n" +
            "      ,A.AGENT_REG_RESON_CNTNT AS agentRegResonCntnt \n" +
            "  FROM OCO.OCO10133 A                                          \n" +
            "  LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID)      \n" +
            "  LEFT OUTER JOIN OCO.OCO10100 C ON (C.USERID = A.AGENT_ID)    \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 D ON (D.DEPTCD = A.DEPTCD)      \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 E ON (E.DEPTCD = B.DEPTCD)      \n" +
            "  LEFT OUTER JOIN OCO.OCO50200 F ON (F.DEPTCD = C.DEPTCD)      \n" +
            " WHERE ( '' = :userInfo OR A.USERID = :userInfo OR B.USER_NM LIKE CONCAT('%',:userInfo,'%'))                \n" +
            " ORDER BY A.AGENT_START_DT DESC, A.AGENT_END_SCHDL_DT DESC, A.AGENT_END_DT DESC, A.END_YN, A.AGENT_REG_SEQ",
            countQuery = "SELECT COUNT(*) \n" +
                    "       FROM OCO.OCO10133 A                                          \n" +
                    "       LEFT OUTER JOIN OCO.OCO10100 B ON (B.USERID = A.USERID)      \n" +
                    "      WHERE ( '' = :userInfo OR A.USERID = :userInfo OR B.USER_NM LIKE CONCAT('%',:userInfo,'%'))   \n",
            nativeQuery = true)
    Page<AgentIDto> findAgentList(String userInfo, Pageable pageable);
}
