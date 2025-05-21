package com.skcc.ra.account.domain.auth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.AgentDto;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OCO10133", catalog = "OCO")
public class Agent extends BaseEntity implements Apiable<AgentDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AGENT_REG_SEQ", nullable = false)
    private Integer agentRegSeq;

    @Column(name = "USERID", length = 10)
    private String userid;

    @Column(name = "AGENT_ID", length = 10)
    private String agentId;

    @Column(name = "DEPTCD", length = 6)
    private String deptcd;

    @Column(name = "AGENT_START_DT", length = 8)
    private String agentStartDt;

    @Column(name = "AGENT_END_SCHDL_DT", length = 8)
    private String agentEndSchdlDt;

    @Column(name = "AGENT_END_DT", length = 8)
    private String agentEndDt;

    @Column(name = "END_YN", length = 1)
    private String endYn;

    @Column(name = "AGENT_REG_RESON_CNTNT", length = 4000)
    private String agentRegResonCntnt;

    @Override
    public AgentDto toApi() {
        AgentDto agentDto = new AgentDto();
        BeanUtils.copyProperties(this, agentDto);
        return agentDto;
    }
}
