package com.skcc.ra.account.domain.hist.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDeptMappingHisPK implements Serializable {

    String roleDeptTeamCd;
    String roleMappReofoCd;
    String userRoleId;
    String chngDtm;
}
