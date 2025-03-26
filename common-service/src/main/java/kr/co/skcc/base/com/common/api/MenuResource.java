package kr.co.skcc.base.com.common.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.skcc.base.com.common.api.dto.domainDto.MenuDto;
import kr.co.skcc.base.com.common.api.dto.domainDto.MenuStatisticsDto;
import kr.co.skcc.base.com.common.api.dto.domainDto.ScrenDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.MenuIDto;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.ScrenIDto;
import kr.co.skcc.base.com.common.service.MenuService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "[메뉴관리] 메뉴 관리 API(MenuResource)", description = "메뉴 / 화면 / 버튼 관리를 위한 API")
@RestController
@RequestMapping("/v1/com/common/menu")
@Slf4j
public class MenuResource {
    @Autowired
    private MenuService menuService;

    @Operation(summary = "메뉴 등록,수정 - 존재여부 기준")
    @PostMapping
    public ResponseEntity<MenuDto> makeMenu(@RequestBody MenuDto menuDto) {
        return new ResponseEntity<>(menuService.makeMenu(menuDto), HttpStatus.OK);
    }
    
    @Operation(summary = "메뉴 조회 - 전체 메뉴")
    @GetMapping
    public ResponseEntity<List<MenuIDto>> findMenuAll(){
        return new ResponseEntity<>(menuService.findAll(), HttpStatus.OK);
    }
    
    @Operation(summary = "메뉴 조회 - 사용 중인 메뉴")
    @GetMapping("/use")
    public ResponseEntity<List<MenuIDto>> findUseMenuAll(){
        return new ResponseEntity<>(menuService.findUseAll(), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 조회 - 화면 ID 기준")
    @GetMapping("/screnId")
    public ResponseEntity<MenuDto> findbyScrenId(@RequestParam String screnId ){
        return new ResponseEntity<>(menuService.findbyScrenId(screnId), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 상세 조회 - 메뉴 ID 기준")
    @GetMapping("/dtl")
    public ResponseEntity<MenuDto> findByMenuId(@RequestParam String menuId){
        return new ResponseEntity<>(menuService.findByMenuId(menuId), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 엑셀 다운로드 - 전체 메뉴")
    @GetMapping("/excel")
    public ResponseEntity<HttpStatus> excelDownMenu() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        menuService.excelDownMenu();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "화면 등록,수정 - 존재여부 기준(버튼 포함)")
    @PostMapping("/scren")
    public ResponseEntity<ScrenDto> makeScren(@RequestBody ScrenDto screnDto) {
        return new ResponseEntity<>(menuService.makeScren(screnDto), HttpStatus.OK);
    }

    @Operation(summary = "화면 조회 - 전체 화면(버튼 포함)")
    @GetMapping("/scren")
    public ResponseEntity<Page<ScrenIDto>> findAllScren(@RequestParam(required = false) String chrgTaskGroupCd,
                                                        @RequestParam(required = false) String screnClCd ,
                                                        @RequestParam(required = false) String screnNm,
                                                        Pageable pageable){
        return new ResponseEntity<>(menuService.findAllScren(chrgTaskGroupCd, screnClCd, screnNm, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "화면 조회 - 사용 중인 화면(버튼 포함)")
    @GetMapping("/scren/use")
    public ResponseEntity<Page<ScrenIDto>> findUseScren(@RequestParam(required = false) String chrgTaskGroupCd,
                                                        @RequestParam(required = false) String screnClCd,
                                                        @RequestParam(required = false)String screnNm,
                                                        @RequestParam(required = false, defaultValue = "N") String isUnpaged,
                                                        Pageable pageable ){
        return new ResponseEntity<>(menuService.findUseScren(chrgTaskGroupCd, screnClCd, screnNm, isUnpaged, pageable), HttpStatus.OK);
    }
    
    @Operation(summary = "화면 상세 조회 - 화면 ID 기준(버튼 포함)")
    @GetMapping("/scren/dtl")
    public ResponseEntity<ScrenDto> findByScrenId(@RequestParam(required = false) String screnId){
        return new ResponseEntity<>(menuService.findByScrenId(screnId), HttpStatus.OK);
    }

    
    @Operation(summary = "메뉴접속통계 조회 - 조건별")
    @GetMapping("/statistics")
    public ResponseEntity<Page<MenuStatisticsDto>> searchMenuStatistics(@RequestParam(required = false) String chrgTaskGroupCd,
                                                                        @RequestParam(required = false) String sumrDtFrom,
                                                                        @RequestParam(required = false) String sumrDtTo,
                                                                        Pageable pageable) {
        return new ResponseEntity<>(menuService.searchMenuStatistics(chrgTaskGroupCd, sumrDtFrom, sumrDtTo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "메뉴접속통계 엑셀다운로드")
    @GetMapping("/statistics/excel")
    public ResponseEntity<HttpStatus> downloadMenuStatistics(@RequestParam(required = false) String chrgTaskGroupCd,
                                                 @RequestParam String sumrDtFrom,
                                                 @RequestParam String sumrDtTo) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException{
        menuService.makeExcel(chrgTaskGroupCd, sumrDtFrom, sumrDtTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "연동 시스템 절체여부 체크 - 메뉴 ID 기준")
    @GetMapping("/systmConn")
    public ResponseEntity<String> findsystmConnPsbty(@RequestParam String menuId) {
        return new ResponseEntity<>(menuService.findsystmConnPsbty(menuId), HttpStatus.OK);
    }
}
