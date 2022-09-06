package io.kakaoi.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.config.Constants;
import io.kakaoi.web.rest.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * RequestRejectException 예외 시 응답 메시지 처리를 위한 AOP
 */
@Aspect
@Component
public class RequestRejectExceptionCatchAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Around("execution(public void org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public Object handleRequestRejectedException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        }
        catch (RequestRejectedException e) {
            HttpServletResponse resp = (HttpServletResponse) joinPoint.getArgs()[1];
            Result<?> errorMessage = Result.of(Constants.CommonCode.BAD_REQUEST);
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name().toLowerCase());
            objectMapper.writeValue(resp.getWriter(), errorMessage);
        }
        return null;
    }

}
