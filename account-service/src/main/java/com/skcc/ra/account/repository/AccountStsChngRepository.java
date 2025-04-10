package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.hist.AccountStsChng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface AccountStsChngRepository extends JpaRepository<AccountStsChng, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value="SELECT t FROM AccountStsChng t " +
                "WHERE t.chngColEngshNm = 'CONN_PSSWD' " +
                "  AND t.userid = :userid " +
                "  AND t.chngColVal = :connPsswd ")
    List<AccountStsChng> findAllAccountPassword(@Param("userid") String userid, @Param("connPsswd") String connPsswd);
}
