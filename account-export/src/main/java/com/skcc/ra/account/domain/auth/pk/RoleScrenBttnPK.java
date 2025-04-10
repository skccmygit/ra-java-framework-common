package com.skcc.ra.account.domain.auth.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleScrenBttnPK implements Serializable {
    String screnId;
    String bttnId;
    String userRoleId;
}
