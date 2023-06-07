package io.kakaoi.config;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;

public class CorsPreFlightPassRequestMatcher implements RequestMatcher {

    private final RequestMatcher requestMatcher;

    public CorsPreFlightPassRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatcher.matches(request) && !CorsUtils.isPreFlightRequest(request);
    }

}
