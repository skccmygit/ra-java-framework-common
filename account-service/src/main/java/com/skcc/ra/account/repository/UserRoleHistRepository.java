package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.account.domain.hist.pk.UserRoleHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleHistRepository extends JpaRepository<UserRoleHist, UserRoleHistPK> {
}
