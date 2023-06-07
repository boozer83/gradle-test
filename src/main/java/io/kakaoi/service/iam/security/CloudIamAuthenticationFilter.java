package io.kakaoi.service.iam.security;

import io.kakaoi.config.Constants;
import io.kakaoi.config.CorsPreFlightPassRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cloud IAM 인증 필터.
 * X-Auth-Token Header 내 토큰을 {@link CloudIamAuthenticationProvider} 를 통해 인증 시도한다.
 */
public class CloudIamAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CloudIamAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationFailureHandler authenticationFailureHandler) {
        super(authRequestMatcher());
        setAuthenticationManager(authenticationManager);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    private static RequestMatcher authRequestMatcher() {
        return new CorsPreFlightPassRequestMatcher(new AntPathRequestMatcher("/api/**"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = request.getHeader(Constants.Headers.X_AUTH_TOKEN);
        return getAuthenticationManager().authenticate(new CloudIamAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

}
