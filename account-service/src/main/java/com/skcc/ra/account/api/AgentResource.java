package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.AgentDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.AgentIDto;
import com.skcc.ra.account.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[권한관리] 대무자 관리(AgentResource)", description = "검토자 부재 시 대무자 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/account/agent")
@Slf4j
public class AgentResource {

    @Autowired
    private AgentService agentService;
    @Operation(summary = "대무자 이력 조회")
    @GetMapping
    public ResponseEntity<Page<AgentIDto>> searchAgent(@RequestParam String userInfo,
                                                       Pageable pageable) {
        return new ResponseEntity<>(agentService.getAgentList(userInfo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "대무자 종료 - 권한 회수")
    @PutMapping
    public ResponseEntity<Void> endAgent(@RequestBody List<Integer> agentRegSeq) {
        agentService.endAgent(agentRegSeq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "대무자 등록, 수정 - 존재여부 기준, 오늘부터 일 경우 권한 부여")
    @PostMapping
    public ResponseEntity<Void> regAgent(@RequestBody AgentDto agentDto) {
        agentService.regAgent(agentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
