package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    @Query(value="SELECT t.userRoleId FROM UserRole t WHERE t.userid = :userid")
    List<String> findRoles(@Param("userid") String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    List<UserRole> findByUserid(String userid);
    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserid(String userid);
}
