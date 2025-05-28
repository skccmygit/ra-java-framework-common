package com.skcc.ra.common.api;


import com.skcc.ra.common.api.dto.domainDto.BoardAuthDto;
import com.skcc.ra.common.api.dto.domainDto.BoardDto;
import com.skcc.ra.common.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@Tag(name = "[공지사항] 공지사항 관리(BoardResource)", description = "공지사항 게시판 API")
@RestController
@RequestMapping("/v1/com/common/board")
@Slf4j
public class BoardResource {

    @Autowired
    private BoardService boardService;


    @Operation(summary = "공지사항 조회 - 공지사항 조건 검색-일반")
    @GetMapping
    public ResponseEntity<Page<BoardDto>> searchBoardByCondition(@RequestParam(required = false) String annceTaskClCd,
                                                                 @RequestParam(required = false) String annceCtgrClCd,
                                                                 @RequestParam(required = false) String annceTitleNmOrAnnceCntnt,
                                                                 @RequestParam(required = false) String regDtmdFrom,
                                                                 @RequestParam(required = false) String regDtmdTo,
                                                                 @RequestParam(required = false)  String expirationYn,
                                                                 @RequestParam(required = false) String delYn,
                                                                 Pageable pageable) {
        Page<BoardDto> boardDtoList = boardService.searchBoardByCondition(annceTaskClCd, annceCtgrClCd, annceTitleNmOrAnnceCntnt, regDtmdFrom, regDtmdTo, expirationYn, delYn, pageable);
        return new ResponseEntity<>(boardDtoList, HttpStatus.OK);
    }

    @Operation(summary = "공지사항 조회 - 상세내용 조회")
    @GetMapping("/annceNo")
    public ResponseEntity<BoardDto> searchBoardByAnnceNo(@RequestParam Long annceNo) {
        return new ResponseEntity<>(boardService.searchBoardByAnnceNo(annceNo),HttpStatus.OK);
    }

    @Operation(summary = "공지사항 등록 - 신규 공지사항 등록")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BoardDto> createBoard(@Valid @RequestPart(value = "boardDto") BoardDto boardDto,
                                                @Valid @RequestPart(value = "boardFileList", required = false) List<MultipartFile> boardFileList,
                                                @Valid @RequestPart(value = "boardAuthDtoList") List<BoardAuthDto> boardAuthDtoList) throws IOException {
        return new ResponseEntity<>(boardService.createBoard(boardDto, boardFileList, boardAuthDtoList),HttpStatus.OK);
    }

    @Operation(summary = "공지사항 수정 - 기 등록된 공지사항 내용 수정")
    @PostMapping(value = "/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDto> modifyBoard(@Valid @RequestPart(value = "boardDto") BoardDto boardDto,
                                                @Valid @RequestPart(value = "boardFileList", required = false) List<MultipartFile> boardFileList,
                                                @Valid @RequestPart(value = "boardAuthDtoList") List<BoardAuthDto> boardAuthDtoList) throws IOException {
        return new ResponseEntity<>(boardService.modifyBoard(boardDto, boardFileList, boardAuthDtoList),HttpStatus.OK);
    }

    @Operation(summary = "공지사항 삭제 - 기 등록된 공지사항 삭제(flag 처리)")
    @PutMapping("/delete")
    public ResponseEntity deleteBoardByAnnceNo(@RequestBody List<Long> annceNoList) {
        boardService.deleteBoardByAnnceNo(annceNoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "공지사항 권한체크 - 사용자별 게시글 대한 권한 체크")
    @GetMapping("/userAuth")
    public ResponseEntity<Boolean> checkUserAuthByAnnceNo(@RequestParam Long annceNo,
                                                          @RequestParam(required = false) String deptcd,
                                                          @RequestParam(required = false) String cucenTeamCd) {
        return new ResponseEntity<>(boardService.checkUserAuthByAnnceNo(annceNo, deptcd, cucenTeamCd), HttpStatus.OK);
    }
}
