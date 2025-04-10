package com.skcc.ra.account.domain.hist.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStsChngPK implements Serializable {
    String userid;
    String chngColEngshNm;
    String chngDtm;
}
