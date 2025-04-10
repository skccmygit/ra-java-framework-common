package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.repository.*;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.adaptor.client.CommonClient;
import com.skcc.ra.account.api.dto.domainDto.*;
import com.skcc.ra.account.domain.addAuth.UserAuthReq;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
import com.skcc.ra.account.domain.userSpecificMenu.BookmarkMenu;
import com.skcc.ra.account.domain.userSpecificMenu.MyView;
import com.skcc.ra.account.domain.userSpecificMenu.MyViewDtl;
import com.skcc.ra.account.domain.userSpecificMenu.ShortcutMenu;
import com.skcc.ra.account.repository.*;
import com.skcc.ra.account.service.UserActvyLogService;
import com.skcc.ra.account.service.UserMenuService;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.ObjectUtil;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserMenuServiceImpl implements UserMenuService {

    @Autowired
    ShortcutMenuRepository shortcutMenuRepository;

    @Autowired
    BookmarkMenuRepository bookmarkMenuRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleMenuRepository roleMenuRepository;

    @Autowired
    UserMenuRepository userMenuRepository;

    @Autowired
    MyViewRepository myViewRepository;

    @Autowired
    MyViewDtlRepository myViewDtlRepository;

    @Autowired
    UserAuthReqRepository userAuthReqRepository;

    @Autowired
    UserActvyLogService userActvyLogService;

    @Autowired
    CommonClient commonClient;

    @Override
    public List<MenuDto> findShortcutMenu() {
        String userid = RequestUtil.getLoginUserid();
        List<MenuIDto> menuIDtoList = shortcutMenuRepository.findShortcutMenu(userid);
        List<MenuDto> menuDtoList = new ArrayList<>();
        if (menuIDtoList != null && !menuIDtoList.isEmpty()) {
            menuDtoList = menuIDtoList.stream().map(MenuDto::new).collect(Collectors.toList());
        }

        return menuDtoList;
    }

    @Override
    public ShortcutMenuDto createShortcutMenu(ShortcutMenuDto shortcutMenuDto) {
        shortcutMenuDto.setUserid(RequestUtil.getLoginUserid());
        ShortcutMenu shortcutMenu = shortcutMenuRepository.save(shortcutMenuDto.toEntity());
        return shortcutMenu.toApi();
    }

    @Override
    public void deleteShortcutMenu(ShortcutMenuDto shortcutMenuDto) {
        UserMenuPK pk = new UserMenuPK(RequestUtil.getLoginUserid(), shortcutMenuDto.getMenuId());
        if (!shortcutMenuRepository.existsById(pk)) throw new ServiceException("COM.I0001");

        shortcutMenuRepository.deleteById(pk);
    }

    @Override
    public void deleteShortcutMenuByUserid(String userid) {
        if ("".equals(userid) || userid == null) {
            throw new ServiceException("COM.I0001");
        } else {
            shortcutMenuRepository.deleteByUserid(userid);
        }
    }

    @Override
    public void createShortcutMenuList(List<ShortcutMenuDto> shortcutMenuDtoList) {
        // 기존데이터 일괄 삭제
        String userid = RequestUtil.getLoginUserid();
        deleteShortcutMenuByUserid(userid);
        // 넘어온 데이터가 공백일 경우 일괄 삭제
        if (!shortcutMenuDtoList.isEmpty()) {
            // 신규데이터 등록
            for (ShortcutMenuDto item : shortcutMenuDtoList) {
                createShortcutMenu(item);
            }
        }
    }

    @Override
    public void deleteBookmarkMenu(BookmarkMenuDto bookmarkMenuDto) {

        UserMenuPK pk = new UserMenuPK(RequestUtil.getLoginUserid(), bookmarkMenuDto.getMenuId());
        if (!bookmarkMenuRepository.existsById(pk)) throw new ServiceException("COM.I1031");

        bookmarkMenuRepository.deleteById(pk);
    }

    @Override
    public List<MenuIDto> findAllUserMenu(String userid) {

        List<MenuIDto> menuList = new ArrayList<>();

        menuList.addAll(findUserRoleMenu(userid));
        menuList.addAll(findUserMenu(userid));

        return menuList.stream().filter(ObjectUtil.distinctByKeys(MenuIDto::getMenuId)).collect(Collectors.toList());
    }

    @Override
    public BookmarkMenuDto createBookmarkMenu(BookmarkMenuDto bookmarkMenuDto) {

        bookmarkMenuDto.setUserid(RequestUtil.getLoginUserid());
        BookmarkMenu bookmarkMenu = bookmarkMenuRepository.save(bookmarkMenuDto.toEntity());

        return bookmarkMenu.toApi();
    }

    @Override
    public void updateBookmarkMenu(List<BookmarkMenuDto> bookmarkMenuDtoList) {
        String userid = RequestUtil.getLoginUserid();
        List<BookmarkMenu> bookmarkMenuList = new ArrayList<>();
        for (BookmarkMenuDto item : bookmarkMenuDtoList) {
            item.setUserid(userid);
            bookmarkMenuList.add(item.toEntity());
        }

        bookmarkMenuRepository.saveAll(bookmarkMenuList);
    }

    @Override
    public List<MenuDto> findBookmarkMenu() {
        String userid = RequestUtil.getLoginUserid();
        List<MenuIDto> menuIDtoList = bookmarkMenuRepository.findBookmarkMenu(userid);
        List<MenuDto> menuDtoList = new ArrayList<>();
        if (menuIDtoList != null && !menuIDtoList.isEmpty()) {
            menuDtoList = menuIDtoList.stream().map(MenuDto::new).collect(Collectors.toList());
        }

        return menuDtoList;
    }

    @Override
    public List<MyViewDto> findMyViewList() {
        String userid = RequestUtil.getLoginUserid();
        List<MyView> myViewList = myViewRepository.findByUserid(userid, Sort.by("userScrenCnstteSeq"));

        return myViewList.stream().map(MyView::toApi).collect(Collectors.toList());
    }

    @Override
    public List<MyViewDtlDto> findMyViewDtlList(String userScrenCnstteSeq) {

        if ("".equals(userScrenCnstteSeq) || userScrenCnstteSeq == null) throw new ServiceException("COM.I0003");
        return myViewDtlRepository.findByUserScrenCnstteSeq(Integer.valueOf(userScrenCnstteSeq));
    }

    @Override
    public void saveMyView(MyViewDto myViewDto) {
        if (myViewDto == null) throw new ServiceException("COM.I0003");

        String userid = RequestUtil.getLoginUserid();
        myViewDto.setUserid(userid);
        // 마이뷰 기본 등록
        MyView myView = myViewRepository.save(myViewDto.toEntity());
        Integer userScrenCnstteSeq = myView.getUserScrenCnstteSeq();
        // 마이뷰 상세 수정할 경우
        if (myViewDto.getMyViewDtlDtoList() != null && !myViewDto.getMyViewDtlDtoList().isEmpty()) {
            // 기존에 등록되어 있는 MyView 가 있으면 삭제
            myViewDtlRepository.deleteByUserScrenCnstteSeq(myViewDto.getUserScrenCnstteSeq());

            // 메뉴 ID를 받아서 화면 ID 저장 --> ERD 수정 가능한지 확인해서 MenuId로 변경
            List<MyViewDtl> myViewDtlList = new ArrayList<>();
            for (MyViewDtlDto item : myViewDto.getMyViewDtlDtoList()) {
                item.setScrenId(commonClient.findByMenuId(item.getMenuId()).getScrenId());
                item.setUserScrenCnstteSeq(userScrenCnstteSeq);
                myViewDtlList.add(item.toEntity());
            }
            myViewDtlRepository.saveAll(myViewDtlList);
        }
    }

    @Override
    public void modifyMyView(List<MyViewDto> myViewDtoList) {
        if (myViewDtoList == null || myViewDtoList.isEmpty()) throw new ServiceException("COM.I0003");

        String userid = RequestUtil.getLoginUserid();
        // 마이뷰 기본 수정
        for (MyViewDto item : myViewDtoList) {
            if (myViewRepository.existsById(item.getUserScrenCnstteSeq())) {
                item.setUserid(userid);
                myViewRepository.save(item.toEntity());
            } else {
                throw new ServiceException("COM.I0001");
            }
        }
    }

    @Override
    public List<MenuIDto> findUserRoleMenu(String userid) {
        List<MenuIDto> tmpDtoList = new ArrayList<>();
        List<String> roles = userRoleRepository.findRoles(userid);
        if (!roles.isEmpty()) {
            tmpDtoList = roleMenuRepository.findUserMenus(roles, userid);
        }

        return tmpDtoList;
    }

    @Override
    public List<MenuIDto> findUserMenu(String userid) {
        return userMenuRepository.findUserMenus(userid);
    }

    @Override
    public MenuDto findLoginUserMenu(String menuId) throws UnknownHostException {

        String userid = RequestUtil.getLoginUserid();
        MenuDto result;

        List<String> roles = userRoleRepository.findRoles(userid);
        if (roles.isEmpty()) {
            throw new ServiceException("COM.I1040");
        } else {
            result = roleMenuRepository.findUserMenusByMenuId(roles, userid, menuId);
            if (result == null) {
                result = userMenuRepository.findUserMenuByMenuId(userid, menuId);
                if (result == null) {
                    throw new ServiceException("COM.I1040");
                }
            }

            // 화면이동 활동 로그 입력
            if (!"COM-MP0007".equals(result.getScrenId()) && !"COM-MP0008".equals(result.getScrenId())) { // 메인 우측 사이드 화면, 알림리스트는 이력남기지 않음
                userActvyLogService.regUserActvyLog(UserActvyLogDto.builder()
                        .userid(RequestUtil.getLoginUserid())
                        .screnId(result.getScrenId())
                        .userActvyTypeCd("3")
                        .systmCtgryCd("ARG")
                        .build());
            }
            return result;
        }
    }

    @Override
    public List<MenuIDto> searchMenuAuthByUserid(Integer athrtyReqstSeq, String menuNm, String userid) {
        if (menuNm == null) menuNm = "";
        List<MenuIDto> menuDtos;
        String reqUserid = userid;
        if (StringUtils.isEmpty(reqUserid) && athrtyReqstSeq > 0) {
            Optional<UserAuthReq> oUserAuthReq = userAuthReqRepository.findByAthrtyReqstSeq(athrtyReqstSeq);
            if (oUserAuthReq.isEmpty()) {
                throw new ServiceException("COM.I1022");
            }
            reqUserid = oUserAuthReq.get().getUserid();
        }
        menuDtos = userMenuRepository.searchMenuAuthByUserid(reqUserid, menuNm);

        return menuDtos;
    }

    @Override
    public List<MenuDto> searchReqMenuAuthByAthrtyReqstSeq(Integer athrtyReqstSeq, String menuNm) {
        if (menuNm == null) menuNm = "";
        List<MenuDto> menuDtos = userMenuRepository.searchReqMenuAuthByAthrtyReqstSeq(athrtyReqstSeq, menuNm);

        List<MenuDto> tmpDtoList = new ArrayList<>();
        for (MenuDto m : menuDtos) {
            tmpDtoList = getSuperMenuList(athrtyReqstSeq, m, tmpDtoList);
            tmpDtoList.add(m);
        }

        return tmpDtoList.stream().filter(ObjectUtil.distinctByKeys(MenuDto::getMenuId)).collect(Collectors.toList());
    }

    public List<MenuDto> getSuperMenuList(Integer athrtyReqstSeq, MenuDto m, List<MenuDto> tmpDtoList) {
        if (m.getSuperMenuId() != null) {
            getSuperMenuList(athrtyReqstSeq, userMenuRepository.findSuperMenuId(athrtyReqstSeq, m.getSuperMenuId()), tmpDtoList);
        }
        tmpDtoList.add(m);
        return tmpDtoList;
    }
}

