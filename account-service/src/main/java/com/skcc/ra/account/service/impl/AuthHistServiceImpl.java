package com.skcc.ra.account.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.skcc.ra.account.api.dto.responseDto.ifDto.RoleHistDto;
import com.skcc.ra.account.repository.RoleRepository;
import com.skcc.ra.account.repository.UserMenuHistRepository;
import com.skcc.ra.account.repository.UserScrenBttnHistRepository;
import com.skcc.ra.account.service.AuthHistService;
import com.skcc.ra.common.api.dto.excelDto.ExcelBodyDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelHeaderDto;
import com.skcc.ra.common.api.dto.excelDto.ExcelMergeDto;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AuthHistServiceImpl implements AuthHistService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMenuHistRepository userMenuHistRepository;

    @Autowired
    UserScrenBttnHistRepository userScrenBttnHistRepository;

    @Override
    public Page<RoleHistDto> findMenuRoleHist(String userRoleNm, String chrgTaskGroupCd, String menuNm, String startDt, String endDt, String chngrNm, Pageable pageable) {

        if(startDt == null || endDt == null) throw new ServiceException("COM.I1027");
        if(userRoleNm == null) userRoleNm = "";
        if(chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if(menuNm == null) menuNm = "";
        if(chngrNm == null) chngrNm = "";

        startDt = startDt + "000000";
        endDt = endDt + "999999";

        return roleRepository.findMenuRoleHist(userRoleNm, chrgTaskGroupCd, menuNm, startDt, endDt, chngrNm, pageable);

    }

    @Override
    public void makeMenuHistExcel(String userRoleNm, String chrgTaskGroupCd, String menuNm, String startDt, String endDt, String chngrNm) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "역할변경이력조회(메뉴)(" + startDt + "-" + endDt + ")"  ;
        //시트명 세팅
        String sheetName = "변경이력";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"역할ID","역할명","업무구분","상위메뉴명","메뉴명"
                , "변경구분","변경자ID","변경자명", "변경일시", "변경자IP", "승인SEQ", "승인사유", "변경사유"};

        // 타이틀 설정
        // header.setTitle("역할 변경 이력 조회(메뉴) (" + startDt + "~" + endDt + ")");

        // 헤더 길이
        header.setHLength(arrayHeaderNm.length);

        // 헤더 List 생성
        List<String[]> headerList = new ArrayList();
        headerList.add(arrayHeaderNm);

        // 헤더 List set
        header.setHeaderNm(headerList);

        // 헤더 Merge 리스트
        header.setHeaderMerge(excelMergeDto);

        // body 데이터 영문명
        String[] arrayBodyColNm = {"userRoleId", "userRoleNm", "chrgTaskGroupNm", "superMenuNm", "menuNm"
                ,"crudClCd", "lastChngrId", "lastChngrNm", "chngrDtm", "ipAddr", "athrtyReqstSeq", "apprvResonCntnt", "chngResonCntnt"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<RoleHistDto> roleHistDtoList = findMenuRoleHist(userRoleNm, chrgTaskGroupCd, menuNm, startDt, endDt, chngrNm, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(roleHistDtoList);
        excelDto.add(new ExcelDto(sheetName, header, body));

        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e){
            throw new ServiceException("COM.I0034");
        }
    }

    @Override
    public Page<RoleHistDto> findScrenRoleHist(String userRoleNm, String chrgTaskGroupCd, String screnNm, String startDt, String endDt, String chngrNm, Pageable pageable) {
        if(startDt == null || endDt == null) throw new ServiceException("COM.I1027");
        if(userRoleNm == null) userRoleNm = "";
        if(chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if(screnNm == null) screnNm = "";
        if(chngrNm == null) chngrNm = "";

        startDt = startDt + "000000";
        endDt = endDt + "999999";

        return roleRepository.findScrenRoleHist(userRoleNm, chrgTaskGroupCd, screnNm, startDt, endDt, chngrNm, pageable);
    }

    @Override
    public void makeScrenHistExcel(String userRoleNm, String chrgTaskGroupCd, String screnNm, String startDt, String endDt, String chngrNm) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "역할변경이력(화면/버튼)(" + startDt + "-" + endDt + ")"  ;
        //시트명 세팅
        String sheetName = "변경이력";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"역할ID", "역할명", "업무구분", "화면명", "버튼명"
                , "변경구분", "변경자ID", "변경자명", "변경일시", "변경자IP", "승인SEQ", "승인사유", "변경사유" };

        // 타이틀 설정
        // header.setTitle("역할 변경 이력 조회(화면/버튼) (" + startDt + "~" + endDt + ")");

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
        String[] arrayBodyColNm = {"userRoleId", "userRoleNm", "chrgTaskGroupNm", "screnNm", "bttnNm"
                ,"crudClCd", "lastChngrId", "lastChngrNm", "chngrDtm","ipAddr", "athrtyReqstSeq", "apprvResonCntnt", "chngResonCntnt"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<RoleHistDto> roleHistDtoList = findScrenRoleHist(userRoleNm, chrgTaskGroupCd, screnNm, startDt, endDt, chngrNm, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(roleHistDtoList);
        excelDto.add(new ExcelDto(sheetName, header, body));

        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e){
            throw new ServiceException("COM.I0034");
        }
    }

    @Override
    public Page<RoleHistDto> findUserRoleHist(String userRoleNm, String userNm, String startDt, String endDt, String chngrNm, Pageable pageable) {
        if(startDt == null || endDt == null) throw new ServiceException("COM.I1027");
        if(userRoleNm == null) userRoleNm = "";
        if(userNm == null) userNm = "";
        if(chngrNm == null) chngrNm = "";

        startDt = startDt + "000000";
        endDt = endDt + "999999";

        return roleRepository.findUserRoleHist(userRoleNm, userNm, startDt, endDt, chngrNm, pageable);
    }

    @Override
    public void makeUserHistExcel(String userRoleNm, String userNm, String startDt, String endDt, String chngrNm) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "사용자권한변경이력(역할)(" + startDt + "-" + endDt + ")"  ;
        //시트명 세팅
        String sheetName = "변경이력";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"역할ID", "역할명", "부서명", "사용자ID", "사용자명"
                , "변경구분", "변경자ID", "변경자명", "변경일시","변경자IP", "승인SEQ", "승인사유", "변경사유"};

        // 타이틀 설정
        // header.setTitle("역할 변경 이력 조회(사용자) (" + startDt + "~" + endDt + ")");

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
        String[] arrayBodyColNm = {"userRoleId", "userRoleNm", "deptNm", "userid", "userNm"
                ,"crudClCd", "lastChngrId", "lastChngrNm", "chngrDtm", "ipAddr", "athrtyReqstSeq", "apprvResonCntnt", "chngResonCntnt"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<RoleHistDto> roleHistDtoList = findUserRoleHist(userRoleNm, userNm, startDt, endDt, chngrNm, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(roleHistDtoList);
        excelDto.add(new ExcelDto(sheetName, header, body));

        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e){
            throw new ServiceException("COM.I0034");
        }
    }

    @Override
    public Page<RoleHistDto> addAuthHistoryMenu(String userNm, String menuNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm, Pageable pageable) {
        if(startDt == null || endDt == null) throw new ServiceException("COM.I1027");
        if(menuNm == null) menuNm = "";
        if(chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if(userNm == null) userNm = "";
        if(chngrNm == null) chngrNm = "";

        startDt = startDt + "000000";
        endDt = endDt + "999999";

        return userMenuHistRepository.searchAddAuthHistoryMenu(userNm, menuNm, chrgTaskGroupCd, startDt, endDt, chngrNm, pageable);
    }
    @Override
    public void addAuthHistoryMenuExcel(String userNm, String menuNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "사용자추가권한변경이력(메뉴)(" + startDt + "-" + endDt + ")"  ;
        //시트명 세팅
        String sheetName = "변경이력";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"부서명", "사용자ID", "사용자명", "업무구분", "상위메뉴명", "메뉴명", "변경구분", "변경자ID", "변경자명",
                "변경일시", "변경자IP", "승인SEQ", "승인사유", "변경사유"};

        // 타이틀 설정
        // header.setTitle("사용자추가권한변경이력(메뉴) (" + startDt + "~" + endDt + ")");

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
        String[] arrayBodyColNm = {"deptNm", "userid", "userNm" , "chrgTaskGroupNm", "superMenuNm", "menuNm", "crudClCd", "lastChngrId", "lastChngrNm",
                "chngrDtm", "ipAddr", "athrtyReqstSeq", "apprvResonCntnt", "chngResonCntnt"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<RoleHistDto> roleHistDtoList = addAuthHistoryMenu(userNm, menuNm, chrgTaskGroupCd, startDt, endDt, chngrNm, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(roleHistDtoList);
        excelDto.add(new ExcelDto(sheetName, header, body));
        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e){
            throw new ServiceException("COM.I0034");
        }
    }
    @Override
    public Page<RoleHistDto> addAuthHistoryScren(String userNm, String screnNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm, Pageable pageable) {
        if(startDt == null || endDt == null) throw new ServiceException("COM.I1027");
        if(screnNm == null) screnNm = "";
        if(chrgTaskGroupCd == null) chrgTaskGroupCd = "";
        if(userNm == null) userNm = "";
        if(chngrNm == null) chngrNm = "";

        startDt = startDt + "000000";
        endDt = endDt + "999999";

        return userScrenBttnHistRepository.searchAddAuthHistoryScren(userNm, screnNm, chrgTaskGroupCd, startDt, endDt, chngrNm, pageable);
    }

    @Override
    public void addAuthHistoryScrenExcel(String userNm, String screnNm, String chrgTaskGroupCd, String startDt, String endDt, String chngrNm) {
        List<ExcelDto> excelDto = new ArrayList<>();
        ExcelHeaderDto header = new ExcelHeaderDto();
        List<ExcelMergeDto> excelMergeDto = new ArrayList<>();
        ExcelBodyDto body = new ExcelBodyDto();

        String fileName = "사용자추가권한변경이력(화면/버튼)(" + startDt + "-" + endDt + ")"  ;
        //시트명 세팅
        String sheetName = "변경이력";

        // 헤더 세팅 Row 별 column merge
        String[] arrayHeaderNm = {"부서명", "사용자ID", "사용자명", "업무구분", "상위메뉴명", "메뉴명", "변경구분", "변경자ID", "변경자명",
                "변경일시", "변경자IP", "승인SEQ", "승인사유", "변경사유"};

        // 타이틀 설정
        // header.setTitle("사용자추가권한변경이력(화면/버튼) (" + startDt + "~" + endDt + ")");

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
        String[] arrayBodyColNm = {"deptNm", "userid", "userNm" , "chrgTaskGroupNm", "screnNm", "bttnNm", "crudClCd", "lastChngrId", "lastChngrNm",
                "chngrDtm", "ipAddr", "athrtyReqstSeq", "apprvResonCntnt", "chngResonCntnt"};
        List<String> bodyColNm = Arrays.asList(arrayBodyColNm);

        // body 생성
        List<RoleHistDto> roleHistDtoList = addAuthHistoryScren(userNm, screnNm, chrgTaskGroupCd, startDt, endDt, chngrNm, Pageable.unpaged()).toList();

        body.setBodyColNm(bodyColNm);
        body.setBody(roleHistDtoList);
        excelDto.add(new ExcelDto(sheetName, header, body));
        try {
            ExcelUtil.excelDownload(excelDto, fileName);
        } catch (Exception e){
            throw new ServiceException("COM.I0034");
        }
    }
}