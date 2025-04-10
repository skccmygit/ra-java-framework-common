package com.skcc.ra.account.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
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
@Table(name = "OCO10110", catalog = "OCO")
public class Role extends BaseEntity implements Apiable<RoleDto> {
    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Column(name = "USER_ROLE_NM", length = 50)
    private String userRoleNm;

    @Column(name = "USER_ROLE_DESC", length = 200)
    private String userRoleDesc;

    @Column(name = "USER_ATHRTY_GROUP_CD", length = 3)
    private String userAthrtyGroupCd;

    @Column(name = "LOCK_ATHRTY_CL_CD", length = 1)
    private String lockAthrtyClCd;

    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @Override
    public RoleDto toApi() {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(this, roleDto);
        return roleDto;
    }
}
