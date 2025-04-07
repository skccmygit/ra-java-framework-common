package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.domainDto.MenuStatisticsDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ScrenIDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MenuService {

    MenuDto makeMenu(MenuDto menuDto);

    List<MenuIDto> findAll();
    List<MenuIDto> findUseAll();
    MenuDto findByMenuId(String menuId);
    MenuDto findbyScrenId(String screnId);

    void excelDownMenu() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ScrenDto makeScren(ScrenDto screnDto);
    ScrenDto createScren(ScrenDto screnDto);
    ScrenDto updateScren(ScrenDto screnDto);
    Page<ScrenIDto> findUseScren(String findScren, String screnClCd, String screnNm, String isUnpaged, Pageable pageable);
    Page<ScrenIDto> findAllScren(String findScren, String screnClCd, String screnNm, Pageable pageable);

    ScrenDto findByScrenId(String screnId);

    Page<MenuStatisticsDto> searchMenuStatistics(String chrgTaskGroupCd, String sumrDtFrom, String sumrDtTo, Pageable pageable);

    void makeExcel(String chrgTaskGroupCd, String sumrDtFrom, String sumrDtTo) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException ;

    String findsystmConnPsbty(String menuId);
}
