package com.skcc.ra.account.domain.auth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.RoleScrenBttnDto;
import com.skcc.ra.account.domain.auth.pk.RoleScrenBttnPK;
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
@IdClass(RoleScrenBttnPK.class)
@Table(name="OCO10121", catalog = "OCO")
public class RoleScrenBttn extends BaseEntity implements Apiable<RoleScrenBttnDto> {
    @Id
    @Column(name = "SCREN_ID", length = 10)
    private String screnId;

    @Id
    @Column(name = "BTTN_ID", length = 10)
    private String bttnId;

    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Override
    public RoleScrenBttnDto toApi() {
        RoleScrenBttnDto roleScrenBttnDto = new RoleScrenBttnDto();
        BeanUtils.copyProperties(this, roleScrenBttnDto);
        return roleScrenBttnDto;
    }
}
