package kr.co.skcc.oss.com.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"kr.co.skcc.oss.com.**.domain"})
@EnableJpaRepositories("kr.co.skcc.oss.com.common.repository")
@SpringBootApplication
@ComponentScan(basePackages = {"kr.co.skcc.oss.com.common"})
@EnableJpaAuditing
public class CommonMainApp {
    public static void main(String[] args) {
        SpringApplication.run(CommonMainApp.class, args);
    }
}
