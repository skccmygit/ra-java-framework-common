package com.skcc.ra.common.domain.menu;

import jakarta.persistence.*;
import com.skcc.ra.common.api.dto.domainDto.MenuStatisticsDto;
import com.skcc.ra.common.domain.menu.pk.MenuStatisticsPK;
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
@IdClass(MenuStatisticsPK.class)
@Table(name="OCO10195", catalog = "OCO")
public class MenuStatistics extends BaseEntity implements Apiable<MenuStatisticsDto> {

    //메뉴통계일자
    @Id
    @Column(name="SUMR_DT", length = 8, nullable = false)
    private String sumrDt;

    //메뉴ID
    @Id
    @Column(name="MENU_ID", nullable = false)
    private String menuId;

    //접속수량
    @Column(name="CONN_QTY")
    private Long connQty;

    @Override
    public MenuStatisticsDto toApi() {

        MenuStatisticsDto menuStatisticsDto = new MenuStatisticsDto();
        BeanUtils.copyProperties(this,menuStatisticsDto);

        return menuStatisticsDto;
    }
}
