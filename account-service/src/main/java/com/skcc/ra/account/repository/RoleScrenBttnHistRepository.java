package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.hist.RoleScrenBttnHist;
import com.skcc.ra.account.domain.hist.pk.RoleScrenBttnHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleScrenBttnHistRepository extends JpaRepository<RoleScrenBttnHist, RoleScrenBttnHistPK> {
}
