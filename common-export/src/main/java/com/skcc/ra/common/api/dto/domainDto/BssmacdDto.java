package com.skcc.ra.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.skcc.ra.common.domain.dept.Bssmacd;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BssmacdDto", description = "사원부가")
public class BssmacdDto implements Entitiable<Bssmacd> {

    @Schema(description = "본부코드")
    private String bssmacd;

    @Schema(description = "본부명")
    private String bssHqNm;

    @Schema(description = "사용어부")
    private String useYn;

    public Bssmacd toEntity() {
        Bssmacd bssmacd = new Bssmacd();
        BeanUtils.copyProperties(this, bssmacd);
        return bssmacd;
    }
}
