package kr.co.skcc.base.com.common.domain.cmmnCd;

import jakarta.persistence.*;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDtlDto;
import kr.co.skcc.base.com.common.domain.cmmnCd.pk.CmmnCdDtlPK;
import kr.co.skcc.base.com.common.jpa.Apiable;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CmmnCdDtlPK.class)
@Table(name="OCO20101", catalog = "OCO")
public class CmmnCdDtl extends BaseEntity implements Apiable<CmmnCdDtlDto> {

    @Id
    @Column(name="CMMN_CD", length = 30, nullable = false)
    private String cmmnCd;

    @Id
    @Column(name="CMMN_CD_VAL", length = 40, nullable = false)
    private String cmmnCdVal;

    @Column(name="CMMN_CD_VAL_NM", length = 50, nullable = false)
    private String cmmnCdValNm;

    @Column(name="CMMN_CD_VAL_DESC", length = 200)
    private String cmmnCdValDesc;

    @Column(name="SORT_SEQN", nullable = false)
    private int sortSeqn;

    @Column(name="USE_YN")
    private String useYn;

    @Column(name="REFRN_ATTR_VAL1", length = 20)
    private String refrnAttrVal1;

    @Column(name="REFRN_ATTR_VAL2", length = 20)
    private String refrnAttrVal2;

    @Column(name="REFRN_ATTR_VAL3", length = 20)
    private String refrnAttrVal3;

    @Column(name="REFRN_ATTR_VAL4", length = 20)
    private String refrnAttrVal4;

    @Column(name="REFRN_ATTR_VAL5", length = 20)
    private String refrnAttrVal5;

    @Column(name="REFRN_ATTR_VAL6", length = 20)
    private String refrnAttrVal6;

    @Column(name="REFRN_ATTR_VAL7", length = 20)
    private String refrnAttrVal7;

    @Column(name="REFRN_ATTR_VAL8", length = 20)
    private String refrnAttrVal8;

    @Column(name="REFRN_ATTR_VAL9", length = 20)
    private String refrnAttrVal9;

    @Column(name="REFRN_ATTR_VAL10", length = 20)
    private String refrnAttrVal10;

    @Column(name="SUPER_CMMN_CD", length = 30)
    private String superCmmnCd;

    @Column(name="SUPER_CMMN_CD_VAL", length = 30)
    private String superCmmnCdVal;

    @Override
    public CmmnCdDtlDto toApi() {
        CmmnCdDtlDto dto = new CmmnCdDtlDto();
        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
