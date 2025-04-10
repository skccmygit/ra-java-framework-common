package com.skcc.ra.account.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.dto.domainDto.AgentDto;
import com.skcc.ra.account.api.dto.responseDto.ifDto.AgentIDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.domain.auth.Agent;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.account.repository.AccountRepository;
import com.skcc.ra.account.repository.AgentRepository;
import com.skcc.ra.account.repository.UserRoleHistRepository;
import com.skcc.ra.account.repository.UserRoleRepository;
import com.skcc.ra.account.service.AgentService;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AgentServiceImpl implements AgentService {

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserRoleHistRepository userRoleHistRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Page<AgentIDto> getAgentList(String userInfo, Pageable pageable) {

        List<String> roleList = RequestUtil.getLoginUserRoleList();
        if(roleList == null) throw new ServiceException("COM.I1016");

        String searchUserInfo = RequestUtil.getLoginUserid();
        if(roleList.contains(AuthType.AUTH_APPROVE.getCode())){
            searchUserInfo = StringUtils.isBlank(userInfo) ? "" : userInfo;
        }
        return agentRepository.findAgentList(searchUserInfo, pageable);
    }

    @Override
    public void regAgent(AgentDto agentDto){

        List<String> roleList = RequestUtil.getLoginUserRoleList();
        if(roleList == null) throw new ServiceException("COM.I1016");

        if(!roleList.contains(AuthType.AUTH_APPROVE.getCode())) {
            if (!StringUtils.equals(RequestUtil.getLoginUserid(), agentDto.getUserid())) {
                throw new ServiceException("COM.I1040");
            }
        }
        // 2. 유효성 체크
        boolean rtnVal = this.isValid(agentDto);

        if(rtnVal){
            // 부서가 공란이면 신청자 부서로 자동 지정
            if(StringUtils.isBlank(agentDto.getDeptcd())){
                AccountDto account = accountRepository.findUserDeptInfo(agentDto.getUserid());
                agentDto.setDeptcd(account.getDeptcd());
            }
            try {
                // 대무자 정보 등록
                agentRepository.save(agentDto.toEntity());

                // 시작일자가 오늘인지
                LocalDate inputStartDt = LocalDate.of(Integer.parseInt(agentDto.getAgentStartDt().substring(0,4))
                        ,Integer.parseInt(agentDto.getAgentStartDt().substring(4,6))
                        ,Integer.parseInt(agentDto.getAgentStartDt().substring(6,8)));

                // 시작일자가 오늘이면 즉시 권한 부여
                if(LocalDate.now().equals(inputStartDt)){
                    this.insertRole(agentDto);
                }
            } catch (Exception e) {
                throw new ServiceException("COM.E0001");
            }
        } else {
            throw new ServiceException("COM.I1054");
        }
    }

    public boolean isValid(AgentDto agentDto){
        boolean rtnVal = false;
        LocalDate now = LocalDate.now();

        LocalDate inputStartDt = LocalDate.of(Integer.parseInt(agentDto.getAgentStartDt().substring(0,4))
                ,Integer.parseInt(agentDto.getAgentStartDt().substring(4,6))
                ,Integer.parseInt(agentDto.getAgentStartDt().substring(6,8)));

        LocalDate inputEndSchdlDt = LocalDate.of(Integer.parseInt(agentDto.getAgentEndSchdlDt().substring(0,4))
                ,Integer.parseInt(agentDto.getAgentEndSchdlDt().substring(4,6))
                ,Integer.parseInt(agentDto.getAgentEndSchdlDt().substring(6,8)));

        // 시작일자 > 종료예정일자
        if(inputStartDt.isAfter(inputEndSchdlDt))  throw new ServiceException("COM.I1051");

        // 기간이 겹치는 건이 있는지 체크
        List<Agent> list = agentRepository.findByAgentRegSeqNotAndUseridAndEndYn(agentDto.getAgentRegSeq(), agentDto.getUserid(), "N");
        for(Agent a : list){
            LocalDate inputRegStartDt = LocalDate.of(Integer.parseInt(a.getAgentStartDt().substring(0,4))
                    ,Integer.parseInt(a.getAgentStartDt().substring(4,6))
                    ,Integer.parseInt(a.getAgentStartDt().substring(6,8)));

            LocalDate inputRegEndSchldDt = LocalDate.of(Integer.parseInt(a.getAgentEndSchdlDt().substring(0,4))
                    ,Integer.parseInt(a.getAgentEndSchdlDt().substring(4,6))
                    ,Integer.parseInt(a.getAgentEndSchdlDt().substring(6,8)));

            // inputRegStartDt <= inputStartDt <= inputRegEndSchldDt || inputRegStartDt <= inputEndSchdlDt <= inputRegEndSchldDt
            if((!inputStartDt.isBefore(inputRegStartDt) && !inputStartDt.isAfter(inputRegEndSchldDt))
                    || (!inputEndSchdlDt.isBefore(inputRegStartDt) && !inputEndSchdlDt.isAfter(inputRegEndSchldDt))){
                throw new ServiceException("COM.I1055");
            }
        }

        Optional<Agent> oAgent = agentRepository.findById(agentDto.getAgentRegSeq());
        // 2.1 데이터가 없으면 등록 : save
        if(oAgent.isEmpty()){
            // 오늘 > 시작일자
            if(now.isAfter(inputStartDt))  throw new ServiceException("COM.I1052");
            rtnVal = true;
        } else {
            Agent agent = oAgent.get();

            // 이미 종료되었다 -> 불가, 강제종료여부와 종료일자 확인
            if(StringUtils.equals("Y", agent.getEndYn()))   throw new ServiceException("COM.I1053");

            // 기등록 날짜와 등록된 날짜 비교
            LocalDate inputRegStartDt = LocalDate.of(Integer.parseInt(agent.getAgentStartDt().substring(0,4))
                    ,Integer.parseInt(agent.getAgentStartDt().substring(4,6))
                    ,Integer.parseInt(agent.getAgentStartDt().substring(6,8)));

            // 기등록시작 <= 오늘
            if(!inputRegStartDt.isAfter(now)){
                // 기등록시작 == 등록시작
                if(inputRegStartDt.equals(inputStartDt)) {
                    rtnVal = true;
                }
            }
            // 기등록시작 > 오늘
            else {
                // 오늘 <= 등록일자
                if(!now.isAfter(inputStartDt)) {
                    rtnVal = true;
                }
            }
        }

        return rtnVal;
    }

    public void insertRole(AgentDto agentDto) throws UnknownHostException {
        String ipAddr = RequestUtil.getClientIP();

        Optional<UserRole> oUserRole = userRoleRepository.findById(new UserRolePK(AuthType.AUTH_CONFIRM.getCode(), agentDto.getAgentId()));

        // 기 등록된 권한이 없을 경우에만 등록
        if(oUserRole.isEmpty()) {
            // 권한 즉시 부여
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(AuthType.AUTH_CONFIRM.getCode());
            userRole.setUserid(agentDto.getAgentId());
            userRoleRepository.save(userRole);

            AccountDto account = accountRepository.searchAccountDtoInfo(agentDto.getAgentId());

            // 이력추가
            String chngResonCntnt = "대무 등록으로 인한 권한 부여";
            userRoleHistRepository.save(UserRoleHist.builder()
                    .userid(agentDto.getAgentId())
                    .userRoleId(AuthType.AUTH_CONFIRM.getCode())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("C")
                    .athrtyReqstSeq(null)
                    .deptcd(account.getDeptcd())
                    .deptNm(account.getDeptNm())
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt)
                    .build());
        }
    }
    @Override
    public void endAgent(List<Integer> agentDto) {
        // 종료처리
        try {
            for (Integer id : agentDto) {
                // 1. 데이터 존재 여부 확인
                Optional<Agent> oAgent = agentRepository.findById(id);

                // 2. 데이터가 있으면 : save
                if (oAgent.isPresent()) {
                    Agent agent = oAgent.get();
                    if (StringUtils.equals("Y", agent.getEndYn())) throw new ServiceException("COM.I1053");

                    agent.setEndYn("Y");
                    agent.setAgentEndDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    Agent a = agentRepository.save(agent);
                    this.deleteRole(a.toApi());
                    // 선택된 대무자 권한 즉시 회수
                } else {
                    throw new ServiceException("COM.I0003");
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new ServiceException("COM.E0001");
        }
    }
    public void deleteRole(AgentDto agentDto) throws UnknownHostException {
        String ipAddr = RequestUtil.getClientIP();

        UserRolePK userRolePK = new UserRolePK(AuthType.AUTH_CONFIRM.getCode(), agentDto.getAgentId());
        Optional<UserRole> oUserRole = userRoleRepository.findById(userRolePK);

        AccountDto account = accountRepository.searchAccountDtoInfo(agentDto.getAgentId());

        // 기 등록된 권한이 있을 경우 && 원래 팀장권한이 없을 경우
        if(oUserRole.isPresent()
                && !(Integer.parseInt(account.getReofoCd()) >= 112 && Integer.parseInt(account.getReofoCd()) <= 210)){
            // 권한 즉시 제거
            userRoleRepository.deleteById(userRolePK);

            // 이력추가
            String chngResonCntnt = "대무 종료로 인한 권한 제거";
            userRoleHistRepository.save(UserRoleHist.builder()
                    .userid(agentDto.getAgentId())
                    .userRoleId(AuthType.AUTH_CONFIRM.getCode())
                    .chngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .crudClCd("D")
                    .athrtyReqstSeq(null)
                    .deptcd(account.getDeptcd())
                    .deptNm(account.getDeptNm())
                    .chngUserIpaddr(ipAddr)
                    .chngResonCntnt(chngResonCntnt)
                    .build());
        }
    }
}