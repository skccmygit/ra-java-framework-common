package com.skcc.ra.account.api.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * SsoDto.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-06-21, 최초 작성
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Sso", description = "SSO인증")
public class SsoDto {

    private boolean result;

    private String authStatus;

    private String errorCode;

    private String errorMessage;

    private String userId;

    private String connIp;
    
}
