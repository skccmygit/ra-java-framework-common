package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleHistDto;
import com.skcc.ra.account.service.AuthHistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[이력관리] 권한 변경 이력 관리(AuthHistResource)", description = "권한 변경 이력 조회 API")
@RestController
@RequestMapping("/v1/com/account/authHist")
@Slf4j
public class AuthHistResource {

    @Autowired
    AuthHistService authHistService;
    
    @Operation(summary = "역할 기준 메뉴 매핑 변경 이력 조회")
    @GetMapping("/role/menu")
    public ResponseEntity<Page<RoleHistDto>> searchMenuRoleChngList(@RequestParam(required = false) String userRoleNm,
                                                                    @RequestParam(required = false) String chrgTaskGroupCd,
                                                                    @RequestParam(required = false) String menuNm,
                                                                    @RequestParam String startDt,
                                                                    @RequestParam String endDt,
                                                                    @RequestParam(required = false) String chngrNm,
                                                                    Pageable pageable){
        return new ResponseEntity<>(authHistService.findMenuRoleHist(userRoleNm, chrgTaskGroupCd, menuNm, startDt, endDt, chngrNm, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 메뉴 매핑 변경 이력 엑셀다운로드")
    @GetMapping("/role/menu/excel")
    public ResponseEntity<Void> downloadMenuRoleHist(@RequestParam(required = false) String userRoleNm,
                                               @RequestParam(required = false) String chrgTaskGroupCd,
                                               @RequestParam(required = false) String menuNm,
                                               @RequestParam String startDt,
                                               @RequestParam String endDt,
                                               @RequestParam(required = false) String chngrNm){

        authHistService.makeMenuHistExcel(userRoleNm, chrgTaskGroupCd, menuNm, startDt, endDt, chngrNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 버튼 매핑 변경 이력 조회")
    @GetMapping("/role/scren")
    public ResponseEntity<Page<RoleHistDto>> searchScrenRoleChngList(@RequestParam(required = false) String userRoleNm,
                                                                     @RequestParam(required = false) String chrgTaskGroupCd,
                                                                     @RequestParam(required = false) String screnNm,
                                                                     @RequestParam String startDt,
                                                                     @RequestParam String endDt,
                                                                     @RequestParam(required = false) String chngrNm,
                                                                     Pageable pageable){
        return new ResponseEntity<>(authHistService.findScrenRoleHist(userRoleNm, chrgTaskGroupCd, screnNm, startDt, endDt, chngrNm, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 버튼 매핑 변경 이력 엑셀다운로드")
    @GetMapping("/role/scren/excel")
    public ResponseEntity<Void> downloadScrenRoleHist(@RequestParam(required = false)String userRoleNm,
                                                @RequestParam(required = false)String chrgTaskGroupCd,
                                                @RequestParam(required = false) String screnNm,
                                                @RequestParam String startDt,
                                                @RequestParam String endDt,
                                                @RequestParam(required = false) String chngrNm){
        authHistService.makeScrenHistExcel(userRoleNm, chrgTaskGroupCd, screnNm, startDt, endDt, chngrNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 사용지 매핑 변경 이력 조회")
    @GetMapping("/role/user")
    public ResponseEntity<Page<RoleHistDto>> searchUserRoleChngList(@RequestParam(required = false) String userRoleNm,
                                                                    @RequestParam(required = false) String userNm,
                                                                    @RequestParam String startDt,
                                                                    @RequestParam String endDt,
                                                                    @RequestParam(required = false) String chngrNm,
                                                                    Pageable pageable){
        return new ResponseEntity<>(authHistService.findUserRoleHist(userRoleNm, userNm, startDt, endDt, chngrNm, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "역할 기준 사용자 매핑 변경 이력 엑셀다운로드")
    @GetMapping("/role/user/excel")
    public ResponseEntity<Void> downloadUserRoleHist(@RequestParam(required = false)String userRoleNm,
                                               @RequestParam(required = false)String userNm,
                                               @RequestParam String startDt,
                                               @RequestParam String endDt,
                                               @RequestParam(required = false) String chngrNm){
        authHistService.makeUserHistExcel(userRoleNm, userNm, startDt, endDt, chngrNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    @Operation(summary = "사용자 기준 메뉴 매핑 변경 이력 조회")
    @GetMapping("/user/menu")
    public ResponseEntity<Page<RoleHistDto>> addAuthHistoryMenu(@RequestParam(required = false) String userNm,
                                                                @RequestParam(required = false) String menuNm,
                                                                @RequestParam(required = false) String chrgTaskGroupCd,
                                                                @RequestParam String startDt,
                                                                @RequestParam String endDt,
                                                                @RequestParam(required = false) String chngrNm,
                                                                Pageable pageable) {

        return new ResponseEntity<>(authHistService.addAuthHistoryMenu(userNm, menuNm, chrgTaskGroupCd, startDt, endDt, chngrNm, pageable), HttpStatus.OK);
    }

    @Operation(summary = "사용자 기준 메뉴 매핑 변경 이력 엑셀다운로드")
    @GetMapping("/user/menu/excel")
    public ResponseEntity<Void> addAuthHistoryMenuExcel(@RequestParam(required = false) String userNm,
                                                  @RequestParam(required = false) String menuNm,
                                                  @RequestParam(required = false) String chrgTaskGroupCd,
                                                  @RequestParam String startDt,
                                                  @RequestParam String endDt,
                                                  @RequestParam(required = false) String chngrNm) {
        authHistService.addAuthHistoryMenuExcel(userNm, menuNm, chrgTaskGroupCd, startDt, endDt, chngrNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "사용자 기준 버튼 매핑 변경 이력 조회 ")
    @GetMapping("/user/scren")
    public ResponseEntity<Page<RoleHistDto>> addAuthHistoryScren(@RequestParam(required = false) String userNm,
                                                                 @RequestParam(required = false) String screnNm,
                                                                 @RequestParam(required = false) String chrgTaskGroupCd,
                                                                 @RequestParam String startDt,
                                                                 @RequestParam String endDt,
                                                                 @RequestParam(required = false) String chngrNm,
                                                                 Pageable pageable) {
        return new ResponseEntity<>(authHistService.addAuthHistoryScren(userNm, screnNm, chrgTaskGroupCd, startDt, endDt, chngrNm, pageable), HttpStatus.OK);
    }

    @Operation(summary = "사용자 기준 버튼 매핑 변경 이력 엑셀다운로드")
    @GetMapping("/user/scren/excel")
    public ResponseEntity<Void> addAuthHistoryScrenExcel(@RequestParam(required = false) String userNm,
                                                   @RequestParam(required = false) String screnNm,
                                                   @RequestParam(required = false) String chrgTaskGroupCd,
                                                   @RequestParam String startDt,
                                                   @RequestParam String endDt,
                                                   @RequestParam(required = false) String chngrNm) {
        authHistService.addAuthHistoryScrenExcel(userNm, screnNm, chrgTaskGroupCd, startDt, endDt, chngrNm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}