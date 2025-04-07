package com.skcc.ra.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.skcc.ra.**.domain"})
@EnableJpaRepositories("com.skcc.ra.common.repository")
@SpringBootApplication
@ComponentScan(basePackages = {"com.skcc.ra.common"})
@EnableJpaAuditing
public class CommonMainApp {
    public static void main(String[] args) {
        SpringApplication.run(CommonMainApp.class, args);
    }
}
