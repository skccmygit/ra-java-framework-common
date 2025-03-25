package kr.co.skcc.base.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import kr.co.skcc.base.com.common.domain.apiInfo.ApiInfo;
import kr.co.skcc.base.com.common.domain.apiInfo.type.BizTask;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import kr.co.skcc.base.com.common.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiInfoDto", description = "API 정보")
public class ApiInfoDto implements Entitiable<ApiInfo> {

    @Schema(description = "APIID")
    private int apiId;

    @Schema(description = "응용프로그램그룹ID")
    private int aproGroupId;

    @Schema(description = "API명", example = "테스트")
    @Size(max = 100)
    private String apiNm;

    @Schema(description = "API설명", example = "테스트입니다.")
    @Size(max = 200)
    private String apiDesc;

    @Schema(description = "API위치URL주소", example = "/test")
    @Size(max = 100)
    private String apiLocUrladdr;

    @Schema(description = "HTT메소드값", example = "GET")
    @Size(max = 10)
    private String httMethodVal;

    @Schema(description = "API요청내용", example = "String aproGroupId")
    @Size(max = 4000)
    private String apiReqCntnt;

    @Schema(description = "API응답내용", example = "String")
    @Size(max = 4000)
    private String apiRespCntnt;

    @Schema(description = "응용프로그램업무구분코드", example = "ISC")
    private BizTask aproTaskClCd;

    @Schema(description = "응용프로그램업무구분코드명")
    private String aproTaskClCdNm;

    @Schema(description = "응용프로그램그룹명", example = "테스트")
    private String aproGroupClNm;

    public ApiInfoDto(int apiId, int aproGroupId, String apiNm, String apiDesc, String apiLocUrladdr, String httMethodVal, String apiReqCntnt, String apiRespCntnt, BizTask aproTaskClCd, String aproGroupClNm) {
        this.apiId = apiId;
        this.aproGroupId = aproGroupId;
        this.apiNm = apiNm;
        this.apiDesc = apiDesc;
        this.apiLocUrladdr = apiLocUrladdr;
        this.httMethodVal = httMethodVal;
        this.apiReqCntnt = apiReqCntnt;
        this.apiRespCntnt = apiRespCntnt;
        this.aproTaskClCd = aproTaskClCd;
        if(this.aproTaskClCd != null) this.aproTaskClCdNm = this.aproTaskClCd.getName();
        this.aproGroupClNm = aproGroupClNm;
    }

    public ApiInfoDto(int apiId, int aproGroupId, BizTask aproTaskClCd, String aproGroupClNm, String apiNm, String apiDesc, String httMethodVal, String apiLocUrladdr) {
        this.apiNm = apiNm;
        this.apiDesc = apiDesc;
        this.apiLocUrladdr = apiLocUrladdr;
        this.httMethodVal = httMethodVal;
        this.aproTaskClCd = aproTaskClCd;
        if(this.aproTaskClCd != null) this.aproTaskClCdNm = this.aproTaskClCd.getName();
        this.aproGroupClNm = aproGroupClNm;
    }

    @Override
    public ApiInfo toEntity() {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(this, apiInfo);
        apiInfo.setLastChngrId(RequestUtil.getLoginUserid());
        return apiInfo;
    }
}
