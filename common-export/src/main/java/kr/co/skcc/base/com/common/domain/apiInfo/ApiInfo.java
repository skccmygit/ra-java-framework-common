package kr.co.skcc.base.com.common.domain.apiInfo;

import jakarta.persistence.*;
import kr.co.skcc.base.com.common.api.dto.domainDto.ApiInfoDto;
import kr.co.skcc.base.com.common.jpa.Apiable;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO40110", catalog = "OCO")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ApiInfo extends BaseEntity implements Apiable<ApiInfoDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "API_ID")
    private int apiId;

    @Column(name = "APRO_GROUP_ID")
    private int aproGroupId;

    @Column(name = "API_NM", length = 100)
    private String apiNm;

    @Column(name = "API_DESC", length = 200)
    private String apiDesc;

    @Column(name = "API_LOC_URLADDR", length = 100)
    private String apiLocUrladdr;

    @Column(name = "HTT_METHOD_VAL", length = 10)
    private String httMethodVal;

    @Column(name = "API_REQ_CNTNT", length = 4000)
    private String apiReqCntnt;

    @Column(name = "API_RESP_CNTNT", length = 4000)
    private String apiRespCntnt;

    @Column(name = "LAST_CHNGR_ID", length = 10)
    private String lastChngrId;

    @Column(name = "LAST_CHNG_DTMD")
    private LocalDateTime lastChngDtmd;


    @Override
    public ApiInfoDto toApi() {
        ApiInfoDto apiInfoDto = new ApiInfoDto();
        BeanUtils.copyProperties(this, apiInfoDto);
        return apiInfoDto;
    }
}
