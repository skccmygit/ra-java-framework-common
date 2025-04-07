package com.skcc.ra.common.adaptor.client;

import com.skcc.ra.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * AccountClient.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-05-31, 최초 작성
 */
@FeignClient(name = "authorization-service", url = "${feign.authorization-service.url:http://com-account-api:8080/}", configuration = {FeignConfig.class})
public interface AuthorizationClient {

    @GetMapping("/api/v1/com/account/authorization/api")
    boolean getApiAuthorization(@RequestParam String userId
                                , @RequestParam List<String> roles
                                , @RequestParam String apiPath
                                , @RequestParam String methodValue);
}
