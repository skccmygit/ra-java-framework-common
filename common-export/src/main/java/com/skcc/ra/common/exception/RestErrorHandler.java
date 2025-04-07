package com.skcc.ra.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Slf4j
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, final HttpServletRequest request) {
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
//        apiError.setError(HttpStatus.BAD_REQUEST.name());
//
//        List<String> details = new ArrayList<>();
//        e.getFieldErrors().forEach(err -> details.add(err.getField() + ": " + err.getDefaultMessage()));
//        e.getGlobalErrors().forEach(err -> details.add(err.getObjectName() + ": " + err.getDefaultMessage()));
//        apiError.setMessage(details);
//
//        apiError.setPath(request.getRequestURI());
//        log.error(apiError.getError(), e);
//        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
//    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException(BadRequestException e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setError(e.getMessage());
        if (e.getDetails() == null)
//            apiError.setMessage(Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)));
            apiError.setMessage(Arrays.asList(e.getMessage()));
        else
            apiError.setMessage(e.getDetails());
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setError(e.getMessage() == null ? HttpStatus.UNAUTHORIZED.name() : e.getMessage());
        apiError.setMessage(List.of(messageSource.getMessage(e.getMessage(), new String[]{}, HttpStatus.UNAUTHORIZED.getReasonPhrase(), Locale.getDefault())));
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setError(e.getMessage());
        if (e.getDetails() == null)
            apiError.setMessage(List.of(messageSource.getMessage(e.getMessage(), new String[]{}, HttpStatus.NOT_FOUND.getReasonPhrase(), Locale.getDefault())));
        else
            apiError.setMessage(e.getDetails());
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,final HttpServletRequest request) {
//        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED);
//        apiError.setError(HttpStatus.METHOD_NOT_ALLOWED.name());
////        apiError.setMessage(Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)));
//        apiError.setMessage(Arrays.asList(e.getMessage()));
//        apiError.setPath(request.getRequestURI());
//        log.error(apiError.getError(), e);
//        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
//    }

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<Object> handleServiceException(ServiceException e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setError(e.getMessage());
        if (e.getDetails() == null)
            apiError.setMessage(List.of(messageSource.getMessage(e.getMessage(), new String[]{}, HttpStatus.CONFLICT.getReasonPhrase(), Locale.getDefault())));
        else
            apiError.setMessage(e.getDetails());
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(SQLException.class)
    public final ResponseEntity<Object> handleSQLException(SQLException e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
//        apiError.setMessage(Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)));
        apiError.setMessage(Arrays.asList(e.getMessage()));
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e, final HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
//        apiError.setMessage(Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)));
        apiError.setMessage(Arrays.asList(e.getMessage()));
        apiError.setPath(request.getRequestURI());
        log.error(apiError.getError(), e);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
