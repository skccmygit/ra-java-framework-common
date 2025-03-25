package kr.co.skcc.base.com.common.domain.apiInfo.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiStatPK implements Serializable {
    // API수행일자
    private String apiExectDt;

    // APIID
    private int apiId;
}
