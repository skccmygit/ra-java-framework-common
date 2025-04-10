package com.skcc.ra.account.domain.baseAuth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDeptDto;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserRoleDeptPK.class)
@Table(name="OCO10111", catalog = "OCO")
public class UserRoleDept extends BaseEntity implements Apiable<UserRoleDeptDto> {

    @Id
    @Column(name = "ROLE_DEPT_TEAM_CD", length = 6)
    private String roleDeptTeamCd;

    @Id
    @Column(name = "ROLE_MAPP_REOFO_CD", length = 4)
    private String roleMappReofoCd;

    @Column(name = "ROLE_DEPT_TEAM_CL_CD")
    private String roleDeptTeamClCd;

    @Column(name = "ACCNT_CRAT_AUTO_YN")
    private String accntCratAutoYn;

    @Column(name = "USE_YN")
    private String useYn;

    @Override
    public UserRoleDeptDto toApi() {
        UserRoleDeptDto userRoleDeptDto = new UserRoleDeptDto();
        BeanUtils.copyProperties(this, userRoleDeptDto);
        return userRoleDeptDto;
    }
}
