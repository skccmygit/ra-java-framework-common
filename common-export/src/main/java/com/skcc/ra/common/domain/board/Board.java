package com.skcc.ra.common.domain.board;

import com.skcc.ra.common.api.dto.domainDto.BoardDto;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OCO30100", catalog = "OCO")
@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseEntity implements Apiable<BoardDto> {

    //공지번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANNCE_NO", length = 12)
    private Long annceNo;

    //공지제목명
    @Column(name = "ANNCE_TITLE_NM", length = 100)
    private String annceTitleNm;

    //공지내용
    @Column(name = "ANNCE_CNTNT")
    private String annceCntnt;

    //공지업무구분코드
    //01:부서, 02:고객센터팀코드
    @Column(name = "ANNCE_TASK_CL_CD", length = 2)
    private String annceTaskClCd;

    //공지카테고리구분코드
    @Column(name = "ANNCE_CTGR_CL_CD", length = 2)
    private String annceCtgrClCd;

    //공지시작일시
    @Column(name = "ANNCE_START_DTM", length = 14)
    private String annceStartDtm;

    //공지종료일시
    @Column(name = "ANNCE_END_DTM", length = 14)
    private String annceEndDtm;

    //상위공지시작일시
    @Column(name = "SUPER_ANNCE_START_DTM", length = 14)
    private String superAnnceStartDtm;

    //상위공지종료일시
    @Column(name = "SUPER_ANNCE_END_DTM", length = 14)
    private String superAnnceEndDtm;

    //등록사용자ID
    @Column(name = "REG_USERID", length = 10)
    private String regUserid;

    //등록일시
    @CreatedDate
    @Column(name = "REG_DTMD")
    private LocalDateTime regDtmd;

    //삭제여부
    @Column(name = "DEL_YN", length = 1)
    private String delYn;

    @Override
    public BoardDto toApi() {
        BoardDto boardDto = new BoardDto();
        BeanUtils.copyProperties(this, boardDto);
        return boardDto;
    }
}

