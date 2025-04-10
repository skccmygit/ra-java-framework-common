package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.hist.UserAuthReqHis;
import com.skcc.ra.account.domain.hist.pk.UserAuthReqHisPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserAuthReqHisRepository extends JpaRepository<UserAuthReqHis, UserAuthReqHisPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value = "SELECT t FROM UserAuthReqHis t WHERE t.athrtyReqstSeq = :athrtyReqstSeq" )
    List<UserAuthReqHis> findByAthrtyReqstSeq(@Param("athrtyReqstSeq") Integer athrtyReqstSeq);
}
