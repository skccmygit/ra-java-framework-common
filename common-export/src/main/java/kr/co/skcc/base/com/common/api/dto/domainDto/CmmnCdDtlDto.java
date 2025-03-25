package kr.co.skcc.base.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.CmmnCdDtlIDto;
import kr.co.skcc.base.com.common.domain.cmmnCd.CmmnCdDtl;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CmmnCdDtlDto", description = "공통코드상세")
public class CmmnCdDtlDto implements Entitiable<CmmnCdDtl> {

    @Schema(description = "공통코드", example = "C01", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 1, max = 30, message="공통코드는 1자~30자 입니다.")
    @NotEmpty(message="공통코드는 필수값 입니다.")
    private String cmmnCd;

    @Schema(description = "공통코드명")
    private String cmmnCdNm;

    @Schema(description = "공통코드상세", example = "AAA")
    @Size(min = 1, max = 40)
    @NotNull
    private String cmmnCdVal;

    @Schema(description = "공통코드상세명", example = "진행중")
    private String cmmnCdValNm;

    @Schema(description = "공통코드상세설명", example = "공통코드값내용")
    private String cmmnCdValDesc;

    @Schema(description = "정렬순서", example = "1")
    private Integer sortSeqn;

    @Schema(description = "사용여부", example = "Y")
    private String useYn;

    @Schema(description = "참조속성명1", example = "참조속성명1")
    private String refrnAttrVal1;

    @Schema(description = "참조속성명2", example = "참조속성명2")
    private String refrnAttrVal2;

    @Schema(description = "참조속성명3", example = "참조속성명3")
    private String refrnAttrVal3;

    @Schema(description = "참조속성명4", example = "참조속성명4")
    private String refrnAttrVal4;

    @Schema(description = "참조속성명5", example = "참조속성명5")
    private String refrnAttrVal5;

    @Schema(description = "참조속성명6", example = "참조속성명6")
    private String refrnAttrVal6;

    @Schema(description = "참조속성명7", example = "참조속성명7")
    private String refrnAttrVal7;

    @Schema(description = "참조속성명8", example = "참조속성명8")
    private String refrnAttrVal8;

    @Schema(description = "참조속성명9", example = "참조속성명9")
    private String refrnAttrVal9;

    @Schema(description = "참조속성명10", example = "참조속성명10")
    private String refrnAttrVal10;

    @Schema(description = "상위코드", example = "C01")
    private String superCmmnCd;

    @Schema(description = "상위코드명")
    private String superCmmnCdNm;

    @Schema(description = "상위코드값", example = "BBB")
    private String superCmmnCdVal;

    @Schema(description = "상위코드값", example = "BBB")
    private String superCmmnCdValNm;

    public CmmnCdDtlDto(CmmnCdDtl c){
        BeanUtils.copyProperties(c, this);
    }

    public CmmnCdDtlDto(CmmnCdDtl c, String cmmnCdNm){
        this(c);
        this.cmmnCdNm = cmmnCdNm;
    }

    public CmmnCdDtlDto(CmmnCdDtl c, String cmmnCdNm, String superCmmnCdNm, String superCmmnCdValNm){
        this(c,cmmnCdNm);
        this.superCmmnCdNm = superCmmnCdNm;
        this.superCmmnCdValNm = superCmmnCdValNm;
    }

    public CmmnCdDtlDto(CmmnCdDtlIDto c){
        BeanUtils.copyProperties(c, this);
    }

    @Override
    public CmmnCdDtl toEntity() {
        CmmnCdDtl entity = new CmmnCdDtl();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }

}
