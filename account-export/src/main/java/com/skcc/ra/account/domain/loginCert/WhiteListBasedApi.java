package com.skcc.ra.account.domain.loginCert;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * WhiteListBasedApi.java
 * : 작성필요
 * API ID | API URL
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Data
@RedisHash("WhiteListBasedApi")
public class WhiteListBasedApi {
    /*
        API ID
     */
    @Id
    private int apiId;

    /*
        API 위치 URL 주소
     */
    @Indexed
    private String apiLocUrladdr;

    @Indexed
    private String httMethodVal;
}
