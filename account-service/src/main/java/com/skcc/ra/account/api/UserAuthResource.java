package com.skcc.ra.account.api;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.skcc.ra.account.api.dto.domainDto.BookmarkMenuDto;
import com.skcc.ra.account.api.dto.domainDto.MyViewDtlDto;
import com.skcc.ra.account.api.dto.domainDto.MyViewDto;
import com.skcc.ra.account.api.dto.domainDto.ShortcutMenuDto;
import com.skcc.ra.account.service.UserMenuService;
import com.skcc.ra.account.service.UserScrenBttnService;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[권한관리] 사용자 기준 메뉴 관리(UserAuthResource)", description = "사용자 기준 메뉴 관리 관련 API ")
@RestController
@RequestMapping("/v1/com/account/userAuth")
@Slf4j
public class UserAuthResource {

    @Autowired
    private UserMenuService userMenuService;

    @Autowired
    private UserScrenBttnService userScrenBttnService;

    /***********************************************************************************************************************/
    
    @Operation(summary = "즐겨찾기 메뉴 등록 - 로그인 사용자 기준")
    @PostMapping("/bookmark")
    public ResponseEntity<BookmarkMenuDto> createBookmarkMenu(@RequestBody BookmarkMenuDto bookmarkMenuDto){
        return new ResponseEntity<>(userMenuService.createBookmarkMenu(bookmarkMenuDto), HttpStatus.OK);
    }
    
    
    @Operation(summary = "즐겨찾기 메뉴 일괄 수정 - 순서변경  - 로그인 사용자 기준")
    @PostMapping("/bookmark/all")
    public ResponseEntity<Void> updateBookmarkMenu(@RequestBody List<BookmarkMenuDto> bookmarkMenuDtoList){
        userMenuService.updateBookmarkMenu(bookmarkMenuDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "즐겨찾기 메뉴 조회 - 로그인 사용자 기준")
    @GetMapping("/bookmark")
    public ResponseEntity<List<MenuDto>> findBookmarkMenu() throws UnknownHostException {
        return new ResponseEntity<>(userMenuService.findBookmarkMenu(), HttpStatus.OK);
    }
    
    @Operation(summary = "즐겨찾기 메뉴 삭제 - 로그인 사용자 기준")
    @DeleteMapping("/bookmark")
    public ResponseEntity deleteBookmarkMenu(@RequestBody BookmarkMenuDto bookmarkMenuDto){
        userMenuService.deleteBookmarkMenu(bookmarkMenuDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***********************************************************************************************************************/
    
    @Operation(summary = "바로가기 메뉴 등록 - 단건 - 로그인 사용자 기준")
    @PostMapping("/shortcut")
    public ResponseEntity<ShortcutMenuDto> createShortcutMenu(@RequestBody ShortcutMenuDto shortcutMenuDto){
        return new ResponseEntity<>(userMenuService.createShortcutMenu(shortcutMenuDto), HttpStatus.OK);
    }

    @Operation(summary = "바로가기 메뉴 등록 - 다건 - 로그인 사용자 기준")
    @PostMapping("/shortcut/list")
    public ResponseEntity<Void> createShortcutMenuList(@RequestBody List<ShortcutMenuDto> shortcutMenuDtoList){
        userMenuService.createShortcutMenuList(shortcutMenuDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "바로가기 메뉴 조회 - 로그인 사용자 기준")
    @GetMapping("/shortcut")
    public ResponseEntity<List<MenuDto>> findShortcutMenu() throws UnknownHostException {
        return new ResponseEntity<>(userMenuService.findShortcutMenu(), HttpStatus.OK);
    }

    @Operation(summary = "바로가기 메뉴 삭제 - 로그인 사용자 기준")
    @DeleteMapping("/shortcut")
    public ResponseEntity<Void> deleteShortcutMenu(@RequestBody ShortcutMenuDto shortcutMenuDto){
        userMenuService.deleteShortcutMenu(shortcutMenuDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "마이뷰 등록")
    @PostMapping("/myView")
    public ResponseEntity<Void> saveMyView(@RequestBody MyViewDto myViewDto){
        userMenuService.saveMyView(myViewDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "마이뷰 조회")
    @GetMapping("/myView")
    public ResponseEntity<List<MyViewDto>> findMyViewList(){
        return new ResponseEntity<>(userMenuService.findMyViewList(), HttpStatus.OK);
    }

    @Operation(summary = "마이뷰 수정 / 삭제 - 삭제는 Flag update")
    @PutMapping("/myView")
    public ResponseEntity<Void> modifyMyView(@RequestBody List<MyViewDto> myViewDtoList){
        userMenuService.modifyMyView(myViewDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "마이뷰 상세 조회")
    @GetMapping("/myView/dtl")
    public ResponseEntity<List<MyViewDtlDto>> findMyViewDtlList(@RequestParam String userScrenCnstteSeq){
        return new ResponseEntity<>(userMenuService.findMyViewDtlList(userScrenCnstteSeq), HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "메뉴 조회 - 로그인 사용자 기준 권한이 있는 전체 메뉴 - 로그인 후 메뉴 트리 생성 용도")
    @GetMapping("/login")
    public ResponseEntity<List<MenuIDto>> findUserRoleMenu() {
        return new ResponseEntity<>(userMenuService.findAllUserMenu(RequestUtil.getLoginUserid()), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 조회 - 입력 받은 사용자 ID 기준 권한이 있는 전체 메뉴 - 사용자 별 권한 조회 용도")
    @GetMapping("/userid")
    public ResponseEntity<List<MenuIDto>> findUserRoleMenu(@RequestParam String userid) {
        return new ResponseEntity<>(userMenuService.findAllUserMenu(userid), HttpStatus.OK);
    }
    
    @Operation(summary = "사용자 기준 권한이 있는 화면 리스트 조회 - 화면에 매핑된 버튼 기준 - 사용자 별 권한 조회 용도")
    @GetMapping("/scren")
    public ResponseEntity<List<ScrenDto>> findUserRoleScren(@RequestParam String userid){
        return new ResponseEntity<>(userScrenBttnService.findUserRoleScren(userid), HttpStatus.OK);
    }
    
    @Operation(summary = "사용자 기준 권한이 있는 버튼 리스트 조회 - 사용자 ID, 화면 ID 기준 - 사용자 별 권한 조회 용도")
    @GetMapping("/scren/bttn")
    public ResponseEntity<List<BttnDto>> findBttnByUserScren(@RequestParam String userid,
                                                             @RequestParam String screnId) {
        return new ResponseEntity<>(userScrenBttnService.findBttnByUserScren(userid, screnId), HttpStatus.OK);
    }

    /***********************************************************************************************************************/

    @Operation(summary = "로그인 사용자 기준 특정 메뉴 권한 여부 조회 - 메뉴 접근 시 마다 체크")
    @GetMapping("/menu")
    public ResponseEntity<MenuDto> findUserMenu(@RequestParam String menuId) throws UnknownHostException {
        return new ResponseEntity<>(userMenuService.findLoginUserMenu(menuId), HttpStatus.OK);
    }

    @Operation(summary = "로그인 사용자 기준 특정 화면 권한 있는 버튼 리스트 조회 - 화면 오픈 시 마다 체크")
    @GetMapping("/scren/bttn/auth")
    public ResponseEntity<List<BttnDto>> findBttnByUserScren(@RequestParam String screnId) {
        return new ResponseEntity<>(userScrenBttnService.findBttnByUserScren(RequestUtil.getLoginUserid(), screnId), HttpStatus.OK);
    }

}
