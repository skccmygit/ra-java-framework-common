package kr.co.skcc.oss.com.common.domain.apiInfo;

import jakarta.persistence.*;
import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiMonitorDto;
import kr.co.skcc.oss.com.common.jpa.Apiable;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OCO40111", catalog = "OCO")
public class ApiMonitor extends BaseEntity implements Apiable<ApiMonitorDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "API_EXECT_DTL_SEQ")
    private Long apiExectDtlSeq;

    //API수행시작일시T
    @Column(name = "API_EXECT_START_DTMT", length = 100)
    private LocalDateTime apiExctStartDtmt;

    //API응답상태값
    @Column(name = "API_RESP_STS_VAL")
    private int apiRespStsVal;

    //APIID
    @Column(name = "API_ID")
    private int apiId;

    //API응답시간
    @Column(name = "API_RESP_TIME")
    private int apiRespTime;

    @Column(name = "ERR_CNTNT")
    private String errCntnt;

    @Column(name = "API_EXECT_USERID")
    private String apiExectUserId;

    @Column(name = "CONN_IPADDR")
    private String connIpaddr;

    @Override
    public ApiMonitorDto toApi() {
        ApiMonitorDto apiMonitorDto = new ApiMonitorDto();
        BeanUtils.copyProperties(this, apiMonitorDto);
        apiMonitorDto.setApiRespTimeUnit(Integer.toString(this.apiRespTime) + "ms" );
        if (this.apiExctStartDtmt != null) apiMonitorDto.setApiExctStartTime(this.apiExctStartDtmt.format(DateTimeFormatter.ofPattern("HH:mm")));

        return apiMonitorDto;
    }
}
