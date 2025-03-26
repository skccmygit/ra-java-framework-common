package kr.co.skcc.base.com.common.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ResponseUtil {
    public static HttpServletResponse setResponseHeader(String contentType, String charset, String fileName) {
        return setResponseHeader(contentType, charset, fileName, "", 0);
    }

    /**
     * 매번 Method로 Response 넘기는건 비효율적임에 따라 Spring 전역에서 사용 가능한 HttpServletResponse를 Utils로 불러온다.
     *
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getResponse();
        } else {
            return null;
        }
    }

    public static HttpServletResponse setResponseHeader(String contentType,
                                                        String charset,
                                                        String fileName,
                                                        String contentTransferEncoding,
                                                        int contentLength){
        HttpServletResponse response = getHttpServletResponse();

        response.setCharacterEncoding(charset);
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        if (!"".equals(contentTransferEncoding)) {
            response.setHeader("Content-Transfer-Encoding", contentTransferEncoding);
        }
        if (contentLength != 0) {
            response.setContentLength(contentLength);
        }

        return response;
    }
}
