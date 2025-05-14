package com.skcc.ra.common.api.dto.responseDto;

import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import lombok.Data;

@Data
public class MenuResponseDto implements MenuIDto {
    private String menuId;
    private String chrgTaskGroupCd;
    private String menuTypeCd;
    private String menuNm;
    private String screnExcutClCd;
    private String menuDesc;
    private Integer menuStepVal;
    private Integer sortSeqn;
    private String useYn;
    private String menuExpseYn;
    private String superMenuId;
    private String superMenuNm;
    private String screnId;
    private String screnNm;
    private String screnDesc;
    private String screnUrladdr;
    private String screnClCd;
    private String screnSizeClCd;
    private Integer screnWidthSize;
    private Integer screnVrtlnSize;
    private Integer screnStartTopCodn;
    private Integer screnStartLeftCodn;
    private String linkaSystmTagCntnt;
    private Integer shortcutSortSeqn;
    private String bookmarkYN;
    private String authYn;
    private String chrgTaskGroupNm;

    public static MenuResponseDto from(MenuIDto menuIDto) {
        MenuResponseDto menuDto = new MenuResponseDto();
        menuDto.setMenuId(menuIDto.getMenuId());
        menuDto.setChrgTaskGroupCd(menuIDto.getChrgTaskGroupCd());
        menuDto.setMenuTypeCd(menuIDto.getMenuTypeCd());
        menuDto.setMenuNm(menuIDto.getMenuNm());
        menuDto.setScrenExcutClCd(menuIDto.getScrenExcutClCd());
        menuDto.setMenuDesc(menuIDto.getMenuDesc());
        menuDto.setMenuStepVal(menuIDto.getMenuStepVal());
        menuDto.setSortSeqn(menuIDto.getSortSeqn());
        menuDto.setUseYn(menuIDto.getUseYn());
        menuDto.setMenuExpseYn(menuIDto.getMenuExpseYn());
        menuDto.setSuperMenuId(menuIDto.getSuperMenuId());
        menuDto.setSuperMenuNm(menuIDto.getSuperMenuNm());
        menuDto.setScrenId(menuIDto.getScrenId());
        menuDto.setScrenNm(menuIDto.getScrenNm());
        menuDto.setScrenDesc(menuIDto.getScrenDesc());
        menuDto.setScrenUrladdr(menuIDto.getScrenUrladdr());
        menuDto.setScrenClCd(menuIDto.getScrenClCd());
        menuDto.setScrenSizeClCd(menuIDto.getScrenSizeClCd());
        menuDto.setScrenWidthSize(menuIDto.getScrenWidthSize());
        menuDto.setScrenVrtlnSize(menuIDto.getScrenVrtlnSize());
        menuDto.setScrenStartTopCodn(menuIDto.getScrenStartTopCodn());
        menuDto.setScrenStartLeftCodn(menuIDto.getScrenStartLeftCodn());
        menuDto.setLinkaSystmTagCntnt(menuIDto.getLinkaSystmTagCntnt());
        menuDto.setShortcutSortSeqn(menuIDto.getShortcutSortSeqn());
        menuDto.setBookmarkYN(menuIDto.getBookmarkYN());
        menuDto.setAuthYn(menuIDto.getAuthYn());
        menuDto.setChrgTaskGroupNm(menuIDto.getChrgTaskGroupNm());
        return menuDto;
    }

}