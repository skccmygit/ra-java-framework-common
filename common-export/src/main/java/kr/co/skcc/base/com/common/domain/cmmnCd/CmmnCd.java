package kr.co.skcc.base.com.common.domain.cmmnCd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.base.com.common.api.dto.domainDto.CmmnCdDto;
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
@Table(name="OCO20100", catalog = "OCO")
public class CmmnCd extends BaseEntity implements Apiable<CmmnCdDto> {
    @Id
    @Column(name="CMMN_CD", length = 30, nullable = false)
    private String cmmnCd;

    @Column(name="CHRG_TASK_GROUP_CD", length = 3)
    private String chrgTaskGroupCd;

    @Column(name="CMMN_CD_NM", length = 50, nullable = false)
    private String cmmnCdNm;

    @Column(name="CMMN_CD_DESC", length = 200)
    private String cmmnCdDesc;

    @Column(name="CMMN_CD_VAL_LENTH")
    private int	cmmnCdValLenth;

    @Column(name="USE_YN")
    private String useYn;

    @Column(name="REFRN_ATTR_NM1", length = 200)
    private String refrnAttrNm1;

    @Column(name="REFRN_ATTR_NM2", length = 200)
    private String refrnAttrNm2;

    @Column(name="REFRN_ATTR_NM3", length = 200)
    private String refrnAttrNm3;

    @Column(name="REFRN_ATTR_NM4", length = 200)
    private String refrnAttrNm4;

    @Column(name="REFRN_ATTR_NM5", length = 200)
    private String refrnAttrNm5;

    @Column(name="REFRN_ATTR_NM6", length = 200)
    private String refrnAttrNm6;

    @Column(name="REFRN_ATTR_NM7", length = 200)
    private String refrnAttrNm7;

    @Column(name="REFRN_ATTR_NM8", length = 200)
    private String refrnAttrNm8;

    @Column(name="REFRN_ATTR_NM9", length = 200)
    private String refrnAttrNm9;

    @Column(name="REFRN_ATTR_NM10", length = 200)
    private String refrnAttrNm10;

    @Override
    public CmmnCdDto toApi() {
        CmmnCdDto dto = new CmmnCdDto();
        BeanUtils.copyProperties(this,dto);

        return dto;
    }

}
