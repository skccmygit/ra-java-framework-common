package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.hist.UserRoleDeptMappingHis;
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
@Schema(name = "UserRoleDeptMappingHisDto", description = "부서, 직책 기준 매핑 이력")
public class UserRoleDeptMappingHisDto  implements Entitiable<UserRoleDeptMappingHis> {

    @Schema(description = "부서/팀코드")
    private String roleDeptTeamCd;

    @Schema(description = "직책코드")
    private String roleMappReofoCd;

    @Schema(description = "역할코드")
    private String userRoleId;

    @Schema(description = "변경일시")
    private String chngDtm;

    @Schema(description = "변경구분")
    private String crudClCd;

    @Schema(description = "변경자IP")
    private String chngUserIpaddr;

    @Schema(description = "승인SEQ")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public UserRoleDeptMappingHisDto(String roleDeptTeamCd, String roleMappReofoCd, String userRoleId,
                                        String crudClCd, Integer athrtyReqstSeq, String chngResonCntnt){
        this.roleDeptTeamCd = roleDeptTeamCd;
        this.roleMappReofoCd = roleMappReofoCd;
        this.userRoleId = userRoleId;
        this.crudClCd = crudClCd;
        this.athrtyReqstSeq = athrtyReqstSeq;
        this.chngResonCntnt = chngResonCntnt;

    }

    public UserRoleDeptMappingHis toEntity() {
        UserRoleDeptMappingHis userRoleDeptMappingHis = new UserRoleDeptMappingHis();
        BeanUtils.copyProperties(this, userRoleDeptMappingHis);
        return userRoleDeptMappingHis;
    }

}
