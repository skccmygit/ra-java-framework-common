package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.domain.baseAuth.UserRoleDeptMapping;
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
@Schema(name = "UserRoleDeptMappingDto", description = "부서, 직책, 직군")
public class UserRoleDeptMappingDto implements Entitiable<UserRoleDeptMapping> {

    @Schema(description = "역할부서팀코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleDeptTeamCd;

    @Schema(description = "역할부서팀명")
    private String roleDeptTeamNm;

    @Schema(description = "역할매핑직책코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleMappReofoCd;

    @Schema(description = "역할매핑직책명")
    private String roleMappReofoNm;

    @Schema(description = "사용자역할ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userRoleId;

    @Schema(description = "사용자역할명")
    private String userRoleNm;

    @Schema(description = "역할부서팀구분코드")
    private String roleDeptTeamClCd;

    @Schema(description = "역할부서팀구분명")
    private String roleDeptTeamClNm;

    @Schema(description = "계정자동생성여부")
    private String accntCratAutoYn;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "권한여부")
    private String authYn;

    @Schema(description = "승인SEQ")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public UserRoleDeptMappingDto(UserRoleDeptMapping t, String roleDeptTeamNm, String roleMappReofoNm, String roleDeptTeamClCd,
                            String accntCratAutoYn, String useYn ) {
        this.roleDeptTeamCd = t.getRoleDeptTeamCd();
        this.roleMappReofoCd= t.getRoleMappReofoCd();
        this.userRoleId = t.getUserRoleId();
        this.roleDeptTeamNm = roleDeptTeamNm;
        this.roleMappReofoNm = roleMappReofoNm;
        this.roleDeptTeamClCd = roleDeptTeamClCd;
        this.accntCratAutoYn = accntCratAutoYn;
        this.useYn = useYn;
    }

    public UserRoleDeptMappingDto(String roleDeptTeamClCd, String roleDeptTeamClNm, String roleDeptTeamCd, String roleDeptTeamNm, String roleMappReofoCd, String roleMappReofoNm
                                ,String userRoleId, String userRoleNm, String useYn, String authYn){
        this.roleDeptTeamClCd = roleDeptTeamClCd;
        this.roleDeptTeamClNm = roleDeptTeamClNm;
        this.roleDeptTeamCd = roleDeptTeamCd;
        this.roleMappReofoCd = roleMappReofoCd;
        this.userRoleId = userRoleId;
        this.useYn = useYn;
        this.accntCratAutoYn = useYn; // 일단 useYn 값으로 입력
        this.authYn = authYn;

        this.roleDeptTeamNm = roleDeptTeamNm;
        this.roleMappReofoNm = roleMappReofoNm;
        this.userRoleNm =  userRoleNm;
    }

    public UserRoleDeptMapping toEntity() {
        UserRoleDeptMapping userRoleDeptMapping = new UserRoleDeptMapping();
        BeanUtils.copyProperties(this, userRoleDeptMapping);
        return userRoleDeptMapping;
    }
}
