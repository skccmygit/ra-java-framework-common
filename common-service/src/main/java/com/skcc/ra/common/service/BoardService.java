package com.skcc.ra.common.service;

import com.skcc.ra.common.api.dto.domainDto.BoardAuthDto;
import com.skcc.ra.common.api.dto.domainDto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    Page<BoardDto> searchBoardByCondition(String annceTaskClCd, String annceCtgrClCd, String annceTitleNmOrAnnceCntnt,
                                          String regDtmdFrom, String regDtmdTo, String expirationYn, String delYn, Pageable pageable);

    BoardDto searchBoardByAnnceNo(Long annceNo);

    BoardDto createBoard(BoardDto boardDto, List<MultipartFile> boardFileList, List<BoardAuthDto> boardAuthDtoList) throws IOException;

    BoardDto modifyBoard(BoardDto boardDto, List<MultipartFile> boardFileList, List<BoardAuthDto> boardAuthDtoList) throws IOException;

    void deleteBoardByAnnceNo(List<Long> annceNoList);

    Boolean checkUserAuthByAnnceNo(Long annceNo, String deptcd, String cucenTeamCd);

}
