package kr.co.skcc.oss.com.common.config;

import kr.co.skcc.oss.com.common.api.dto.emailDto.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    Properties pt = new Properties();

    private final EmailSender emailSender;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.emailSender.getHost());
//        javaMailSender.setUsername(this.emailSender.getUsername());
//        javaMailSender.setPassword(this.emailSender.getPassword());
        javaMailSender.setPort(this.emailSender.getPort());

//        pt.put("mail.smtp.socketFactory.port", this.emailSender.getPort());
//        pt.put("mail.smtp.auth", false);
//        pt.put("mail.smtp.starttls.enable", false);
//        pt.put("mail.smtp.starttls.required", false);
//        pt.put("mail.smtp.socketFactory.fallback", false);
//        pt.put("mail.smtp.socketFactory.class", this.emailSender.getSocketFactoryClass());
//
//        javaMailSender.setJavaMailProperties(pt);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}
