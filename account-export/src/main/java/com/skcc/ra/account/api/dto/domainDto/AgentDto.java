package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.auth.Agent;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AgentDto", description = "대무자등록내역")
public class AgentDto implements Entitiable<Agent> {

    @Schema(description = "대무자등록SEQ")
    private Integer agentRegSeq;

    @Schema(description = "사용자부서코드")
    private String userDeptcd;

    @Schema(description = "사용자부서명")
    private String userDeptNm;

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "사용자이름")
    private String userNm;

    @Schema(description = "대무자부서코드")
    private String agentDeptcd;

    @Schema(description = "대무자부서명")
    private String agentDeptNm;

    @Schema(description = "대무자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String agentId;

    @Schema(description = "대무자이름")
    private String agentNm;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "대무시작일자", requiredMode = Schema.RequiredMode.REQUIRED)
    private String agentStartDt;

    @Schema(description = "대무종료예정일자", requiredMode = Schema.RequiredMode.REQUIRED)
    private String agentEndSchdlDt;

    @Schema(description = "대무종료일자")
    private String agentEndDt;

    @Schema(description = "종료여부")
    private String endYn;

    @Schema(description = "대무자등록사유")
    private String agentRegResonCntnt;

    public Agent toEntity() {
        Agent agent = new Agent();
        BeanUtils.copyProperties(this, agent);
        return agent;
    }
}
