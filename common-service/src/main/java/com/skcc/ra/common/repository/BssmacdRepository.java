package com.skcc.ra.common.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.common.domain.dept.Bssmacd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface BssmacdRepository extends JpaRepository<Bssmacd, String> {
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT t " +
            " FROM Bssmacd t " +
            " WHERE '' = :useYn OR t.useYn = :useYn ")
    List<Bssmacd> findByUseYn(String useYn);
}
