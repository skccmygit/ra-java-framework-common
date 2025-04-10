package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.domain.hist.pk.UserScrenBttnHistPK;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserScrenBttnHistPK.class)
@Table(name="OCO10117", catalog = "OCO")
public class UserScrenBttnHist extends BaseEntity {
    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Id
    @Column(name = "SCREN_ID", length = 10)
    private String screnId;

    @Id
    @Column(name = "BTTN_ID", length = 10)
    private String bttnId;

    @Id
    @Column(name = "CHNG_DTM", length = 14)
    private String chngDtm;

    @Id
    @Column(name = "CRUD_CL_CD")
    private String crudClCd;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;


    @Column(name = "CHNG_USER_IPADDR", length = 16)
    private String chngUserIpaddr;

    @Column(name = "CHNG_RESON_CNTNT")
    private String chngResonCntnt;

}

