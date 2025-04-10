package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.hist.RoleMenuHist;
import com.skcc.ra.account.domain.hist.pk.RoleMenuHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuHistRepository extends JpaRepository<RoleMenuHist, RoleMenuHistPK> {
}
