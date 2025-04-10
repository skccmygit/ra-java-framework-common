package com.skcc.ra.account.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.account.api.dto.domainDto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInfoDto", description = "로그인  시 전달될 사용자 정보")
public class UserInfoDto{

    /* JwtResponse 데이터
     * ID / 이름 / 부서코드 / 부서명 / 고객센터팀코드 / 고객센터팀명 / CTI 번호 / 직책코드 / 직책명/ 역할
     */

    @Schema(description = "사용자ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userid;

    @Schema(description = "사용자명")
    private String userNm;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "적용종료일")
    private String ctiAgentId;

    @Schema(description = "직책코드")
    private String reofoCd;

    @Schema(description = "직책명")
    private String reofoNm;

    @Schema(description = "역할")
    private Set<RoleDto> roles;

}
