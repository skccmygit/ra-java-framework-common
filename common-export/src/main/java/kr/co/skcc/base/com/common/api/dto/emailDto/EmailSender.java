package kr.co.skcc.base.com.common.api.dto.emailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app.email")
public class EmailSender {

    @Schema(description = "서버주소")
    private String host;

    @Schema(description = "포트")
    private int port;

}
