package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleHistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthHistService {

    Page<RoleHistDto> findMenuRoleHist(String userRoleNm, String chrgTaskGroupCd, String menuNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    Page<RoleHistDto> findScrenRoleHist(String userRoleNm, String chrgTaskGroupCd, String screnNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    Page<RoleHistDto> findUserRoleHist(String userRoleNm, String userNm, String startDt, String endDt, String chngrNm, Pageable pageable);

    void makeMenuHistExcel(String userRoleNm, String chrgTaskGroupCd, String menuNm, String startDt, String endDt, String chngrNm);

    void makeScrenHistExcel(String userRoleNm, String chrgTaskGroupCd, String screnNm, String startDt, String endDt, String chngrNm);

    void makeUserHistExcel(String userRoleNm, String userNm, String startDt, String endDt, String chngrNm);

    Page<RoleHistDto> addAuthHistoryMenu(String userNm, String menuNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm, Pageable pageable);

    Page<RoleHistDto> addAuthHistoryScren(String userNm, String screnNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm, Pageable pageable);

    void addAuthHistoryMenuExcel(String userNm, String menuNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm);

    void addAuthHistoryScrenExcel(String userNm, String screnNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm);
}

