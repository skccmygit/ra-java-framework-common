package com.skcc.ra.account.domain.auth.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScrenBttnPK implements Serializable {
    private String userid;
    private String screnId;
    private String bttnId;
}
