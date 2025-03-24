package kr.co.skcc.base.com.common.adaptor.client;

import kr.co.skcc.base.com.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "Api-service", url = "url-placeholder", configuration = {FeignConfig.class})
public interface ApiDocsClient {

    @GetMapping
    String getApiDocs(URI uri);

}
