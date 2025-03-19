package kr.co.skcc.oss.gateway.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@Data
@RedisHash("RefreshToken")
public class Token {
    @Id
    private String userid;

    @Indexed
    private String accessToken;

    @Indexed
    private String oldAccessToken;

    private String refreshToken;

    private String updateTime;

    private Instant expiryDate;
}