package com.skcc.ra.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.skcc.ra.common.adaptor.client.AuthorizationClient;
import com.skcc.ra.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class APIAuthorizationInterceptor implements HandlerInterceptor {

    @Value("${app.api-auth-enable:false}")
    private boolean apiAuthEnabled;

    @Autowired
    private AuthorizationClient authorizationClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*
            API 인가 Check 활성화 체크
            로컬 개발 환경에서는 disable 처리 해서 개발 편의성 확보
         */
        log.debug("app.api-auth-enable : {} ", apiAuthEnabled);
        if (!apiAuthEnabled) {
            log.trace("[API AUTH CHECK is disabled]" );
            return true;
            //return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        /*
            API AUTH Check Start
         */
        try {
            HandlerMethod method = (HandlerMethod) handler;
            GetMapping getMapping = method.getMethodAnnotation(GetMapping.class);
            PostMapping postMapping = method.getMethodAnnotation(PostMapping.class);
            DeleteMapping deleteMapping = method.getMethodAnnotation(DeleteMapping.class);
            PutMapping putMapping = method.getMethodAnnotation(PutMapping.class);
            PatchMapping patchMapping = method.getMethodAnnotation((PatchMapping.class));
            RequestMapping requestMapping = method.getBeanType().getAnnotation(RequestMapping.class);

            String accountId = request.getHeader("ACCOUNT_ID");
            String rolesString = request.getHeader("ROLES_STR");
            String apiPath = request.getContextPath() +  Arrays.toString(requestMapping.value());
            String requestMethod = "GET";

            if (getMapping != null) {
                requestMethod = "GET";
                apiPath += Arrays.toString(getMapping.value());
            } else if (postMapping != null) {
                requestMethod = "POST";
                apiPath += Arrays.toString(postMapping.value());
            } else if (deleteMapping != null) {
                requestMethod = "DELETE";
                apiPath += Arrays.toString(deleteMapping.value());
            } else if (putMapping != null) {
                requestMethod = "PUT";
                apiPath += Arrays.toString(putMapping.value());
            } else if (patchMapping != null) {
                requestMethod = "PATCH";
                apiPath += Arrays.toString(patchMapping.value());
            }

            apiPath = apiPath.replace(",", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();

            /*
                WHITELIST Role 처리
             */
            if ("WHITELIST_IP".equals(rolesString)) {
                log.debug("[API AUTH CHECK] RESULT:{} // METHOD:{} / API:{} / ACCOUNT_ID:{} / ROLES_STR:{}  ", true, requestMethod, apiPath, accountId, rolesString);
                return true;
            }

            List<String> roles = null;
            if (rolesString != null && !rolesString.isEmpty()) {
                roles = Arrays.asList(rolesString.split("\\|"));
            }
            boolean isAuthorized = authorizationClient.getApiAuthorization(accountId, roles, apiPath, requestMethod);
            log.debug("[API AUTH CHECK] RESULT:{} // METHOD:{} / API:{} / ACCOUNT_ID:{} / ROLES_STR:{}  ", isAuthorized, requestMethod, apiPath, accountId, rolesString);

            if(!isAuthorized){
                throw new UnauthorizedException("COM.W0002");
            }
        } catch (UnauthorizedException ue){
            throw ue;
        } catch ( Exception e) {
            log.error(e.getMessage(), e);
            log.error("API Check UnexpectedException");
            throw new UnauthorizedException("COM.W0002", e.getCause()) ;
        }
        return true;
        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
