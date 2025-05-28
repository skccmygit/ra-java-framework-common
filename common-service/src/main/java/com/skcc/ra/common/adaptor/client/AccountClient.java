package com.skcc.ra.common.adaptor.client;

import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.common.config.FeignConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "${feign.account-service.url}", configuration = {FeignConfig.class})
public interface AccountClient {

    @Schema(name="ID로 계정조회")
    @GetMapping("/api/v1/com/account/userid")
    AccountDto searchAccountByUserid(@RequestParam(required = false, value = "userid") String userid);

}