package com.skcc.ra.account.domain.auth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.RoleMenuDto;
import com.skcc.ra.account.domain.auth.pk.RoleMenuPK;
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
@IdClass(RoleMenuPK.class)
@Table(name="OCO10120", catalog = "OCO")
public class RoleMenu extends BaseEntity implements Apiable<RoleMenuDto> {

    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "MENU_ID")
    private String menuId;

    @Override
    public RoleMenuDto toApi() {
        RoleMenuDto roleMenuDto = new RoleMenuDto();
        BeanUtils.copyProperties(this, roleMenuDto);
        return roleMenuDto;
    }
}
