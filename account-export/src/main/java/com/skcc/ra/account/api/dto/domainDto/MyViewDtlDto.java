package com.skcc.ra.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.skcc.ra.account.domain.userSpecificMenu.MyViewDtl;
import com.skcc.ra.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MyViewDtlDto", description = "마이뷰상세")
public class MyViewDtlDto implements Entitiable<MyViewDtl> {

    @Schema(description = "마이뷰ID")
    @Size(min = 1, max = 10)
    @NotNull
    private Integer userScrenCnstteSeq;

    @Schema(description = "마이뷰 화면ID")
    @NotNull
    private String screnId;

    @Schema(description = "화면 시작 X좌표")
    private float screnStartXcoo;

    @Schema(description = "화면 시작 Y좌표")
    private float screnStartYcoo;

    @Schema(description = "화면 가로 길이")
    private Integer screnWidthSize;

    @Schema(description = "화면 세로 길이")
    private Integer screnVrtlnSize;

    @Schema(description = "화면 URL")
    private String screnUrladdr;

    @Schema(description = "메뉴ID")
    private String menuId;

    @Schema(description = "메뉴명")
    private String menuNm;

    @Schema(description = "최대여부")
    private String maxYn;

    public MyViewDtlDto(MyViewDtl t, String screnUrladdr, String menuId, String menuNm) {
        this.userScrenCnstteSeq = t.getUserScrenCnstteSeq();
        this.screnId = t.getScrenId();
        this.screnStartXcoo = t.getScrenStartXcoo();
        this.screnStartYcoo = t.getScrenStartYcoo();
        this.screnWidthSize = t.getScrenWidthSize();
        this.screnVrtlnSize = t.getScrenVrtlnSize();
        this.screnUrladdr = screnUrladdr;
        this.menuId = menuId;
        this.menuNm = menuNm;

        // 최대화 상태로 저장된 건들 최대화 여부 표기
        if(this.screnStartXcoo == 0 && this.screnStartYcoo == 0
          && (this.screnWidthSize >= 1877
          && (this.screnVrtlnSize == 823 ||  this.screnVrtlnSize == 966))){
            this.maxYn = "Y";
        }else{
            this.maxYn = "N";
        }
    }

    @Override
    public MyViewDtl toEntity() {
        MyViewDtl entity = new MyViewDtl();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }

}
