package com.skcc.ra.account.service.impl;

import com.skcc.ra.account.domain.loginCert.RoleBasedApi;
import com.skcc.ra.account.domain.loginCert.UserBasedApi;
import com.skcc.ra.account.domain.loginCert.WhiteListBasedApi;
import com.skcc.ra.account.repository.RoleBasedApiRepository;
import com.skcc.ra.account.repository.UserBasedApiRepository;
import com.skcc.ra.account.repository.WhiteListBasedApiRepository;
import com.skcc.ra.account.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * AuthorizationServiceImpl.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-09, 최초 작성
 */
@Service
@Transactional
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    WhiteListBasedApiRepository whiteListBasedApiRepository;

    @Autowired
    RoleBasedApiRepository roleBasedApiRepository;

    @Autowired
    UserBasedApiRepository userBasedApiRepository;

    @Override
    public boolean isAuthorizedAccess(String userid, List<String> roles, String apiPath, String methodValue) {

        /*
            Step 1
            WhiteList - API Check Logic
         */
        Optional<WhiteListBasedApi> whiteListBasedApiResult = whiteListBasedApiRepository.findByApiLocUrladdrAndHttMethodVal(apiPath, methodValue);
        boolean isAuth = whiteListBasedApiResult.isPresent();

        if(isAuth){
            log.debug("[whitelist-OK]  apiPath : {} / method : {}", apiPath, methodValue);
        }

        /*
            Step 2
            Role - API Check Logic
         */
        if(!isAuth){
            if(roles != null) {
                for (String role : roles) {
                    Optional<RoleBasedApi> roleBasedApiResult = roleBasedApiRepository.findByUserRoleIdAndApiLocUrladdrAndHttMethodVal(role, apiPath, methodValue);
                    if (roleBasedApiResult.isPresent()) {

                        log.debug("[Role-API-OK] role : {} / apiPath : {} / method : {}", role, apiPath, methodValue);

                        isAuth = true;
                        break;
                    }
                }
            }
        }

        /*
            Step 3
            User - API Check Logic
         */
        if(!isAuth){
            if(userid != null){
                Optional<UserBasedApi> userBasedApiResult = userBasedApiRepository.findByUseridAndApiLocUrladdrAndHttMethodVal(userid, apiPath, methodValue);
                if(userBasedApiResult.isPresent()){

                    log.debug("[User-API-OK] userid : {} / apiPath : {} / method : {}", userid, apiPath, methodValue);

                    isAuth = true;
                }
            }
        }
        return isAuth;
    }

}
