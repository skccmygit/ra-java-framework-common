package com.skcc.ra.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordSecurityConfig {

    /*
        기존 WebSecurityConfig에 정의된 PasswordEncoder를 분리함.
        순환 참조 오류 해소 용
        WebSecurityConfig -> AccountService -> PasswordEncoder -> WebSecurityConfig

        아래와 같이 변경
        PasswordEncoder -> WebSecurityConfig
        PasswordEncoder -> AccountService
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new ArgosPasswordEncoder();
        //return new BCryptPasswordEncoder();
    }
}
