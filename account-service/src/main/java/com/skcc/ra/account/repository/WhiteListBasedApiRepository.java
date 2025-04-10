package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.loginCert.WhiteListBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * WhiteListBasedApi.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface WhiteListBasedApiRepository extends CrudRepository<WhiteListBasedApi, Integer> {
    Optional<WhiteListBasedApi> findByApiLocUrladdrAndHttMethodVal(String apiLocUrladdr, String httpMethodVal);
}
