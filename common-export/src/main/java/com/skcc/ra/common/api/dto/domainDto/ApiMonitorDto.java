package com.skcc.ra.common.api.dto.domainDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.common.domain.apiInfo.ApiMonitor;
import com.skcc.ra.common.jpa.Entitiable;
import com.skcc.ra.common.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "apiMonitorDto", description = "API 모니터링 내역")
public class ApiMonitorDto implements Entitiable<ApiMonitor> {

    @Schema(description = "API수행내역순번")
    private Long apiExectDtlSeq;

    @Schema(description = "API응답상태값")
    private Integer apiRespStsVal;

    @Schema(description = "APIID")
    private Integer apiId;

    @Schema(description = "API응답시간")
    private Integer apiRespTime;

    @Schema(description = "API응답시간")
    private String apiRespTimeUnit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "API수행시작일시T")
    private LocalDateTime apiExctStartDtmt;

    @Schema(description = "API수행시각")
    private String apiExctStartTime;

    @Schema(description = "상세원인")
    private String errCntnt;

    @Schema(description = "수행자ID")
    private String apiExectUserId;

    @Override
    public ApiMonitor toEntity() {
        ApiMonitor apiMonitor = new ApiMonitor();
        BeanUtils.copyProperties(this, apiMonitor);
        apiMonitor.setLastChngrId(RequestUtil.getLoginUserid());
        return apiMonitor;
    }
}
