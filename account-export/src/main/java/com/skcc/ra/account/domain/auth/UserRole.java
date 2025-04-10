package com.skcc.ra.account.domain.auth;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDto;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserRolePK.class)
@Table(name="OCO10101", catalog = "OCO")
public class UserRole extends BaseEntity implements Apiable<UserRoleDto> {
    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

    @Override
    public UserRoleDto toApi() {
        UserRoleDto userRoleDto = new UserRoleDto();
        BeanUtils.copyProperties(this, userRoleDto);
        return userRoleDto;
    }
}

