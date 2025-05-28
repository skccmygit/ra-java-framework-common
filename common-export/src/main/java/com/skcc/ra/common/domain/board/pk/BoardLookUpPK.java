package com.skcc.ra.common.domain.board.pk;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardLookUpPK implements Serializable {

    private Long annceNo;

    private String userid;
}