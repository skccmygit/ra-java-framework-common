package com.skcc.ra.account.domain.baseAuth.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDeptPK implements Serializable {

    String roleDeptTeamCd;
    String roleMappReofoCd;
}
