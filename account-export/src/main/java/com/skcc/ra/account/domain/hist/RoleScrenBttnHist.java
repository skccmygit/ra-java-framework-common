package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.domain.hist.pk.RoleScrenBttnHistPK;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RoleScrenBttnHistPK.class)
@Table(name="OCO10123", catalog = "OCO")
public class RoleScrenBttnHist extends BaseEntity {

    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "SCREN_ID", length = 10)
    private String screnId;

    @Id
    @Column(name = "BTTN_ID", length = 10)
    private String bttnId;

    @Id
    @Column(name = "CHNG_DTM", length = 14)
    private String chngDtm;

    @Column(name = "CRUD_CL_CD", length = 1)
    private String crudClCd;

    @Column(name = "CHNG_USER_IPADDR", length = 16)
    private String chngUserIpaddr;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

    @Column(name = "CHNG_RESON_CNTNT")
    private String chngResonCntnt;
}
