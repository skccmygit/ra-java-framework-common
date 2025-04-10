package com.skcc.ra.common.config;

import com.skcc.ra.common.interceptor.APIAuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class APIInterceptorAppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAuthorizationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/v3/api-docs/**"
                        , "/h2-console/**"
                        , "/swagger-ui/**"
                        , "/swagger-resources"
                        , "/swagger-ui.html"
                        , "/webjars/**"
                        , "/v3/api-docs"
                        , "/swagger-resources/**"
                        , "/v1/com/account/authorization/**"
                        , "/**/swagger-ui/**"
                        , "/**/v3/api-docs/**"
                        , "/**/swagger-resources/**"
                );
    }

    @Bean
    public APIAuthorizationInterceptor apiAuthorizationInterceptor() {
        return new APIAuthorizationInterceptor();
    }
}
