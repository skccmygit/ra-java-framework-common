package kr.co.skcc.oss.com.common.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.common.domain.menu.MenuStatistics;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "MenuStatisticsDto", description = "메뉴사용통계")
public class MenuStatisticsDto implements Entitiable<MenuStatistics> {

    @Schema(description = "메뉴통계일자", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sumrDt;

    @Schema(description = "담당업무그룹코드")
    private String chrgTaskGroupCd;

    @Schema(description = "담당업무그룹명")
    private String chrgTaskGroupNm;

    @Schema(description = "메뉴ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuId;

    @Schema(description = "메뉴명")
    private String menuNm;

    @Schema(description = "상위메뉴ID")
    private String superMenuId;

    @Schema(description = "상위메뉴명")
    private String superMenuNm;

    @Schema(description = "접속수량")
    private Long connQty;

    public MenuStatisticsDto( String sumrDt, String menuId, Long connQty) {
        this.sumrDt = sumrDt;
        this.menuId = menuId;
        this.connQty = connQty;
    }

    public MenuStatisticsDto( MenuStatistics menuStatistics) {
        this.sumrDt = menuStatistics.getSumrDt();
        this.menuId = menuStatistics.getMenuId();
        this.connQty = menuStatistics.getConnQty();
    }

    public MenuStatisticsDto( String chrgTaskGroupCd, String chrgTaskGroupNm, String menuId, String menuNm,
                              String superMenuId, String superMenuNm, Long connQty) {
        this.chrgTaskGroupCd = chrgTaskGroupCd;
        this.chrgTaskGroupNm = chrgTaskGroupNm;
        this.menuId = menuId;
        this.menuNm = menuNm;
        this.superMenuId = superMenuId;
        this.superMenuNm = superMenuNm;
        this.connQty = connQty;
    }

    @Override
    public MenuStatistics toEntity() {
        MenuStatistics menuStatistics = new MenuStatistics();
        BeanUtils.copyProperties(this, menuStatistics);

        return menuStatistics;
    }
}