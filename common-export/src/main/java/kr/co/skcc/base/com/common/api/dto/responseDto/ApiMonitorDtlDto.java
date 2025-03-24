package kr.co.skcc.base.com.common.api.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.common.domain.apiInfo.type.BizTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiMonitorDtlDto", description = "API 검색조건 내역")
public class ApiMonitorDtlDto {

    @Schema(description = "API수행내역순번")
    private Long apiExectDtlSeq;

    @Schema(description = "업무구분코드")
    private BizTask aproTaskClCd;

    @Schema(description = "업무구분코드명")
    private String aproTaskClCdNm;

    @Schema(description = "그룹ID")
    private int aproGroupId;

    @Schema(description = "그룹명")
    private String aproGroupClNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "API수행시작일시T")
    private LocalDateTime apiExctStartDtmt;

    @Schema(description = "APIID")
    private int apiId;

    @Schema(description = "API명")
    private String apiNm;

    @Schema(description = "API위치URL주소")
    private String apiLocUrladdr;

    @Schema(description = "HTT메소드값")
    private String httMethodVal;

    @Schema(description = "실행 User id")
    private String apiExectUserId;

    public ApiMonitorDtlDto(Long apiExectDtlSeq, BizTask aproTaskClCd, int aproGroupId,
                            String aproGroupClNm, LocalDateTime apiExctStartDtmt, int apiid,
                            String apiNm, String apiLocUrladdr, String httMethodVal, String apiExectUserId) {
        this.apiExectDtlSeq = apiExectDtlSeq;
        this.aproTaskClCd = aproTaskClCd;
        if(this.aproTaskClCd != null) this.aproTaskClCdNm = this.aproTaskClCd.getName();
        this.aproGroupId = aproGroupId;
        this.aproGroupClNm = aproGroupClNm;
        this.apiExctStartDtmt = apiExctStartDtmt;
        this.apiId = apiid;
        this.apiNm = apiNm;
        this.apiLocUrladdr = apiLocUrladdr;
        this.httMethodVal = httMethodVal;
        this.apiExectUserId = apiExectUserId;
    }

}
