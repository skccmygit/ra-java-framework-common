package kr.co.skcc.base.com.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RequestUtil {
    /**
     * * 매번 Method로 Request를 넘기는건 비효율적임에 따라 Spring 전역에서 사용 가능한 HttpServletRequest를 Utils로 불러온다.
     * <p>
     * <p>
     * * @return
     */
    private static String env;
    @Value("${spring.profiles.active}")
    public void setEnv(String value){ this.env = value;  }

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        } else {
            return null;
        }
    }

    public static String getLoginUserid() {
        if("local".equals(env)){
            return "Admin";
        }
        String loginUserid = null;
        try {
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            if (httpServletRequest == null) {
                return "System";
            }
            if (httpServletRequest.getHeader("ACCOUNT_ID") == null) {
                return "System";
            }
            String header = httpServletRequest.getHeader("ACCOUNT_ID");
            if (header.isEmpty()) {
                return "System";
            } else {
                loginUserid = header;
            }
        } catch (NullPointerException e) {
            return "System";
        }

        if (loginUserid == null || "".equals(loginUserid)) {
            // 로그인 정보가 없을 때 - 401 에러 + 로그아웃처리 ?

            return "System";
        } else {
            return loginUserid;
        }
    }

    public static List<String> getLoginUserRoleList() {
        List<String> loginUserRoleList = null;
        if("local".equals(env)){
            loginUserRoleList = new ArrayList<>();
            loginUserRoleList.add("C001");
            loginUserRoleList.add("C002");
            loginUserRoleList.add("C003");

            return loginUserRoleList;
        }
        try {
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            if (httpServletRequest == null) {
                return null;
            }
            if (httpServletRequest.getHeader("ROLES_STR") == null) {
                return null;
            }
            String header = httpServletRequest.getHeader("ROLES_STR");
            if (header.isEmpty()) {
                return null;
            } else {
                loginUserRoleList = Arrays.asList(header.split("\\|"));
            }
        } catch (NullPointerException e) {
            return null;
        }

        if (loginUserRoleList == null || loginUserRoleList.isEmpty()) {
            return null;
        } else {
            return loginUserRoleList;
        }
    }

    public static String getClientIP() throws UnknownHostException {

        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        if (request != null) {
            String ip = request.getHeader("x-real-ip");

            if (ip == null) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return null;
    }

    public static String toSafe(String html) {
        return Jsoup.clean(html, Safelist.relaxed()
                .addProtocols("img", "src", "data"));
    }
}
