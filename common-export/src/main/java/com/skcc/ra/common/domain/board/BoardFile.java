package com.skcc.ra.common.domain.board;

import com.skcc.ra.common.api.dto.domainDto.BoardFileDto;
import com.skcc.ra.common.domain.board.pk.BoardFilePK;
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
@Table(name = "OCO30110", catalog = "OCO")
@IdClass(BoardFilePK.class)
public class BoardFile extends BaseEntity implements Apiable<BoardFileDto> {

    //첨부파일번호
    @Id
    @Column(name = "ANNCE_NO", length = 5)
    private Long annceNo;

    //첨부파일번호
    @Id
    @Column(name = "ATAC_FILE_NO")
    private Long atacFileNo;

    //삭제여부
    @Column(name = "DEL_YN")
    private String delYn;

    @Override
    public BoardFileDto toApi() {
        BoardFileDto boardFileDto = new BoardFileDto();
        BeanUtils.copyProperties(this, boardFileDto);
        return boardFileDto;
    }
}

