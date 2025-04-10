package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.auth.Role;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RoleDto", description = "사용자역할기본")
public class RoleDto implements Entitiable<Role> {

    @Schema(description = "사용자역할ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleId;

    @Schema(description = "사용자역할명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleNm;

    @Schema(description = "사용자역할 설명")
    private String userRoleDesc;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "사용자권한그룹코드")
    private String userAthrtyGroupCd;

    @Schema(description = "잠금권한구분코드")
    private String lockAthrtyClCd;

    @Schema(description = "권한여부")
    private String authYn;

    public RoleDto(Role t){
        BeanUtils.copyProperties(t, this);
    }
    public RoleDto(Role t, String authYn){
        this(t);
        this.authYn = authYn;
    }

    public Role toEntity() {
        Role role = new Role();
        BeanUtils.copyProperties(this, role);
        return role;
    }
}
