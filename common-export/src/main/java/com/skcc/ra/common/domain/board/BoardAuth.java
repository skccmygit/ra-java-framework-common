package com.skcc.ra.common.domain.board;

import com.skcc.ra.common.api.dto.domainDto.BoardAuthDto;
import com.skcc.ra.common.domain.board.pk.BoardAuthPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OCO30102", catalog = "OCO")
@IdClass(BoardAuthPK.class)
public class BoardAuth extends BaseEntity implements Apiable<BoardAuthDto> {

    //공지번호
    @Id
    @Column(name = "ANNCE_NO", length = 12)
    private Long annceNo;

    //공지대상순번
    @Id
    @Column(name = "ANNCE_OBJ_SEQ")
    private int annceObjSeq;

    //공지대상부서구분코드
    @Column(name = "ANNCE_OBJ_DEPT_CL_CD", length = 2)
    private String annceObjDeptClCd;

    //공지대상부서팀코드
    @Column(name = "ANNCE_OBJ_DEPT_TEAM_CD", length = 6)
    private String annceObjDeptTeamCd;

    @Override
    public BoardAuthDto toApi() {
        BoardAuthDto boardAuthDto = new BoardAuthDto();
        BeanUtils.copyProperties(this, boardAuthDto);
        return boardAuthDto;
    }
}

