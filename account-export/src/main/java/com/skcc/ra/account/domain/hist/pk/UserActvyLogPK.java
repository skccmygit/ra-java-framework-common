package com.skcc.ra.account.domain.hist.pk;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActvyLogPK implements Serializable {

    @Column(name="USER_ACTVY_SEQ")
    private Long userActvySeq;

    @Column(name="USER_ACTVY_DTM")
    private String userActvyDtm;

    @Column(name="USERID")
    private String userid;



}
