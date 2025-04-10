package com.skcc.ra.account.domain.baseAuth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDeptMappingDto;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptMappingPK;
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
@IdClass(UserRoleDeptMappingPK.class)
@Table(name="OCO10113", catalog = "OCO")
public class UserRoleDeptMapping extends BaseEntity implements Apiable<UserRoleDeptMappingDto> {
//부서/직군/직책별 역할 매핑

    @Id
    @Column(name = "ROLE_DEPT_TEAM_CD", length = 6)
    private String roleDeptTeamCd;

    @Id
    @Column(name = "ROLE_MAPP_REOFO_CD", length = 4)
    private String roleMappReofoCd;

    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    public UserRoleDeptMapping(UserRoleDeptMappingPK pk) {
        BeanUtils.copyProperties(pk, this);
    }

    @Override
    public UserRoleDeptMappingDto toApi() {
        UserRoleDeptMappingDto userRoleDeptMappingDto = new UserRoleDeptMappingDto();
        BeanUtils.copyProperties(this, userRoleDeptMappingDto);
        return userRoleDeptMappingDto;
    }
}
