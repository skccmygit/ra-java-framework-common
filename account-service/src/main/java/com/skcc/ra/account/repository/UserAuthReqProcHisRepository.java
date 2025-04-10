package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.hist.UserAuthReqProcHis;
import com.skcc.ra.account.domain.hist.pk.UserAuthReqProcHisPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthReqProcHisRepository extends JpaRepository<UserAuthReqProcHis, UserAuthReqProcHisPK> {

}
