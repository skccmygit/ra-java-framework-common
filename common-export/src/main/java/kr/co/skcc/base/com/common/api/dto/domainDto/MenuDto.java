package kr.co.skcc.base.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.skcc.base.com.common.api.dto.responseDto.ifDto.MenuIDto;
import kr.co.skcc.base.com.common.domain.menu.Menu;
import kr.co.skcc.base.com.common.domain.menu.Scren;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MenuDto", description = "메뉴관리")
public class MenuDto implements Entitiable<Menu> {

    @Schema(description = "메뉴ID", example = "1")
    private String menuId;

    @Schema(description = "담당업무그룹코드", example = "00")
    private String chrgTaskGroupCd;

    @Schema(description = "담당업무그룹명")
    private String chrgTaskGroupNm;

    @Schema(description = "메뉴타입", example = "0")
    @NotNull
    private String menuTypeCd;

    @Schema(description = "메뉴명", example = "메뉴명")
    @NotNull
    private String menuNm;

    @Schema(description = "화면실행구분코드", example = "1")
    @NotNull
    private String screnExcutClCd;

    @Schema(description = "메뉴설명", example = "메뉴설명입니다")
    private String menuDesc;

    @Schema(description = "메뉴단계", example = "1")
    @NotNull
    private int menuStepVal;

    @Schema(description = "정렬순서", example = "1")
    private int sortSeqn;

    @Schema(description = "사용여부", example = "Y")
    @NotNull
    private String useYn;

    @Schema(description = "메뉴노출여부", example = "Y")
    private String menuExpseYn;

    @Schema(description = "상위메뉴ID", example = "1")
    private String superMenuId;

    @Schema(description = "상위메뉴명")
    private String superMenuNm;

    @Schema(description = "매핑화면ID")
    private String screnId;

    @Schema(description = "매핑화면명")
    private String screnNm;

    @Schema(description = "매핑화면설명")
    private String screnDesc;

    @Schema(description = "매핑화면UrlAddr")
    private String screnUrladdr;

    @Schema(description = "매핑화면구분코드")
    private String screnClCd;

    @Schema(description = "매핑화면사이즈코드")
    private String screnSizeClCd;

    @Schema(description = "매핑화면가로사이즈")
    private Integer screnWidthSize;

    @Schema(description = "매핑화면세로사이즈")
    private Integer screnVrtlnSize;

    @Schema(description = "매핑화면시작위치(위)")
    private Integer screnStartTopCodn;

    @Schema(description = "매핑화면시작위치(좌)")
    private Integer screnStartLeftCodn;

    @Schema(description = "절체시스템태그")
    private String linkaSystmTagCntnt;

    @Schema(description = "바로가기정렬Seq")
    private Integer shortcutSortSeqn;

    @Schema(description = "즐겨찾기등록여부")
    private String bookmarkYN;

    @Schema(description = "권한보유여부")
    private String authYn;

    @Schema(description = "신규 여부")
    private String newYn;

    public MenuDto(MenuIDto t){
        BeanUtils.copyProperties(t, this);
    }
    public MenuDto(Menu t){
        BeanUtils.copyProperties(t, this);
    }

    /* 기본 메뉴 리스트 */
    public MenuDto(Menu t, Scren k){
        this(t);
        if (k != null) {
            BeanUtils.copyProperties(k, this);
        }
    }

    /* 전체메뉴 리스트 */
    public MenuDto(Menu t, Scren k, String chrgTaskGroupNm) {
        this(t, k) ;
        this.chrgTaskGroupNm = chrgTaskGroupNm;
    }
    /* 전체메뉴 리스트 */
    public MenuDto(Menu t, Scren k, String chrgTaskGroupNm, String superMenuNm) {
        this(t, k, chrgTaskGroupNm) ;
        this.superMenuNm = superMenuNm;
    }
    /* 바로가기 메뉴 리스트 */
    public MenuDto(Menu t, Scren k,  String chrgTaskGroupNm, String superMenuNm, int shortcutSortSeqn) {
        this(t, k, chrgTaskGroupNm, superMenuNm) ;
        this.shortcutSortSeqn = shortcutSortSeqn;
    }
    /* 전체메뉴 리스트 */
    public MenuDto(Menu t, Scren k, String chrgTaskGroupNm, String superMenuNm, String value, String gbn) {
        this(t, k, chrgTaskGroupNm, superMenuNm) ;
        if ("bookmark".equals(gbn))
            this.bookmarkYN = value;
        else if ("auth".equals(gbn))
            this.authYn = value;
    }

    /* 전체메뉴 리스트 */
    public MenuDto(Menu t, String chrgTaskGroupNm, String superMenuNm) {
        this(t) ;
        this.chrgTaskGroupNm = chrgTaskGroupNm;
        this.superMenuNm = superMenuNm;

    }

    /* 전체메뉴 리스트 */
    public MenuDto(Menu t, String chrgTaskGroupNm, String superMenuNm, String value, String gbn) {
        this(t, chrgTaskGroupNm, superMenuNm);
        if ("bookmark".equals(gbn))
            this.bookmarkYN = value;
        else if ("auth".equals(gbn))
            this.authYn = value;
    }

    @Override
    public Menu toEntity() {
        Menu entity = new Menu();
        BeanUtils.copyProperties(this, entity);

        return entity;
    }
}
