package com.skcc.ra.account.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqDto;
import com.skcc.ra.account.api.dto.domainDto.UserAuthReqHisDto;
import com.skcc.ra.account.api.dto.requestDto.UserAuthProcReqDto;
import com.skcc.ra.account.api.dto.responseDto.UserAuthReqListDto;
import com.skcc.ra.account.service.RoleService;
import com.skcc.ra.account.service.UserAuthReqService;
import com.skcc.ra.account.service.UserMenuService;
import com.skcc.ra.account.service.UserScrenBttnService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@Tag(name = "[권한관리] 추가 권한 신청 관리(AddAuthRequestResource)", description = "추가 권한 신청 시 사용하는 API")
@RestController
@RequestMapping("/v1/com/account/userAuthRequest")
@Slf4j
public class AddAuthRequestResource {

    @Autowired
    private UserAuthReqService userAuthReqService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMenuService userMenuService;

    @Autowired
    private UserScrenBttnService userScrenBttnService;

    @Operation(summary = "추가 권한 신청 건 조회 - 일반 사용자")
    @GetMapping
    public ResponseEntity<Page<UserAuthReqListDto>> searchUserAuthReqListByConditionForUser(@RequestParam(required = false) String userNm,
                                                                                            @RequestParam(required = false) String athrtyReqstOpDtmFrom,
                                                                                            @RequestParam(required = false) String athrtyReqstOpDtmTo,
                                                                                            @RequestParam(required = false) String athrtyReqstStsCd,
                                                                                            Pageable pageable) {
        return new ResponseEntity<>(userAuthReqService.searchUserAuthReqListByConditionForUser(userNm, athrtyReqstOpDtmFrom, athrtyReqstOpDtmTo, athrtyReqstStsCd, pageable)
                , HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 신청")
    @PostMapping
    public ResponseEntity<UserAuthReqDto> createUserAuthReq(@RequestBody UserAuthReqDto userAuthReqDto) throws UnknownHostException {
        return new ResponseEntity<>(userAuthReqService.createUserAuthReq(userAuthReqDto), HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 신청 프로세스 처리 - 취소 / 검토 / 담당자검토 / 반려")
    @PutMapping
    public ResponseEntity<List<UserAuthReqDto>> procUserAuthReq(@RequestBody UserAuthProcReqDto userAuthProcReqDto) throws UnknownHostException {
        return new ResponseEntity<>(userAuthReqService.procUserAuthReq(userAuthProcReqDto), HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 신청 건 조회 - 검토자/승인자 조회")
    @GetMapping("/admin")
    public ResponseEntity<Page<UserAuthReqListDto>> searchUserAuthReqListByConditionForAdmin(@RequestParam(required = false) String userNm,
                                                                                             @RequestParam(required = false) String athrtyReqstOpDtmFrom,
                                                                                             @RequestParam(required = false) String athrtyReqstOpDtmTo,
                                                                                             @RequestParam(required = false) String athrtyReqstStsCd,
                                                                                             @RequestParam(required = false) String indivInfoYn,
                                                                                             Pageable pageable) {
        return new ResponseEntity<>(userAuthReqService.searchUserAuthReqListByConditionForAdmin(userNm, athrtyReqstOpDtmFrom, athrtyReqstOpDtmTo, athrtyReqstStsCd, indivInfoYn, pageable)
                                    , HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 신청 건 확인")
    @GetMapping("/check")
    public ResponseEntity<List<UserAuthReqHisDto>> checkUserAuthReq(@RequestParam Integer athrtyReqstSeq) {
        return new ResponseEntity<>(userAuthReqService.checkUserAuthReq(athrtyReqstSeq), HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 승인 처리")
    @PutMapping("/approve")
    public ResponseEntity<List<UserAuthReqDto>> approveUserAuthReq(@RequestBody List<UserAuthReqDto> userAuthReqDtoList) throws UnknownHostException {
        return new ResponseEntity<>(userAuthReqService.approveUserAuthReq(userAuthReqDtoList), HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 부여를 위한 전체 역할 리스트 조회 - 기 추가 권한 부여된 역할은 별도 표기")
    @GetMapping("/role/all")
    public ResponseEntity<List<RoleDto>> searchRoleAuthByUserid(@RequestParam String userid){
        return new ResponseEntity<>(roleService.searchRoleAuthByUserid(userid), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 부여를 위한 전체 메뉴 리스트 조회 - 기 추가 권한 부여된 메뉴는 별도 표기")
    @GetMapping("/menu/all")
    public ResponseEntity<List<MenuIDto>> searchMenuAuthByUserid(@RequestParam(required = false) Integer athrtyReqstSeq,
                                                                 @RequestParam(required = false) String menuNm,
                                                                 @RequestParam(required = false) String userid){
        return new ResponseEntity<>(userMenuService.searchMenuAuthByUserid(athrtyReqstSeq, menuNm, userid), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 부여를 위한 전체 화면 리스트 조회")
    @GetMapping("/scren/all")
    public ResponseEntity<List<ScrenDto>> searchScrenAuthByUserid(@RequestParam(required = false) String chrgTaskGroupCd,
                                                                  @RequestParam(required = false) String screnClCd,
                                                                  @RequestParam(required = false) String screnNm){
        return new ResponseEntity<>(userScrenBttnService.searchScrenAuthByUserid(chrgTaskGroupCd, screnClCd, screnNm), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 부여를 위한 버튼 리스트 조회 - 화면 ID 기준")
    @GetMapping("/bttn/all")
    public ResponseEntity<List<BttnDto>> searchBttnAuthByUserid(@RequestParam(required = false) Integer athrtyReqstSeq,
                                                                @RequestParam(required = false) String screnId,
                                                                @RequestParam(required = false) String userid) {
        return new ResponseEntity<>(userScrenBttnService.searchBttnAuthByUserid(athrtyReqstSeq, screnId, userid), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 신청 후 부여 받은 역할 리스트 조회 - 신청번호 기준")
    @GetMapping("/role/request")
    public ResponseEntity<List<RoleDto>> searchReqRoleAuthByAthrtyReqstSeq(@RequestParam Integer athrtyReqstSeq){
        return new ResponseEntity<>(roleService.searchReqRoleAuthByAthrtyReqstSeq(athrtyReqstSeq), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 신청 후 부여 받은 메뉴 리스트 조회 - 신청번호 기준")
    @GetMapping("/menu/request")
    public ResponseEntity<List<MenuDto>> searchReqMenuAuthByAthrtyReqstSeq(@RequestParam Integer athrtyReqstSeq,
                                                                           @RequestParam(required = false) String menuNm){
        return new ResponseEntity<>(userMenuService.searchReqMenuAuthByAthrtyReqstSeq(athrtyReqstSeq, menuNm), HttpStatus.OK);
    }
    
    @Operation(summary = "추가 권한 신청 후 부여 받은 화면 리스트 조회 - 신청번호 기준")
    @GetMapping("/scren/request")
    public ResponseEntity<List<ScrenDto>> searchReqScrenAuthByAthrtyReqstSeq(@RequestParam Integer athrtyReqstSeq,
                                                                             @RequestParam(required = false) String chrgTaskGroupCd ,
                                                                             @RequestParam(required = false) String screnNm){
        return new ResponseEntity<>(userScrenBttnService.searchReqScrenAuthByAthrtyReqstSeq(athrtyReqstSeq, chrgTaskGroupCd, screnNm), HttpStatus.OK);
    }

    @Operation(summary = "추가 권한 신청 후 부여 받은 버튼 리스트 조회 - 신청번호 기준")
    @GetMapping("/bttn/request")
    public ResponseEntity<List<BttnDto>> searchReqBttnAuthByAthrtyReqstSeq(@RequestParam Integer athrtyReqstSeq,
                                                                           @RequestParam(required = false) String screnId) {
        return new ResponseEntity<>(userScrenBttnService.searchReqBttnAuthByAthrtyReqstSeq(athrtyReqstSeq, screnId), HttpStatus.OK);
    }

    @Operation(summary = "사용자 기준 추가 메뉴/버튼 매핑 - 추가 권한 신청 건에 대한 보완을 위한 API")
    @PutMapping("/add/user")
    public ResponseEntity<List<UserAuthReqDto>> addAuthForUser(@RequestBody UserAuthReqDto userAuthReqDto) throws UnknownHostException {
        userAuthReqService.createUserAuthByReqHis(userAuthReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}