package com.skcc.ra.account.service;

import java.util.List;

/**
 * AuthorizationService.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-09, 최초 작성
 */
public interface AuthorizationService {
    boolean isAuthorizedAccess(String userId, List<String> roles, String apiPath, String methodValue);

}
