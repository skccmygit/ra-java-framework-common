package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.auth.UserRole;
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
@Schema(name = "UserRoleDto", description = "사용자역할내역")
public class UserRoleDto implements Entitiable<UserRole> {

    @Schema(description = "사용자역할ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleId;

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "권한신청순번")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public UserRole toEntity() {
        UserRole userRole = new UserRole();
        BeanUtils.copyProperties(this, userRole);
        return userRole;
    }
}
