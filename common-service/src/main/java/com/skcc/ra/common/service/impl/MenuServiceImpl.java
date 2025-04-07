package com.skcc.ra.common.service.impl;

import com.skcc.ra.common.api.dto.excelDto.*;
import com.skcc.ra.common.repository.*;
import com.skcc.ra.common.api.dto.excelDto.*;
import com.skcc.ra.common.repository.*;
import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.api.dto.domainDto.MenuDto;
import com.skcc.ra.common.api.dto.domainDto.MenuStatisticsDto;
import com.skcc.ra.common.api.dto.domainDto.ScrenDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuExelDownloadIDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.MenuIDto;
import com.skcc.ra.common.api.dto.responseDto.ifDto.ScrenIDto;
import com.skcc.ra.common.domain.apiInfo.ApiInfo;
import com.skcc.ra.common.domain.menu.Bttn;
import com.skcc.ra.common.domain.menu.Menu;
import com.skcc.ra.common.domain.menu.Scren;
import com.skcc.ra.common.domain.menu.SystmConnPsbty;
import com.skcc.ra.common.domain.menu.pk.BttnPK;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.service.MenuService;
import com.skcc.ra.common.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ScrenRepository screnRepository;

    @Autowired
    BttnRepository bttnRepository;

    @Autowired
    MenuStatisticsRepository menuStatisticsRepository;

    @Autowired
    SystmConnPsbtyRepository systmConnPsbtyRepository;

    @Autowired
    ApiInfoRepository apiInfoRepository;

    @Override
    public MenuDto makeMenu(MenuDto menuDto){

        boolean isExist = menuRepository.existsById(menuDto.getMenuId());
        boolean isNew = StringUtils.equals(menuDto.getNewYn(),"Y");

        if (isExist && isNew) throw new ServiceException("COM.I0008");
        if (!isExist && !isNew) throw new ServiceException("COM.I0007");

        return menuRepository.save(menuDto.toEntity()).toApi();
    }

    @Override
    public List<MenuIDto> findAll(){
        return menuRepository.findAllMenu("");
    }

    @Override
    public List<MenuIDto> findUseAll(){
        return menuRepository.findAllMenu("Y");
    }

    @Override
    public MenuDto findByMenuId(String menuId) {
        if (menuId == null) throw new ServiceException("COM.I0003");
        if (menuRepository.existsById(menuId)) {
            return new MenuDto(menuRepository.findMenuDtl(menuId));
        } else {
            throw new ServiceException("COM.I0007");
        }
    }

    @Override
    public MenuDto findbyScrenId(String screnId) {
        Menu menu = menuRepository.findByScrenIdAndUseYn(screnId,"Y");
        return menu.toApi();
    }

    @Override
    public void excelDownMenu() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<ExcelDto> excelDto = new ArrayList<>();
        // 파일명
        String fileName = "메뉴관리";
        // 시트명
        String sheetName = "메뉴관리";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"메뉴1_ID","메뉴1","메뉴1_사용여부","메뉴2_ID","메뉴2","메뉴2_사용여부"
                                 ,"메뉴3_ID","메뉴3","메뉴3_사용여부","메뉴4_ID","메뉴4","메뉴4_사용여부"
                                 ,"화면ID", "화면명", "화면URL"};

        // body 데이터 영문명
        String[] arrayBodyColNm = {"menuId1","menuNm1","useYn1","menuId2","menuNm2","useYn2"
                                  ,"menuId3","menuNm3","useYn3","menuId4","menuNm4","useYn4"
                                  ,"screnId", "screnNm", "screnUrladdr"};

        // body 데이터
        List<MenuExelDownloadIDto> list = menuRepository.excelDownloadMenu();

        // 헤더
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<String[]> headerList = new ArrayList<>();
        headerList.add(arrayHeaderNm);
        header.setHLength(arrayHeaderNm.length);
        header.setHeaderNm(headerList);

        // body
        ExcelBodyDto body = new ExcelBodyDto();
        body.setBodyColNm(Arrays.asList(arrayBodyColNm));
        body.setBody(list);

        List<ExcelCellType> cellType = new ArrayList<>();
        cellType.add(new ExcelCellType("smsMsgFormId","STRING",0));
        cellType.add(new ExcelCellType("smsMsgCntnt","STRING",10200));
        body.setCellType(cellType);

        excelDto.add(new ExcelDto(sheetName, header, body));
        ExcelUtil.excelDownload(excelDto, fileName);
    }

    @Override
    public ScrenDto findByScrenId(String screnId) {
        if ("".equals(screnId) || screnId == null) throw new ServiceException("COM.I0003");

        Optional<Scren> oScren = screnRepository.findById(screnId);

        if (oScren.isPresent()) {
            Scren scren = oScren.get();
            List<BttnDto> bttnDtoList = bttnRepository.findByScrenId(screnId);

            for (BttnDto bttnDto : bttnDtoList) {
                // API명 가져오기
                if (bttnDto.getApiId() != null && !(0 == bttnDto.getApiId())) {
                    try {
                        String apiNm = "";
                        Optional<ApiInfo> oApiInfo = apiInfoRepository.findById(bttnDto.getApiId());
                        if (oApiInfo.isPresent()) {
                            apiNm = oApiInfo.get().getApiNm();
                        }
                        bttnDto.setApiNm(apiNm);
                    } catch(Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            ScrenDto screnDto = new ScrenDto(scren);
            screnDto.setBttnList(bttnDtoList);
            return screnDto;
        } else {
            throw new ServiceException("COM.I0009");
        }
    }

    @Override
    public ScrenDto makeScren(ScrenDto screnDto) {
        if (!screnRepository.existsById(screnDto.getScrenId())) { return createScren(screnDto); }
        else { return updateScren(screnDto); }
    }
    @Override
    public ScrenDto createScren(ScrenDto screnDto) {

        List<BttnDto> tmpBttnList;
        if (!screnRepository.existsById(screnDto.getScrenId())) {
            tmpBttnList = screnDto.getBttnList();
            for (BttnDto b : tmpBttnList) {
                this.createBttn(b.toEntity());
            }
            Scren entity = screnRepository.save(screnDto.toEntity());
            return entity.toApi();
        } else {
            throw new ServiceException("COM.I0010");
        }
    }

    @Override
    public ScrenDto updateScren(ScrenDto screnDto){

        List<BttnDto> tmpBttnList;

        if (screnRepository.existsById(screnDto.getScrenId())) {
            tmpBttnList = screnDto.getBttnList();
            if (!tmpBttnList.isEmpty()) {
                for (BttnDto b : tmpBttnList) {
                    this.updateBttn(b.toEntity());
                }
            }
            Scren entity = screnRepository.save(screnDto.toEntity());
            return entity.toApi();
        } else {
            throw new ServiceException("COM.I0009");
        }
    }

    public void createBttn(Bttn bttn) {

        if (!bttnRepository.existsById(new BttnPK(bttn.getScrenId(),bttn.getBttnId()))) {
            bttnRepository.save(bttn);
        }
        else {
            throw new ServiceException("COM.I0012");
        }
    }

    public void updateBttn(Bttn bttn){
        bttnRepository.save(bttn);
    }

    @Override
    public Page<ScrenIDto> findUseScren(String chrgTaskGroupCd, String screnClCd, String screnNm, String isUnpaged, Pageable pageable) {

        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if (screnClCd == null) screnClCd = "";
        if (screnNm == null) screnNm = "";
        if ("Y".equals(isUnpaged))   pageable = Pageable.unpaged();

        return  screnRepository.findAllScren(chrgTaskGroupCd, screnClCd, screnNm,"Y", pageable);
    }

    @Override
    public Page<ScrenIDto> findAllScren(String chrgTaskGroupCd, String screnClCd, String screnNm, Pageable pageable){

        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if (screnClCd == null) screnClCd = "";
        if (screnNm == null) screnNm = "";

        return screnRepository.findAllScren(chrgTaskGroupCd, screnClCd, screnNm,"", pageable);
    }

    @Override
    public Page<MenuStatisticsDto> searchMenuStatistics(String chrgTaskGroupCd, String sumrDtFrom, String sumrDtTo, Pageable pageable) {
        if (chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        //조회 시작 날짜와 종료 날짜에 대한 기본 세팅 값 필요
        if (sumrDtFrom == null || sumrDtFrom.isEmpty()) sumrDtFrom = "00010101";
        if (sumrDtTo == null || sumrDtTo.isEmpty()) sumrDtTo = "99991231";

        return menuStatisticsRepository.searchMenuStatistics(chrgTaskGroupCd, sumrDtFrom, sumrDtTo, pageable);
    }

    @Override
    public void makeExcel(String chrgTaskGroupCd, String sumrDtFrom, String sumrDtTo) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        // 파일명
        String fileName = "일일메뉴통계이력(" + sumrDtFrom + "-" + sumrDtTo + ")";

        //시트명
        String sheetName = "메뉴사용통계";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"업무구분","상위메뉴명","메뉴명","접속수량"};

        // 타이틀 설정
        header.setTitle("메뉴사용통계(" + sumrDtFrom + "~" + sumrDtTo + ")");

        // 헤더 길이
        header.setHLength(arrayHeaderNm.length);

        // 헤더 List 생성
        List<String[]> headerList = new ArrayList<>();
        headerList.add(arrayHeaderNm);

        // 헤더 List set
        header.setHeaderNm(headerList);

        // 헤더 Merge 리스트
        header.setHeaderMerge(excelMergeDto);

        // body 데이터 영문명
        String[] arrayBodyColNm = {"chrgTaskGroupNm","superMenuNm", "menuNm", "connQty"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<MenuStatisticsDto> menuStatisticsDtoList = searchMenuStatistics(chrgTaskGroupCd, sumrDtFrom, sumrDtTo, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(menuStatisticsDtoList);

        List<ExcelCellType> excelCellTypeList = new ArrayList<>();
        for (String colNm : bodyColNm) {
            if ("connQty".equals(colNm)) {
                excelCellTypeList.add(new ExcelCellType(colNm, "NUMERIC",0));
            } else {
                excelCellTypeList.add(new ExcelCellType(colNm, "STRING",0));
            }
        }
        body.setCellType(excelCellTypeList);
        excelDto.add(new ExcelDto(sheetName, header, body));

        ExcelUtil.excelDownload(excelDto, fileName);
    }

    @Override
    public String findsystmConnPsbty(String menuId) {

        if ("".equals(menuId) || menuId == null) throw new ServiceException("COM.I0003");

        // 절체 시스템 없으면 + 연관 메뉴 아니면  공백 리턴, 맞으면 절체중인 시스템명 전체 리턴
        String result = "";

        //  1. 현재 절체중인 시스템이 있는지 체크
        List<SystmConnPsbty> systmConnPsbty = systmConnPsbtyRepository.findByConnPsbtyYn("N");
        if (systmConnPsbty == null || systmConnPsbty.isEmpty())  return result;

        // 2. 절체 시스템이 있을 경우 -- 메뉴ID로 시스템 태그 조회
        Optional<Menu> oMenu = menuRepository.findById(menuId);

        if (oMenu.isEmpty()) return result;

        Menu menu = oMenu.get();
        if (StringUtils.isBlank(menu.getLinkaSystmTagCntnt()))   return result;

        // 포함되어 있으면 false 리턴
        for (SystmConnPsbty item : systmConnPsbty) {
            if (menu.getLinkaSystmTagCntnt().contains(item.getLinkaSystmNm())){
                if (result.isEmpty()){
                    result = item.getLinkaSystmNm();
                } else {
                    result = result + "," + item.getLinkaSystmNm();
                }
            }
        }
        return result;

    }
}
