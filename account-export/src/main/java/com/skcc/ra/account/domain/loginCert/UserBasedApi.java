package com.skcc.ra.account.domain.loginCert;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

/**
 * UserBasedApi.java
 * : 작성필요
 * 사용자ID | 화면ID | 버튼ID | APIID | API URL | HTTP METHOD
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Data
@RedisHash("UserBasedApi")
public class UserBasedApi {

    @Id
    private UUID id;

    @Indexed
    private String userid;

    @Indexed
    private String screnId;

    @Indexed
    private String bttnId;


    private int apiId;

    @Indexed
    private String apiLocUrladdr;

    @Indexed
    private String httMethodVal;
}
