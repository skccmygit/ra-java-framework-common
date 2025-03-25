package kr.co.skcc.base.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.base.com.common.api.dto.domainDto.MenuStatisticsDto;
import kr.co.skcc.base.com.common.domain.menu.MenuStatistics;
import kr.co.skcc.base.com.common.domain.menu.pk.MenuStatisticsPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface MenuStatisticsRepository extends JpaRepository<MenuStatistics, MenuStatisticsPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT new kr.co.skcc.base.com.common.api.dto.domainDto.MenuStatisticsDto(menu.chrgTaskGroupCd, cmmnCdDtl.cmmnCdValNm, " +
                                                                                         "menuStatistics.menuId, menu.menuNm, " +
                                                                                         "superMenu.menuId, superMenu.menuNm, " +
                                                                                         "SUM(menuStatistics.connQty)) " +
                    "FROM MenuStatistics menuStatistics " +
                    "LEFT OUTER JOIN Menu menu ON menuStatistics.menuId = menu.menuId " +
                    "LEFT OUTER JOIN Menu superMenu ON menu.superMenuId = superMenu.menuId " +
                    "LEFT OUTER JOIN CmmnCdDtl cmmnCdDtl ON cmmnCdDtl.cmmnCd = 'CHRG_TASK_GROUP_CD' AND menu.chrgTaskGroupCd = cmmnCdDtl.cmmnCdVal " +
                   "WHERE menu.chrgTaskGroupCd LIKE CONCAT('%',:chrgTaskGroupCd,'%') " +
                     "AND menuStatistics.sumrDt >= :sumrDtFrom " +
                     "AND menuStatistics.sumrDt <= :sumrDtTo " +
                   "GROUP BY menuStatistics.menuId " +
                   "ORDER BY menuStatistics.menuId ")
    Page<MenuStatisticsDto> searchMenuStatistics(@Param("chrgTaskGroupCd") String chrgTaskGroupCd,
                                                 @Param("sumrDtFrom") String sumrDtFrom,
                                                 @Param("sumrDtTo") String sumrDtTo,
                                                 Pageable pageable);
}