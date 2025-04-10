package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.loginCert.UserBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserBasedApi.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface UserBasedApiRepository extends CrudRepository<UserBasedApi, UUID> {

    Optional<UserBasedApi> findByUseridAndApiLocUrladdrAndHttMethodVal(String userid, String apiLocUrladdr, String httMethodVal);

    Optional<UserBasedApi> findByUseridAndScrenIdAndBttnId(String userid, String screnId, String bttnId);
}
