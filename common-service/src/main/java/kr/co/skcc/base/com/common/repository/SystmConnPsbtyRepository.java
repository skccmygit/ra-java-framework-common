package kr.co.skcc.oss.com.common.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.common.domain.menu.SystmConnPsbty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface SystmConnPsbtyRepository extends JpaRepository<SystmConnPsbty, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<SystmConnPsbty> findByConnPsbtyYn(String connPsbtyYn);
}
