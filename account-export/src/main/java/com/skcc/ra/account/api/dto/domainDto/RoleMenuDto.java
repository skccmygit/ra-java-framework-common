package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.auth.RoleMenu;
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
@Schema(name = "RoleMenuDto", description = "역할별메뉴")
public class RoleMenuDto implements Entitiable<RoleMenu> {

    @Schema(description = "사용자역할ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleId;

    @Schema(description = "메뉴ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuId;

    @Schema(description = "권한 추가/삭제", requiredMode = Schema.RequiredMode.REQUIRED)
    private String authYn;

    @Schema(description = "권한신청순번")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public RoleMenu toEntity() {
        RoleMenu roleMenu = new RoleMenu();
        BeanUtils.copyProperties(this, roleMenu);
        return roleMenu;
    }
}
