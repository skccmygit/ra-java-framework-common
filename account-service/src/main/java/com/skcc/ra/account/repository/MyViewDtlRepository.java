package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.MyViewDtlDto;
import com.skcc.ra.account.domain.userSpecificMenu.MyViewDtl;
import com.skcc.ra.account.domain.userSpecificMenu.pk.MyViewDtlPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface MyViewDtlRepository extends JpaRepository<MyViewDtl, MyViewDtlPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value="SELECT new com.skcc.ra.account.api.dto.domainDto.MyViewDtlDto(t, k.screnUrladdr, l.menuId, l.menuNm) " +
            "FROM MyViewDtl t " +
            "INNER JOIN Scren k ON (k.screnId = t.screnId AND k.useYn = 'Y') " +
            "LEFT OUTER JOIN Menu l ON (l.screnId = t.screnId AND l.useYn = 'Y') " +
            "WHERE t.userScrenCnstteSeq = :userScrenCnstteSeq ")
    List<MyViewDtlDto> findByUserScrenCnstteSeq(Integer userScrenCnstteSeq);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserScrenCnstteSeq(Integer userScrenCnstteSeq);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Modifying
    @Query(value = "DELETE FROM MyViewDtl t \n" +
                   " WHERE t.userScrenCnstteSeq IN ( SELECT k.userScrenCnstteSeq \n" +
                   "                                  FROM MyView k \n" +
                   "                                 WHERE k.userid = :userid ) \n")
    void deleteMyViewDtl(@Param("userid") String userid);
}
