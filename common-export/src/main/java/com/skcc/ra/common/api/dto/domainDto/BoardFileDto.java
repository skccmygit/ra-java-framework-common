package com.skcc.ra.common.api.dto.domainDto;

import com.skcc.ra.common.domain.board.BoardFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BoardFileDto", description = "공지사항첨부파일")
public class BoardFileDto {

    @Schema(description = "공지번호", required = true)
    private Long annceNo;

    @Schema(description = "첨부파일번호", required = true)
    private Long atacFileNo;

    @Schema(description = "삭제여부")
    private String delYn;

    private String atacFileNm;

    private String atacFileType;

    private Integer atacFileSize;

    public BoardFile toEntity() {
        BoardFile boardFile = new BoardFile();
        BeanUtils.copyProperties(this, boardFile);
        return boardFile;
    }

    public BoardFileDto(BoardFile boardFile, String atacFileNm, String atacFileType, Integer atacFileSize) {
        this.annceNo = boardFile.getAnnceNo();
        this.atacFileNo = boardFile.getAtacFileNo();
        this.delYn = boardFile.getDelYn();
        this.atacFileNm = atacFileNm;
        this.atacFileType = atacFileType;
        this.atacFileSize = atacFileSize;
    }
}
