package kr.co.skcc.oss.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.skcc.oss.com.common.api.dto.responseDto.ifDto.ScrenIDto;
import kr.co.skcc.oss.com.common.domain.menu.Scren;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ScrenDto", description = "화면관리")
public class ScrenDto implements Entitiable<Scren> {

    @Schema(description = "화면ID", example = "M00001")
    @Size(min = 1, max = 10)
    @NotNull
    private String screnId;

    @Schema(description = "담당업무그룹코드", example = "00")
    private String chrgTaskGroupCd;

    @Schema(description = "담당업무그룹명")
    private String chrgTaskGroupNm;

    @Schema(description = "화면명", example = "테스트화면")
    @NotNull
    private String screnNm;

    @Schema(description = "화면설명", example = "테스트화면입니다.")
    private String screnDesc;

    @Schema(description = "화면URL주소", example = "com-service/cmmnCd")
    private String screnUrladdr;

    @Schema(description = "사용여부", example = "Y")
    @NotNull
    private String useYn;

    @Schema(description = "화면구분", example = "1")
    private String screnClCd;

    @Schema(description = "화면구분명")
    private String screnClNm;

    @Schema(description = "화면사이즈구분", example = "1")
    private String screnSizeClCd;

    @Schema(description = "화면width", example = "100")
    private Integer screnWidthSize;

    @Schema(description = "화면height", example = "50")
    private Integer screnVrtlnSize;

    @Schema(description = "화면 시작 top 위치", example = "50")
    private Integer screnStartTopCodn;

    @Schema(description = "화면 시작 left 위치", example = "50")
    private Integer screnStartLeftCodn;

    @Schema(description = "권한부부유 여부")
    private String authYn;

    @Schema(description = "매핑된 버튼리스트")
    private List<BttnDto> bttnList;

    public ScrenDto(ScrenIDto t) {
        BeanUtils.copyProperties(t, this);
    }
    public ScrenDto(Scren t) {
        BeanUtils.copyProperties(t, this);
    }
    public ScrenDto(Scren t, String task) {
        this(t);
        this.chrgTaskGroupNm = task;
    }
    public ScrenDto(Scren t, String task, String auth) {
        this(t);
        this.chrgTaskGroupNm = task;
        this.authYn = auth;
    }

    public ScrenDto(Scren t, String task, String auth, String screnClNm) {
        this(t);
        this.chrgTaskGroupNm = task;
        this.authYn = auth;
        this.screnClNm = screnClNm;
    }

    @Override
    public Scren toEntity() {
        Scren entity = new Scren();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }

}
