package com.skcc.ra.common.api.dto.domainDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skcc.ra.common.domain.board.Board;
import com.skcc.ra.common.jpa.Entitiable;
import com.skcc.ra.common.util.RequestUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BoardDto", description = "공지사항내역")
public class BoardDto implements Entitiable<Board> {

    @Schema(description = "공지번호")
    private Long annceNo;

    @Schema(description = "공지제목명")
    @NotNull
    @Size(min=1, max = 1000)
    private String annceTitleNm;

    @Schema(description = "공지내용", example = "내용")
    @NotNull
    private String annceCntnt;

    @Schema(description = "공지업무구분코드")
    @NotNull
    private String annceTaskClCd;

    @Schema(description = "공지업무구분명")
    private String annceTaskClNm;

    @Schema(description = "공지카테고리구분코드")
    @NotNull
    private String annceCtgrClCd;

    @Schema(description = "공지카테고리구분명")
    private String annceCtgrClNm;

    @Schema(description = "공지시작일시")
    @NotNull
    private String annceStartDtm;

    @Schema(description = "공지종료일시")
    @NotNull
    private String annceEndDtm;

    @Schema(description = "상위공지시작일시")
    private String superAnnceStartDtm;

    @Schema(description = "상위공지종료일시")
    private String superAnnceEndDtm;

    @Schema(description = "등록사용자ID")
    private String regUserid;

    @Schema(description = "등록사용자명")
    private String regUserNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "등록일시")
    private LocalDateTime regDtmd;

    @Schema(description = "만료여부", example = "N")
    private String expirationYn;

    @Schema(description = "삭제여부", example = "N")
    private String delYn;

    @Schema(description = "조회수")
    private Long cnt;

    @Schema(description = "seq")
    private int seqNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "최종수정일자")
    private LocalDateTime lastChngDtmd ;

    @Schema(description = "공지권한")
    private List<BoardAuthDto> boardAuthDtoList;

    @Schema(description = "첨부파일")
    private List<BoardFileDto> boardFileDtoList;

    public Board toEntity() {
        Board board = new Board();
        BeanUtils.copyProperties(this, board);
        board.setAnnceCntnt(RequestUtil.toSafe(this.annceCntnt));
        return board;
    }
}
