package kr.co.skcc.base.com.common.domain.apiInfo.type;

import kr.co.skcc.base.com.common.api.dto.CdDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.BeanUtils;

@Getter
@AllArgsConstructor
public enum BizTask {

    ISC("ISC","관제", "feign.isc-batch-service.url"),
    CTC("CTC","고객센터", "feign.ctc-batch-service.url"),
    WFM("WFM","WFM", "feign.wfm-batch-service.url"),
    CSV("CSV","고객서비스", "feign.csv-batch-service.url"),
    ANL("ANL","분석", "feign.anl-batch-service.url"),
    ONM("ONM","ONM", "feign.onm-batch-service.url"),
    COM("COM","공통", "feign.com-batch-service.url"),
    IGW("IGW","통합GW", "feign.gw-batch-service.url");

    private final String code;
    private final String name;
    private final String batchUri;

    public CdDto toApi() {
        CdDto cdDto = new CdDto();
        BeanUtils.copyProperties(this, cdDto);

        return cdDto;
    }
}
