package com.skcc.ra.account.service;

import com.skcc.ra.account.api.dto.domainDto.BookmarkMenuDto;
import com.skcc.ra.account.api.dto.domainDto.MyViewDtlDto;
import com.skcc.ra.account.api.dto.domainDto.MyViewDto;
import com.skcc.ra.account.api.dto.domainDto.ShortcutMenuDto;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;

import java.net.UnknownHostException;
import java.util.List;

public interface UserMenuService {

    /********************************************************
     * 사용자에 할당된 메뉴
     ********************************************************/
    List<MenuIDto> findAllUserMenu(String userid);

    /********************************************************
     * 즐겨찾기 메뉴
     ********************************************************/
    BookmarkMenuDto createBookmarkMenu(BookmarkMenuDto bookmarkMenuDto);

    void deleteBookmarkMenu(BookmarkMenuDto bookmarkMenuDto);

    void updateBookmarkMenu(List<BookmarkMenuDto> bookmarkMenuDtoList);

    List<MenuDto> findBookmarkMenu() throws UnknownHostException;
    /********************************************************
     * 바로가기 메뉴
     ********************************************************/
    ShortcutMenuDto createShortcutMenu(ShortcutMenuDto shortcutMenuDto);

    void deleteShortcutMenu(ShortcutMenuDto shortcutMenuDto);

    void deleteShortcutMenuByUserid(String userid);

    void createShortcutMenuList(List<ShortcutMenuDto> shortcutMenuDtoList);

    List<MenuDto> findShortcutMenu() throws UnknownHostException;
    
    List<MenuIDto> findUserRoleMenu(String userid) throws UnknownHostException;


    /********************************************************
     * 마이뷰
     ********************************************************/
    List<MyViewDto> findMyViewList();

    List<MyViewDtlDto>findMyViewDtlList(String userScrenCnstteSeq);

    void saveMyView(MyViewDto myViewDto);

    void modifyMyView(List<MyViewDto> myViewDtoList);


    /********************************************************
     * 사용자 추가권한 - 메뉴
     ********************************************************/
    List<MenuIDto> searchMenuAuthByUserid(Integer athrtyReqstSeq, String menuNm, String userid);

    List<MenuDto> searchReqMenuAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String menuNm);

    List<MenuIDto> findUserMenu(String userid) throws UnknownHostException;

    /*
    * 메뉴 이동 시 실시간 권한 체크..
    * */
    MenuDto findLoginUserMenu(String menuId) throws UnknownHostException;

}
