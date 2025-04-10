package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.loginCert.RoleBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * RoleBasedApiRepository.java
 * : 작성필요  authorization
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface RoleBasedApiRepository extends CrudRepository<RoleBasedApi, UUID> {

    Optional<RoleBasedApi> findByUserRoleIdAndApiLocUrladdrAndHttMethodVal(String userRoleId, String apiLocUrladdr, String httMethodVal);
    Optional<RoleBasedApi> findByUserRoleIdAndScrenIdAndBttnId(String userRoleId, String screnId, String bttnId);
}
