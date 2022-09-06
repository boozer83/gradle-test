package io.kakaoi.exception;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.web.rest.Result;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Exception 발생 시 오류 메시지 발생 및 로그 작성을 위한 Handler 메소드들이 있는 Controller Advice
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
        responseCode = "400",
        description =
            "Bad Request\n\n" +
                "Code 필드에 따른 에러 형태\n\n" +
                "* 0400: Bad Request / 특정할 수 없는 잘못된 요청\n" +
                "* 1000: Invalid Input Value / 유효성 체크 에러 (유효성 체크 결과가 data 필드에 포함될 가능성 존재)\n" +
                "* 1001: Invalid Type Value / 요청 데이터 타입 미스매치 등 잘못된 값 에러\n" +
                "* 1002: Invalid Json Shape / Json 문법 에러\n"
    )
    protected Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("handleMethodArgumentNotValidException", e);
        return Result.of(Constants.CommonCode.INVALID_INPUT_VALUE, makeFieldErrorsMap(e.getBindingResult()));
    }

    /**
     * 애노테이션 @ModelAttribute 으로 객체 바인딩 실패 시 던져진
     * BindException 예외를 처리하는 메소드이다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleBindException(BindException e) {
        log.debug("handleBindException", e);
        return Result.of(Constants.CommonCode.INVALID_INPUT_VALUE, makeFieldErrorsMap(e.getBindingResult()));
    }

    /**
     * 정렬 적용 시 Field 명이 유효하지 않을 때, 발생하는 에러
     */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handlePropertyReferenceException(PropertyReferenceException e) {
        log.debug("handlePropertyReferenceException", e);
        return Result.of(Constants.CommonCode.INVALID_INPUT_VALUE);
    }

    /**
     * Argument 형태가 잘못 되었을 때 던져지는 에러
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug("handleIllegalArgumentException", e);
        return Result.of(Constants.CommonCode.INVALID_INPUT_VALUE);
    }

    /**
     * Pageable, Sort 파라미터를 잘못 입력했을 때 발생하는 예외 처리
     */
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleDateTimeParseException(InvalidDataAccessApiUsageException e) {
        log.debug("handleDataAccessApiUsageException", e);
        return Result.of(Constants.CommonCode.INVALID_INPUT_VALUE);
    }

    /**
     * DateTime 형태를 받을 수 없을 때 발생하는 에러
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleDateTimeParseException(DateTimeParseException e) {
        log.debug("handleDateTimeParseException", e);
        return Result.of(Constants.CommonCode.INVALID_TYPE_VALUE);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.debug("handleMethodArgumentTypeMismatchException", e);
        return Result.of(Constants.CommonCode.INVALID_TYPE_VALUE);
    }

    /**
     * JSON Parse 에러 (예로, 값에 이스케이프가 안된 엔터가 들어가는 경우) 시에 던져지는
     * HttpMessageNotReadableException 예외를 처리하는 메소드이다.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.debug("handleHttpMessageNotReadableException", e);
        return Result.of(Constants.CommonCode.INVALID_JSON_SHAPE);
    }

    /**
     * 401 인증 에러.
     * API Token 인증은 {@link io.kakaoi.config.WebSecurityConfig} 내에서 처리 한다.
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected Result<?> handleAuthenticationException(AuthenticationException e) {
        log.debug("handleAuthenticationException", e);
        return Result.withMessage(Constants.CommonCode.UNAUTHORIZED, e.getMessage());
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden (Access Denied)"
    )
    protected Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.debug("handleAccessDeniedException", e);
        return Result.of(Constants.CommonCode.ACCESS_DENIED);
    }

    /**
     * 데이터를 찾을 수 없을 경우 발생 시키는 Exception.
     * 404 NOT FOUND 를 리턴한다.
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result<?> handleNoSuchElementException(NoSuchElementException e) {
        log.debug("NoSuchElementException", e);
        return Result.of(Constants.CommonCode.NOT_FOUND);
    }

    /**
     * 경로를 찾을 수 없을 경우 발생.
     * 404 전달.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Object  handleNoHandlerFoundException() throws URISyntaxException {
        if (StringUtils.isEmpty(applicationProperties.getErrorPage())) {
            return Result.withMessage(Constants.CommonCode.NOT_FOUND, "Not Found");
        } else {
            URI redirectUri = new URI(applicationProperties.getErrorPage());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     * 405 Method Not Allowed 리턴
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws URISyntaxException {
        log.debug("handleHttpRequestMethodNotSupportedException", e);
        if (StringUtils.isEmpty(applicationProperties.getErrorPage())) {
            return Result.of(Constants.CommonCode.METHOD_NOT_ALLOWED);
        } else {
            URI redirectUri = new URI(applicationProperties.getErrorPage());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }
    }

    /**
     * 요청 Content-Type 헤더가 잘못 사용되었을 때 발생
     * (보통 Content-Type 값이 application/xml, application/x-www-form-urlencoded 등을 사용했을 때)
     * 415 Unsupported media type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    protected Result<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.debug("handleHttpMediaTypeNotSupportedException", e);
        return Result.of(Constants.CommonCode.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Service 내에서 비즈니스 로직 상 오류가 발생했을 때 발생 시킨 오류를 처리하는 메소드이다.
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Result<?>> handleBusinessException(final BusinessException e) {
        final Constants.CommonCode errorCode = e.getErrorCode();

        Result<?> result;

        if (errorCode.getDefaultHttpStatus().is5xxServerError()) {
            log.error("handleBusinessException", e);
            result = Result.of(errorCode);
        }
        else {
            log.debug("handleBusinessException", e);
            if (e.getData() != null) {
                result = Result.withMessage(errorCode, e.getMessage(), e.getData());
            }
            else {
                result = Result.withMessage(errorCode, e.getMessage());
            }
        }

        return new ResponseEntity<>(result, errorCode.getDefaultHttpStatus());
    }

    /**
     * 기타 알 수 없는 에러들을 처리하기 위한 메소드.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
        responseCode = "500",
        description =
            "Internal Server Error\n\n" +
            "* 9XXX: Server Error / 서버 에러\n"
    )
    protected Result<?> handleException(Exception e) {
        log.error("handleException", e);
        return Result.error();
    }

    /**
     * Validation 시 발생한 Errors (BindingResult) 객체의 내용을 이용하여,
     * 필드 에러를 보기 쉽게 Map 으로 정리하는 메소드.
     * @param errors Errors (BindingResult) 객체.
     * @return 정리한 에러 데이터 Map List
     */
    private static List<Map<String, Object>> makeFieldErrorsMap(Errors errors) {
        if (errors != null) {
            return errors.getFieldErrors().stream()
                .map(fieldError -> {
                    HashMap<String, Object> map = new HashMap<>(3);
                    map.put("field", fieldError.getField());
                    map.put("value", fieldError.getRejectedValue());
                    map.put("message", fieldError.getDefaultMessage());
                    return Collections.unmodifiableMap(map);
                }).collect(Collectors.toList());
        }
        return null;
    }
}
