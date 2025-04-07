package com.skcc.ra.common.domain.apiInfo;

import jakarta.persistence.*;
import com.skcc.ra.common.domain.apiInfo.pk.ApiStatPK;
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
@Table(name = "OCO40112", catalog = "OCO")
@IdClass(ApiStatPK.class)
public class ApiStat extends BaseEntity {

    // API수행일자
    @Id
    @Column(name = "API_EXECT_DT")
    private String apiExectDt;

    // APIID
    @Id
    @Column(name = "API_ID")
    private int apiId;

    // API수행건수
    @Column(name = "API_EXECT_CCNT")
    private int apiExectCcnt;
}
