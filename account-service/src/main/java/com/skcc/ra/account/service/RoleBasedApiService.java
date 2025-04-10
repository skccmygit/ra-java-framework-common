package com.skcc.ra.account.service;

import java.util.List;

/**
 * RoleBasedApiService.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-09, 최초 작성
 */
public interface RoleBasedApiService {
    boolean insertSampleData(String delimitedString);
    List<String> selectSampleData();
}
