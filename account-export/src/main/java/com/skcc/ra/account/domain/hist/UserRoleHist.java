package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.domain.hist.pk.UserRoleHistPK;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserRoleHistPK.class)
@Table(name="OCO10112", catalog = "OCO")
public class UserRoleHist extends BaseEntity {
    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Id
    @Column(name = "CHNG_DTM", length = 14)
    private String chngDtm;

    @Id
    @Column(name = "CRUD_CL_CD")
    private String crudClCd;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;


    @Column(name = "DEPTCD")
    private String deptcd;

    @Column(name = "DEPT_NM")
    private String deptNm;

    @Column(name = "CHNG_USER_IPADDR", length = 16)
    private String chngUserIpaddr;

    @Column(name = "CHNG_RESON_CNTNT")
    private String chngResonCntnt;

}

