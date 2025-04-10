package com.skcc.ra.account.domain.userSpecificMenu;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.MyViewDtlDto;
import com.skcc.ra.account.domain.userSpecificMenu.pk.MyViewDtlPK;
import com.skcc.ra.common.jpa.Apiable;
import com.skcc.ra.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MyViewDtlPK.class)
@DynamicUpdate
@Table(name="OCO10142", catalog = "OCO")
public class MyViewDtl extends BaseEntity implements Apiable<MyViewDtlDto> {

    @Id
    @Column(name="USER_SCREN_CNSTTE_SEQ", nullable=false)
    private Integer userScrenCnstteSeq;

    @Id
    @Column(name="SCREN_ID", nullable=false)
    private String screnId;

    @Column(name="SCREN_START_XCOO", nullable=false)
    private float screnStartXcoo;

    @Column(name="SCREN_START_YCOO", nullable=false)
    private float screnStartYcoo;

    @Column(name="SCREN_WIDTH_SIZE", nullable=false)
    private Integer screnWidthSize;

    @Column(name="SCREN_VRTLN_SIZE", nullable=false)
    private Integer screnVrtlnSize;

    @Override
    public MyViewDtlDto toApi() {
        MyViewDtlDto dto = new MyViewDtlDto();
        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
