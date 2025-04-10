package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.AgentDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.AgentIDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgentService {

    Page<AgentIDto> getAgentList(String userInfo, Pageable pageable);

    void regAgent(AgentDto agentDto);

    void endAgent(List<Integer> agentDto);
}
