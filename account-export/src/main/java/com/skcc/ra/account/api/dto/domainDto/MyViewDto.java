package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.skcc.ra.account.domain.userSpecificMenu.MyView;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MyViewDto", description = "마이뷰")
public class MyViewDto implements Entitiable<MyView> {

    @Schema(description = "마이뷰ID")
    @Size(min = 1, max = 10)
    @NotNull
    private Integer userScrenCnstteSeq;

    @Schema(description = "사용자ID")
    private String userid;

    @Schema(description = "마이뷰명")
    private String userScrenCnstteNm;

    @Schema(description = "초기화면사용여부")
    private String userIntiScrenYn;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "상세화면리스트")
    private List<MyViewDtlDto> myViewDtlDtoList;

    @Override
    public MyView toEntity() {
        MyView entity = new MyView();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }
}
