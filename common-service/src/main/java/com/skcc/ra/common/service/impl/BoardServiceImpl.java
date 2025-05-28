package com.skcc.ra.common.service.impl;

import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.common.adaptor.client.AccountClient;
import com.skcc.ra.common.api.dto.domainDto.AttachFileDto;
import com.skcc.ra.common.api.dto.domainDto.BoardAuthDto;
import com.skcc.ra.common.api.dto.domainDto.BoardDto;
import com.skcc.ra.common.api.dto.domainDto.BoardFileDto;
import com.skcc.ra.common.domain.board.Board;
import com.skcc.ra.common.domain.board.BoardAuth;
import com.skcc.ra.common.domain.board.BoardFile;
import com.skcc.ra.common.domain.board.BoardLookUp;
import com.skcc.ra.common.exception.ServiceException;
import com.skcc.ra.common.repository.BoardAuthRepository;
import com.skcc.ra.common.repository.BoardFileRepository;
import com.skcc.ra.common.repository.BoardLookUpRepository;
import com.skcc.ra.common.repository.BoardRepository;
import com.skcc.ra.common.service.AttachFileService;
import com.skcc.ra.common.service.BoardService;
import com.skcc.ra.common.util.ObjectUtil;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardLookUpRepository boardLookUpRepository;

    @Autowired
    private BoardFileRepository boardFileRepository;

    @Autowired
    private BoardAuthRepository boardAuthRepository;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private AccountClient accountClient;


    @Override
    public Page<BoardDto> searchBoardByCondition(String annceTaskClCd, String annceCtgrClCd, String annceTitleNmOrAnnceCntnt,
                                                 String regDtmdFrom, String regDtmdTo, String expirationYn, String delYn, Pageable pageable) {

        if (annceTaskClCd == null) annceTaskClCd = "";
        if (annceCtgrClCd == null) annceCtgrClCd = "";
        if (annceTitleNmOrAnnceCntnt == null) annceTitleNmOrAnnceCntnt = "";
        if (regDtmdFrom == null || "".equals(regDtmdFrom)) regDtmdFrom = "00010101";
        LocalDateTime regDtmdFromL = LocalDate.parse(regDtmdFrom, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
        if (regDtmdTo == null || "".equals(regDtmdTo)) regDtmdTo = "99991230";
        LocalDateTime regDtmdToL = LocalDate.parse(regDtmdTo, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay().plusDays(1);

        boolean isAdmin = false;
        List<String> roleList = RequestUtil.getLoginUserRoleList();
        if(roleList.contains(AuthType.IT_ADMIN) || roleList.contains(AuthType.AUTH_DEVELOPER)){
            isAdmin = true;
        }
        log.info("isAdmin : {}", isAdmin);
        log.info("RequestUtil.getLoginUserid : {}", RequestUtil.getLoginUserid());
        //관리자 -> 전체 리스트 필요
        if(isAdmin){
            return ObjectUtil.toDtoPage(boardRepository.searchBoardByCondition(annceTaskClCd, annceCtgrClCd, annceTitleNmOrAnnceCntnt,
                                                                                regDtmdFromL, regDtmdToL, expirationYn,  delYn, pageable), BoardDto.class);

        }
        //일반사용자 -> 권한 파악 필요
        else{
            return ObjectUtil.toDtoPage(boardRepository.searchBoardByAuthCondition(annceTaskClCd, annceCtgrClCd, annceTitleNmOrAnnceCntnt,
                                                                                 regDtmdFromL, regDtmdToL, RequestUtil.getLoginUserid(), expirationYn, delYn, pageable), BoardDto.class);
        }
    }

    @Override
    public BoardDto searchBoardByAnnceNo(Long annceNo) {
        boardLookUpRepository.save(BoardLookUp.builder()
                                                .annceNo(annceNo)
                                                .userid(RequestUtil.getLoginUserid())
                                                .build());

        BoardDto boardDto = ObjectUtil.toDto(boardRepository.searchBoardByAnnceNo(annceNo), BoardDto.class);
        List<BoardAuth> boardAuthList = boardAuthRepository.findByAnnceNo(annceNo);
        boardDto.setBoardAuthDtoList(boardAuthList.stream().map(BoardAuth::toApi).collect(Collectors.toList()));
        boardDto.setBoardFileDtoList(boardFileRepository.findByAnnceNo(annceNo));
        return boardDto;
    }

    @Override
    public BoardDto createBoard(BoardDto boardDto, List<MultipartFile> boardFileList, List<BoardAuthDto> boardAuthDtoList) throws IOException {

        //공지사항 입력 없으면 에러
        if(boardDto == null) throw new ServiceException("COM.I2003");

        //권한설정 없으면 에러
        if(boardAuthDtoList == null) throw new ServiceException("COM.I2005");

        boardDto.setRegDtmd(LocalDateTime.now());
        boardDto.setRegUserid(RequestUtil.getLoginUserid());
        boardDto.setDelYn("N");
        //Board 저장
        Board board = boardRepository.save(boardDto.toEntity());

        //BoardAuth 저장
        creatBoardAuth(board.getAnnceNo(), boardAuthDtoList);

        //AttachFile 있는 경우
        if(!(boardFileList == null || boardFileList.isEmpty())) {
            createBoardFile(boardFileList, board.getAnnceNo(), board.getDelYn());
        }

        return board.toApi();
    }

    @Override
    public BoardDto modifyBoard(BoardDto boardDto, List<MultipartFile> boardFileList, List<BoardAuthDto> boardAuthDtoList) throws IOException {

        boolean isAdmin = false;
        List<String> roleList = RequestUtil.getLoginUserRoleList();
        if(roleList.contains(AuthType.IT_ADMIN) || roleList.contains(AuthType.AUTH_DEVELOPER)){
            isAdmin = true;
        }

        if(!StringUtils.equals(RequestUtil.getLoginUserid(), boardDto.getRegUserid()) && !isAdmin)
            throw new ServiceException("COM.I2006");

        //권한설정 없으면 에러
        if(boardAuthDtoList == null) throw new ServiceException("COM.I2005");

        //Board 저장
        Optional<Board> oBoard = boardRepository.findById(boardDto.getAnnceNo());
        if(oBoard.isEmpty())    throw new ServiceException("COM.I2002");

        Board board = oBoard.get();

        boardDto.setDelYn(board.getDelYn());
        boardDto.setRegDtmd(board.getRegDtmd());
        boardRepository.save(boardDto.toEntity());
        //신규 권한 등록
        creatBoardAuth(boardDto.getAnnceNo(), boardAuthDtoList);

        //기존 파일 삭제
        if(boardDto.getBoardFileDtoList() != null && boardDto.getBoardFileDtoList().size() != 0) deleteFile(boardDto.getBoardFileDtoList());
        //신규파일 등록
        if(boardFileList != null) createBoardFile(boardFileList, boardDto.getAnnceNo(), "N");

        return boardDto;
    }


    @Override
    public void deleteBoardByAnnceNo(List<Long> annceNoList) {

        boolean isAdmin = false;
        List<String> roleList = RequestUtil.getLoginUserRoleList();
        if(roleList.contains(AuthType.IT_ADMIN) || roleList.contains(AuthType.AUTH_DEVELOPER)){
            isAdmin = true;
        }

        for(Long annceNo : annceNoList) {
            BoardDto boardDto = ObjectUtil.toDto(boardRepository.searchBoardByAnnceNo(annceNo), BoardDto.class);
            if(!StringUtils.equals(RequestUtil.getLoginUserid(), boardDto.getRegUserid()) && !isAdmin)
                throw new ServiceException("COM.I2007");
            // 삭제복구 때문에 toggle 처리
            boardDto.setDelYn("Y".equals(boardDto.getDelYn()) ? "N" : "Y");
            boardRepository.save(boardDto.toEntity());
        }
    }

    @Override
    public Boolean checkUserAuthByAnnceNo(Long annceNo, String deptcd, String cucenTeamCd) {

        if(StringUtils.isBlank(deptcd) && StringUtils.isBlank(cucenTeamCd)){
            AccountDto accountDto = accountClient.searchAccountByUserid(RequestUtil.getLoginUserid());
            deptcd = accountDto.getDeptcd();
        }

        int cnt;
        if(StringUtils.isNotBlank(deptcd)){
            cnt = boardAuthRepository.checkUserAuth(annceNo, "01", deptcd);
            return cnt > 0;
        }

        return false;
    }

    public void creatBoardAuth(Long annceNo,  List<BoardAuthDto> boardAuthDtoList){

        //기존 권한 삭제(수정일 경우)
        boardAuthRepository.deleteByAnnceNo(annceNo);

        List<BoardAuth> boardAuthList = new ArrayList<>();

        int maxNum = boardAuthRepository.findMaxSeq(annceNo);

        for (BoardAuthDto boardAuthDto : boardAuthDtoList) {

            BoardAuth boardAuth = boardAuthDto.toEntity();
            boardAuth.setAnnceNo(annceNo);
            boardAuth.setAnnceObjSeq(maxNum++);

            boardAuthList.add(boardAuth);
        }
        boardAuthRepository.saveAll(boardAuthList);
    }

    //공지사항 첨부파일 저장
    public void createBoardFile(List<MultipartFile> boardFileList, Long annceNo, String delYn) throws IOException {
        //AttachFile 업로드
        List<AttachFileDto> attachFileDtoList = attachFileService.fileUpload(boardFileList, "19");
        List<BoardFile> boardFiles = new ArrayList<>();

        if(attachFileDtoList == null) return;
        if(attachFileDtoList.size() == 0) return;

        //Board-AttachFile Mapping 저장
        for (AttachFileDto attachFileDto : attachFileDtoList) {
            BoardFile boardFile = new BoardFile();

            boardFile.setAnnceNo(annceNo);
            boardFile.setAtacFileNo(attachFileDto.getAtacFileNo());
            boardFile.setDelYn(delYn);
            boardFiles.add(boardFile);
        }
        boardFileRepository.saveAll(boardFiles);
    }

    private void deleteFile(List<BoardFileDto> boardFiles) {

        List<Long> deleteBoardFileList = new ArrayList<>();
        //파일 삭제 건
        for(BoardFileDto item : boardFiles) {
            if ("Y".equals(item.getDelYn())) {
                deleteBoardFileList.add(item.getAtacFileNo());
            }
        }
        if(!deleteBoardFileList.isEmpty()) {
            deleteBoardFile(deleteBoardFileList);
            attachFileService.removeFileSoft(deleteBoardFileList);
        }
    }

    private void deleteBoardFile(List<Long> atacFileNoList) {

        List<BoardFile> boardFileList = new ArrayList<>();

        for(Long item : atacFileNoList) {
            BoardFile boardFile = boardFileRepository.findByAtacFileNo(item);
            boardFile.setDelYn("Y");

            boardFileList.add(boardFile);
        }
        boardFileRepository.saveAll(boardFileList);
    }
}