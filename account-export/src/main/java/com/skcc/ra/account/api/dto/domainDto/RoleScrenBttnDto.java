package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.auth.RoleScrenBttn;
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
@Schema(name = "RoleScrenBttnDto", description = "역할별화면버튼")
public class RoleScrenBttnDto implements Entitiable<RoleScrenBttn> {

    @Schema(description = "화면ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String screnId;

    @Schema(description = "버튼ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bttnId;

    @Schema(description = "사용자역할ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleId;

    @Schema(description = "권한 추가/삭제", requiredMode = Schema.RequiredMode.REQUIRED)
    private String authYn;

    @Schema(description = "권한신청순번")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public RoleScrenBttn toEntity() {
        RoleScrenBttn roleScrenBttn = new RoleScrenBttn();
        BeanUtils.copyProperties(this, roleScrenBttn);
        return roleScrenBttn;
    }
}
