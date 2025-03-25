package kr.co.skcc.base.com.common.trace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.Include;
import org.springframework.boot.actuate.web.exchanges.servlet.HttpExchangesFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class RequestTraceFilter extends HttpExchangesFilter {

    public RequestTraceFilter(HttpExchangeRepository repository, Set<Include> tracer) {
        super(repository, tracer);
    }

    private static final String[] excludedPaths = {
            "/actuator/**",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs",
            "/v1/ctc/inb/gw/save-call-stt-cnsl-detail",
            "/v1/ctc/inb/gw/update-counsel-status",
            "/v1/ctc/inb/cti/cnsl-log/saveCtiEventLog",
            "/v1/ctc/mgmt/cti/cnsl-log/saveCtiEventLog",
            "/v1/ctc/mgmt/system-management/linka-system/search/list/linka-system",
            "/v1/com/common/cmmnCd/searchCmmnCd*",
            "/v1/com/account/userMenu/scren/bttn/auth",
            "/v1/com/common/menu/systmConn",
            "/v1/com/account/useLog",
            "/v1/com/messaging/send/sendTalk",
            "/v1/com/messaging/send/sendSms",
            "/v1/com/messaging/send/email",
            "/v1/com/account/userMenu/menu",
            "/v1/com/account/userMenu/bookmark",
            "/v1/com/account/userMenu/shortcut",
            "/v1/com/common/menu/scren/dtl",
            "/v1/com/account/login/tokenAliveChk",
            "/v1/isc/dcg/signal/sis",
            "/v1/wfm/vhcLocHisty/vhcLocChngHisty"
    };

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludedPaths).anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long end = System.currentTimeMillis();

        JsonNode error = null;
        if (responseWrapper.getStatus() > 400) {
            try {
                error = mapper.readTree(getResponseBody(responseWrapper)).get("message");
            } catch (JsonProcessingException e) {
                log.debug("Parse Message", e.getMessage());
            } catch (Exception e){
                log.debug("Parse Error ", e.getMessage());
            }
        }

        // 로그인 아이디
        String userId = request.getHeader("account_id");

        Map<String, Object> traceMap = new LinkedHashMap<>();
        traceMap.put("timestamp", LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        traceMap.put("method", request.getMethod());
        traceMap.put("uri", request.getRequestURI());
        traceMap.put("remoteAddress", request.getRemoteAddr());
        traceMap.put("status", responseWrapper.getStatus());
        traceMap.put("timeTaken", (end - start));
        traceMap.put("error", error == null ? new String[]{} : error);
        traceMap.put("userId", userId == null ? "" : userId);

        log.info(mapper.writeValueAsString(traceMap));
        responseWrapper.copyBodyToResponse();
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if(wrapper == null) throw new NullPointerException();
        String characterEncoding = wrapper.getCharacterEncoding();
        return getPayload(wrapper.getContentAsByteArray(), characterEncoding);
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if(wrapper == null) throw new NullPointerException();
        return getPayload(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
    }

    private Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    public String getPayload(byte[] buf, String characterEncoding) {
        String payload = null;
        if (buf.length > 0) {
            try {
                payload = new String(buf, 0, buf.length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
        }
        return payload;
    }
}