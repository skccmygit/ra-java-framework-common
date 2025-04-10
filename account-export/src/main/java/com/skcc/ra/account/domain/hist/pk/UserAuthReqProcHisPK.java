package com.skcc.ra.account.domain.hist.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthReqProcHisPK implements Serializable {

    private Integer athrtyReqstSeq;
    private String athrtyReqstOpDtm;
    private String athrtyReqstStsCd;
}
