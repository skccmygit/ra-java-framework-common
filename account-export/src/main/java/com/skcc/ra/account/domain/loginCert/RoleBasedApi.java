package com.skcc.ra.account.domain.loginCert;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

/**
 * RoleBasedApi.java
 * : 작성필요
 * 사용자역할ID | 화면ID | 버튼ID | APIID | API URL
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Data
@RedisHash("RoleBasedApi")
public class RoleBasedApi {

    @Id
    private UUID id;

    /*
        사용자역할ID
     */
    @Indexed
    private String userRoleId;

    /*
        화면ID
     */
    @Indexed
    private String screnId;

    /*
        버튼ID
     */
    @Indexed
    private String bttnId;

    /*
        API ID
     */
    private int apiId;

    /*
        API 위치 URL 주소
     */
    @Indexed
    private String apiLocUrladdr;

    @Indexed
    private String httMethodVal;
}
