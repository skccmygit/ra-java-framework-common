package kr.co.skcc.oss.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.skcc.oss.com.common.domain.apiInfo.AppGroup;
import kr.co.skcc.oss.com.common.domain.apiInfo.type.BizTask;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import kr.co.skcc.oss.com.common.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "appGroupDto", description = "응용프로그램그룹기본")
public class AppGroupDto implements Entitiable<AppGroup> {

    @Schema(description = "응용프로그램그룹ID", example = "1")
    private int aproGroupId;

    @Schema(description = "응용프로그램유형구분코드", example = "A")
    @NotNull
    private String aproTypeClCd;

    @Schema(description = "응용프로그램업무구분코드", example = "ISC")
    @NotNull
    private BizTask aproTaskClCd;

    @Schema(description = "응용프로그램업무구분코드명")
    private String aproTaskClCdNm;

    @Schema(description = "응용프로그램그룹명", example = "테스트")
    @Size(max = 30)
    @NotNull
    private String aproGroupClNm;

    @Schema(description = "응용프로그램그룹설명", example = "테스트입니다.")
    @Size(max = 1000)
    @NotNull
    private String aproGroupDesc;

    @Override
    public AppGroup toEntity() {
        AppGroup appGroup = new AppGroup();
        BeanUtils.copyProperties(this, appGroup);
        appGroup.setLastChngrId(RequestUtil.getLoginUserid());
        return appGroup;
    }
}
