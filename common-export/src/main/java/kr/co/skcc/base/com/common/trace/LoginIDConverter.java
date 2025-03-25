package kr.co.skcc.base.com.common.trace;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import kr.co.skcc.base.com.common.util.RequestUtil;

public class LoginIDConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return RequestUtil.getLoginUserid();

//        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
//        if (attrs != null) {
//            return attrs.getSessionId();
//        }
//        return "NO_SESSION";
    }
}
