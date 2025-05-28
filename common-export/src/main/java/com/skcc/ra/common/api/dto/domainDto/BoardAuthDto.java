package com.skcc.ra.common.api.dto.domainDto;

import com.skcc.ra.common.domain.board.BoardAuth;
import com.skcc.ra.common.jpa.Entitiable;
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
@Schema(name = "BoardAuthDto", description = "공지사항권한관리")
public class BoardAuthDto implements Entitiable<BoardAuth> {

    @Schema(description = "공지번호")
    private Long annceNo;

    @Schema(description = "공지대상순번")
    private int annceObjSeq;

    @Schema(description = "공지대상부서구분코드")
    private String annceObjDeptClCd;

    @Schema(description = "공지대상부서팀코드")
    private String annceObjDeptTeamCd;

    public BoardAuth toEntity() {
        BoardAuth boardAuth = new BoardAuth();
        BeanUtils.copyProperties(this, boardAuth);
        return boardAuth;
    }

    public BoardAuthDto(BoardAuth boardAuth) {
        this.annceNo = boardAuth.getAnnceNo();
        this.annceObjSeq = boardAuth.getAnnceObjSeq();
        this.annceObjDeptClCd = boardAuth.getAnnceObjDeptClCd();
        this.annceObjDeptTeamCd = boardAuth.getAnnceObjDeptTeamCd();
    }
}
