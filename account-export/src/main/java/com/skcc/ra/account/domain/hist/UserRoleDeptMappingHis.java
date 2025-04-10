package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserRoleDeptMappingHisDto;
import com.skcc.ra.account.domain.hist.pk.UserRoleDeptMappingHisPK;
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
@IdClass(UserRoleDeptMappingHisPK.class)
@Table(name="OCO10114", catalog = "OCO")
public class UserRoleDeptMappingHis extends BaseEntity implements Apiable<UserRoleDeptMappingHisDto> {
//부서/직군/직책별 역할 매핑

    @Id
    @Column(name = "ROLE_DEPT_TEAM_CD", length = 6)
    private String roleDeptTeamCd;

    @Id
    @Column(name = "ROLE_MAPP_REOFO_CD", length = 4)
    private String roleMappReofoCd;

    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "CHNG_DTM")
    private String chngDtm;

    @Column(name = "CRUD_CL_CD")
    private String crudClCd;

    @Column(name = "CHNG_USER_IPADDR")
    private String chngUserIpaddr;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

    @Column(name = "CHNG_RESON_CNTNT")
    private String chngResonCntnt;

    @Override
    public UserRoleDeptMappingHisDto toApi() {
        UserRoleDeptMappingHisDto userRoleDeptMappingHisDto = new UserRoleDeptMappingHisDto();
        BeanUtils.copyProperties(this, userRoleDeptMappingHisDto);
        return userRoleDeptMappingHisDto;
    }

}
