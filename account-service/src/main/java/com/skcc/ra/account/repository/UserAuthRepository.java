package com.skcc.ra.account.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.domain.loginCert.UserAuth;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserAuthRepository extends CrudRepository<UserAuth, String> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    Optional<UserAuth> findByUserid(String userid);

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 나태관"))
    void deleteByUserid(String userid);
}
