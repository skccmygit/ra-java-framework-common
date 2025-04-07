package com.skcc.ra.common.trace;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.skcc.ra.common.util.RequestUtil;

import java.net.UnknownHostException;

public class ClientIPConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        try {
            return RequestUtil.getClientIP();
        } catch (UnknownHostException e) {
            return "Unknown";
        }

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            return auth.getName();
//        }
//        return "NO_USER";
    }
}