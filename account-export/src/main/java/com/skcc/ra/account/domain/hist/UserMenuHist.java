package com.skcc.ra.account.domain.hist;

import jakarta.persistence.*;
import com.skcc.ra.account.domain.hist.pk.UserMenuHistPK;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserMenuHistPK.class)
@Table(name="OCO10116", catalog = "OCO")
public class UserMenuHist extends BaseEntity {
    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Id
    @Column(name = "MENU_ID")
    private String menuId;

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

