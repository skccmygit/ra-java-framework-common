package com.skcc.ra.account.domain.auth;

import jakarta.persistence.*;
import com.skcc.ra.account.domain.auth.pk.UserScrenBttnPK;
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
@IdClass(UserScrenBttnPK.class)
@Table(name="OCO10107", catalog = "OCO")
public class UserScrenBttn extends BaseEntity {
    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Id
    @Column(name = "SCREN_ID", length = 10)
    private String screnId;

    @Id
    @Column(name = "BTTN_ID", length = 10)
    private String bttnId;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

}

