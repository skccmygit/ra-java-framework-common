package kr.co.skcc.oss.com.common.trace;

import ch.qos.logback.classic.PatternLayout;

public class PatternLayoutWithUserContext extends PatternLayout {
    static {
        PatternLayout.DEFAULT_CONVERTER_MAP.put("loginID", LoginIDConverter.class.getName());
        PatternLayout.DEFAULT_CONVERTER_MAP.put("clientIP", ClientIPConverter.class.getName());
    }
}