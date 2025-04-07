package com.skcc.ra.common.domain.menu;

import jakarta.persistence.*;
import com.skcc.ra.common.api.dto.domainDto.BttnDto;
import com.skcc.ra.common.domain.menu.pk.BttnPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
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
@IdClass(BttnPK.class)
@Table(name="OCO10230", catalog = "OCO")
public class Bttn extends BaseEntity implements Apiable<BttnDto> {

//    @EmbeddedId
//    private BttnPK bttnpk;
    @Id
    @Column(name="SCREN_ID", length = 10, nullable = false)
    private String screnId;

    @Id
    @Column(name="BTTN_ID", length = 10, nullable = false)
    private String bttnId;

    @Column(name="BTTN_NM", length = 100, nullable = false)
    private String bttnNm;

    @Column(name="BTTN_DESC", length = 500)
    private String bttnDesc;

    @Column(name="API_ID", length = 10)
    private Integer apiId;

    @Column(name="USE_YN", length = 1)
    private String useYn;

    @Column(name="CRUD_CL_CD", length = 1)
    private String crudClCd;

    @Override
    public BttnDto toApi() {

        BttnDto dto = new BttnDto();
        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
