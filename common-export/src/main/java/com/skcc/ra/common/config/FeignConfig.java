package com.skcc.ra.common.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import com.skcc.ra.common.exception.FeignErrorDecoder;
import com.skcc.ra.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Configuration
@EnableFeignClients({"com.skcc.ra.**.adaptor.client"})
@Import(FeignClientsConfiguration.class)
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public PageJacksonModule pageJacksonModule(){
        return new PageJacksonModule();
    }

    @Bean
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return new HttpMessageConverters(new RestTemplate().getMessageConverters());
            }
        }));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
//        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            String token = "";
            String accountId = "";
            String roleStr = "WHITELIST_IP";

            // web 프로젝트 여부?
            if(RequestContextHolder.getRequestAttributes() != null) {
                    // 모바일 일부는 Authorzation 이 없을 수 있음
                token = RequestUtil.getHttpServletRequest().getHeader("Authorization");
                accountId = RequestUtil.getHttpServletRequest().getHeader("ACCOUNT_ID");
                roleStr = RequestUtil.getHttpServletRequest().getHeader("ROLES_STR");
            }
            requestTemplate.header("Authorization", token);
            requestTemplate.header("ACCOUNT_ID", accountId);
            requestTemplate.header("ROLES_STR", roleStr);
        };
    }
}
