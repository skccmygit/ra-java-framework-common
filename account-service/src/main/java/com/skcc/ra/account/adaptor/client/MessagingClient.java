package com.skcc.ra.account.adaptor.client;

import jakarta.validation.Valid;
import com.skcc.ra.common.api.dto.requestDto.SendReqDto;
import com.skcc.ra.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "messaging-service", url = "${feign.messaging-service.url:http://localhost:9104}", configuration = {FeignConfig.class})
public interface MessagingClient {

    @PostMapping("/api/v1/com/messaging/send/sendSms")
    ResponseEntity sendSms(@Valid @RequestBody SendReqDto sendReqDto);

    @PostMapping("/api/v1/com/messaging/send/multiSendSms")
    ResponseEntity multiSendSms(@Valid @RequestBody List<SendReqDto> sendReqDtoList);
}
