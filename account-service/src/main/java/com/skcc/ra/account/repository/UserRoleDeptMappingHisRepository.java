package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.hist.UserRoleDeptMappingHis;
import com.skcc.ra.account.domain.hist.pk.UserRoleDeptMappingHisPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDeptMappingHisRepository extends JpaRepository<UserRoleDeptMappingHis, UserRoleDeptMappingHisPK> {

}
