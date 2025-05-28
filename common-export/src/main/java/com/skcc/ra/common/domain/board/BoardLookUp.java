package com.skcc.ra.common.domain.board;

import com.skcc.ra.common.api.dto.domainDto.BoardLookUpDto;
import com.skcc.ra.common.domain.board.pk.BoardLookUpPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "OCO30101", catalog = "OCO")
@IdClass(BoardLookUpPK.class)
public class BoardLookUp extends BaseEntity implements Apiable<BoardLookUpDto> {

    //공지번호
    @Id
    @Column(name = "ANNCE_NO")
    private Long annceNo;

    //사용자ID
    @Id
    @Column(name = "USERID")
    private String userid;


    @Override
    public BoardLookUpDto toApi() {
        BoardLookUpDto boardLookUpDto = new BoardLookUpDto();
        BeanUtils.copyProperties(this, boardLookUpDto);
        return boardLookUpDto;
    }
}

