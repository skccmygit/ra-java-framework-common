package kr.co.skcc.base.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.skcc.base.com.common.domain.menu.Bttn;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "BttnDto", description = "버튼기본")
public class BttnDto implements Entitiable<Bttn> {

    @Schema(description = "화면ID", example = "M00001")
    @Size(min = 1, max = 10, message = "")
    @NotEmpty(message = "")
    private String screnId;

    @Schema(description = "버튼ID", example = "BTN001")
    @Size(min = 1, max = 10)
    @NotNull
    private String bttnId;

    @Schema(description = "버튼명", example = "등록")
    private String bttnNm;

    @Schema(description = "버튼설명", example = "등록기능")
    private String bttnDesc;

    @Schema(description = "API ID", example = "API0001")
    private Integer apiId;

    @Schema(description = "API명", example = "조회 API")
    private String apiNm;

    @Schema(description = "사용여부", example = "Y")
    private String useYn;

    @Schema(description = "권한여부", example = "Y")
    private String authYn;

    @Schema(description = "CRUD구분코드", example = "R")
    private String crudClCd;

    @Schema(description = "CRUD구분명", example = "읽기")
    private String crudClNm;

    public BttnDto(Bttn bttn){
        BeanUtils.copyProperties(bttn, this);
    }

    public BttnDto(Bttn b, String authYn) {
        this(b);
        this.authYn = authYn;
    }

    public BttnDto(Bttn b, String crudClNm, String gbn) {
        this(b);
        this.crudClNm = crudClNm;
    }

    @Override
    public Bttn toEntity() {
        Bttn entity = new Bttn();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }
}
