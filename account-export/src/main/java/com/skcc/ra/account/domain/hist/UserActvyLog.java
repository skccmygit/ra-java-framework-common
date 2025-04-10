package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.UserActvyLogDto;
import com.skcc.ra.account.domain.hist.pk.UserActvyLogPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserActvyLogPK.class)
@DynamicUpdate
@Entity
@Table(name="OCO10190", catalog = "OCO")
public class UserActvyLog extends BaseEntity  implements Apiable<UserActvyLogDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ACTVY_SEQ")
    private Long userActvySeq;

    @Column(name="USER_ACTVY_DTM", nullable=false)
    private String userActvyDtm;

    @Column(name="USERID", nullable=false)
    private String userid;

    @Column(name="USER_ACTVY_TYPE_CD", nullable=false)
    private String userActvyTypeCd;

    @Column(name="CONN_IPADDR", nullable=false)
    private String connIpaddr;

    @Column(name="SCREN_ID", nullable=false)
    private String screnId;

    @Column(name="SYSTM_CTGRY_CD", nullable=false)
    private String systmCtgryCd;

    @Column(name="DWNLD_RESON_CNTNT")
    private String dwnldResonCntnt;

    @Column(name="ACCSS_TOKEN_VAL")
    private String accssTokenVal;

    @Column(name="REFSH_TOKEN_VAL")
    private String refshTokenVal;

    @Override
    public UserActvyLogDto toApi() {
        UserActvyLogDto entity = new UserActvyLogDto();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

}
