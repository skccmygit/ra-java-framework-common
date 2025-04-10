package com.skcc.ra.account.domain.userSpecificMenu.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyViewDtlPK  implements Serializable {

    private int userScrenCnstteSeq;
    private String screnId;
}
