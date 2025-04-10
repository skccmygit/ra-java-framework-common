package com.skcc.ra.account.domain.hist.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuHistPK implements Serializable {
    String userid;
    String menuId;
    String chngDtm;
    String crudClCd;
}
