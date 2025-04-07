package com.skcc.ra.common.domain.dept;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.skcc.ra.common.api.dto.domainDto.BssmacdDto;
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
@Table(name="OCO50300", catalog = "OCO")
public class Bssmacd extends BaseEntity implements Apiable<BssmacdDto> {
    @Id
    @Column(name="BSSMACD", length = 2, nullable = false)
    private String bssmacd;

    @Column(name="BSS_HQ_NM", length = 50, nullable = false)
    private String bssHqNm;

    @Column(name="USE_YN", length = 1, nullable = false)
    private String useYn;

    @Override
    public BssmacdDto toApi() {
        BssmacdDto bssmacdDto = new BssmacdDto();
        BeanUtils.copyProperties(this,bssmacdDto);

        return bssmacdDto;
    }
}
