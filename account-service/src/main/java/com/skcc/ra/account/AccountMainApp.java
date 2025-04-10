package com.skcc.ra.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EntityScan(basePackages = {"com.skcc.ra.**.domain"})
@EnableJpaRepositories("com.skcc.ra.account.repository")
@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.skcc.ra.account", "com.skcc.ra.common"})
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class AccountMainApp {
    public static void main(String[] args) {
        SpringApplication.run(AccountMainApp.class, args);
    }

}
