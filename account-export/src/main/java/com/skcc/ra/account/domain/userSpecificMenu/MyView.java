package com.skcc.ra.account.domain.userSpecificMenu;

import jakarta.persistence.*;
import com.skcc.ra.account.api.dto.domainDto.MyViewDto;
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
@DynamicUpdate
@Table(name="OCO10141", catalog = "OCO")
public class MyView extends BaseEntity implements Apiable<MyViewDto>{
    @Id
    @Column(name="USER_SCREN_CNSTTE_SEQ", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userScrenCnstteSeq;

    @Column(name="USERID", nullable=false)
    private String userid;

    @Column(name="USER_SCREN_CNSTTE_NM", nullable=false)
    private String userScrenCnstteNm;

    @Column(name="USER_INTI_SCREN_YN", nullable=false)
    private String userIntiScrenYn;

    @Column(name="USE_YN", nullable=false)
    private String useYn;

    @Override
    public MyViewDto toApi() {
        MyViewDto dto = new MyViewDto();

        BeanUtils.copyProperties(this,dto);

        return dto;
    }
}
