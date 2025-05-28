package com.skcc.ra.common.repository;

import com.skcc.ra.common.domain.board.BoardAuth;
import com.skcc.ra.common.domain.board.pk.BoardAuthPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface BoardAuthRepository extends JpaRepository<BoardAuth, BoardAuthPK>, JpaSpecificationExecutor<BoardAuth> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT IFNULL(MAX(boardAuth.annceObjSeq),0)+1 " +
                    "FROM BoardAuth boardAuth " +
                   "WHERE boardAuth.annceNo = :annceNo")
    int findMaxSeq(Long annceNo);
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByAnnceNo(Long annceNo);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<BoardAuth> findByAnnceNo(Long annceNo);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value= "SELECT COUNT(*) " +
            "FROM BoardAuth boardAuth " +
            "WHERE boardAuth.annceNo = :annceNo " +
            "  AND boardAuth.annceObjDeptClCd = :annceObjDeptClCd " +
            "  AND boardAuth.annceObjDeptTeamCd = :annceObjDeptTeamCd ")
    int checkUserAuth(Long annceNo, String annceObjDeptClCd, String annceObjDeptTeamCd);
}
