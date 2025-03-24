package kr.co.skcc.oss.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.domain.apiInfo.AppGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

@Repository
public interface AppGroupRepository extends JpaRepository<AppGroup, Integer> {

    @QueryHints({@QueryHint(name = HINT_COMMENT, value = "onm AppGroupRepository.queryAppGroupSearch 이제현")})
    @Query("SELECT t FROM AppGroup t " +
            "WHERE t.aproTaskClCd LIKE CONCAT('%',:aproTaskClCd,'%') " +
            "and t.aproTypeClCd = :aproTypeClCd " +
            "ORDER BY t.aproTaskClCd, t.aproGroupClNm")
    List<AppGroup> queryAppGroupSearch(@Param("aproTaskClCd") String aproTaskClCd, @Param("aproTypeClCd") String aproTypeClCd);

}
