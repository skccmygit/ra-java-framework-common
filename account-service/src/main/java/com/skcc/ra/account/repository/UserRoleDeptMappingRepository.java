package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.baseAuth.UserRoleDeptMapping;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptMappingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserRoleDeptMappingRepository extends JpaRepository<UserRoleDeptMapping, UserRoleDeptMappingPK> {
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Query(value="SELECT t.userRoleId \n" +
                 "  FROM UserRoleDeptMapping t \n" +
                 " INNER JOIN UserRoleDept k ON (t.roleDeptTeamCd = k.roleDeptTeamCd AND t.roleMappReofoCd = k.roleMappReofoCd )" +
                 " WHERE k.roleDeptTeamCd = :roleDeptTeamCd \n" +
                 "   AND k.roleMappReofoCd = :roleMappReofoCd \n" +
                 "   AND k.useYn = 'Y' \n")
    List<String> findByRoleDeptTeamCdAndRoleMappReofoCd(String roleDeptTeamCd, String roleMappReofoCd);
}
