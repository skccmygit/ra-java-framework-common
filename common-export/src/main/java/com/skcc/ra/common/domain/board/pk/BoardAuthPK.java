//복합키 사용 시 필요
package com.skcc.ra.common.domain.board.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardAuthPK implements Serializable {

    //공지번호
    private Long annceNo;
    //통합조직코드
    private int annceObjSeq;

}