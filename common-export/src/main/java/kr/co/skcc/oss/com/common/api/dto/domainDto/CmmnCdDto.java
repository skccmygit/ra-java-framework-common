package kr.co.skcc.oss.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.skcc.oss.com.common.domain.cmmnCd.CmmnCd;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CmmnCdDto", description = "공통코드")
public class CmmnCdDto implements Entitiable<CmmnCd> {

    @Schema(description = "공통코드", example = "C01", required = true)
    @Size(min = 1, max = 30, message="공통코드는 1자~30자 입니다.")
    @NotEmpty(message="공통코드는 필수값 입니다.")
    private String cmmnCd;

    @Schema(description = "시스템구분코드", example = "BSS")
    @NotNull
    private String systmClCd;

    @Schema(description = "업무구분코드", example = "00")
    @NotNull
    private String chrgTaskGroupCd;

    @Schema(description = "BSS공통코드", example = "BC01")
    private String bssCmmnCd;

    @Schema(description = "공통코드명", example = "테스트코드")
    private String cmmnCdNm;

    @Schema(description = "공통코드설명내용", example = "테스트입니다")
    private String cmmnCdDesc;

    @Schema(description = "공통코드값길이", example = "5")
    private int	cmmnCdValLenth;

    @Schema(description = "사용여부", example = "Y")
    private String useYn;

    @Schema(description = "참조속성명1", example = "참조속성명1")
    private String refrnAttrNm1;

    @Schema(description = "참조속성명2", example = "참조속성명2")
    private String refrnAttrNm2;

    @Schema(description = "참조속성명3", example = "참조속성명3")
    private String refrnAttrNm3;

    @Schema(description = "참조속성명4", example = "참조속성명4")
    private String refrnAttrNm4;

    @Schema(description = "참조속성명5", example = "참조속성명5")
    private String refrnAttrNm5;

    @Schema(description = "참조속성명6", example = "참조속성명6")
    private String refrnAttrNm6;

    @Schema(description = "참조속성명7", example = "참조속성명7")
    private String refrnAttrNm7;

    @Schema(description = "참조속성명8", example = "참조속성명8")
    private String refrnAttrNm8;

    @Schema(description = "참조속성명9", example = "참조속성명9")
    private String refrnAttrNm9;

    @Schema(description = "참조속성명10", example = "참조속성명10")
    private String refrnAttrNm10;

    @Schema(description = "업무구분명", example = "공통")
    private String chrgTaskGroupNm;

   @Override
    public CmmnCd toEntity() {
        CmmnCd entity = new CmmnCd();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }

    public CmmnCdDto(CmmnCd cmmnCd){
        BeanUtils.copyProperties(cmmnCd, this);
    }
    public CmmnCdDto(CmmnCd cmmnCd, String chrgTaskGroupNm){
        this(cmmnCd);
        this.chrgTaskGroupNm = chrgTaskGroupNm;

    }
}
