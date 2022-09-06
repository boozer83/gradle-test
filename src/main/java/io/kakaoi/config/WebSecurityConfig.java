package io.kakaoi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.web.rest.Result;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.CorsFilter;

/**
 * 보안 및 인증 설정
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;

    private final CorsFilter corsFilter;

    private final CloudIAMAuthProvider cloudIAMAuthProvider;

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(ApplicationProperties applicationProperties, CorsFilter corsFilter, CloudIAMAuthProvider cloudIAMAuthProvider, ObjectMapper objectMapper) {
        this.applicationProperties = applicationProperties;
        this.corsFilter = corsFilter;
        this.cloudIAMAuthProvider = cloudIAMAuthProvider;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationProvider를 등록한다.
     * 여기선 {@link CloudIAMAuthProvider}를 등록한다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(cloudIAMAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/assets/**")
            .antMatchers("/resources/**")
            .antMatchers("/css/**")
            .antMatchers("/vendor/**")
            .antMatchers("/js/**")
            .antMatchers("/favicon*/**")
            .antMatchers("/img/**")
            .antMatchers("/swagger-resources/**")
            .antMatchers("/swagger-ui.html")
            .antMatchers("/v3/api-docs*")
            .antMatchers("/webjars/**")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(cloudIAMAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, CsrfFilter.class)
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
            .and().csrf()
                .disable()
            .headers()
                .frameOptions().disable()
            .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * {@link CloudIAMAuthFilter}의 객체를 만든다.
     * @return CloudIAMAuthFilter의 객체
     */
    private CloudIAMAuthFilter cloudIAMAuthFilter() throws Exception {
        return new CloudIAMAuthFilter(authenticationManagerBean(), authenticationFailureHandler());
    }

    /**
     * 권한 에러 시에 오류 메시지를 던지는 핸들러 구현체를 만든다.
     * @return AccessDeniedHandler 구현체
     */
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            Result<?> result = Result.of(Constants.CommonCode.ACCESS_DENIED);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), result);
        };
    }

    /**
     * 권한 에러 시에 오류 메시지를 던지는 핸들러 구현체를 만든다.
     * @return AccessDeniedHandler 구현체
     */
    private AccessDeniedHandler hostAccessDeniedHandler() {
        return (request, response, ex) -> {
            if (applicationProperties.getErrorPage() == null) {
                Result<?> result = Result.of(Constants.CommonCode.NOT_FOUND);
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), result);
            }
            else {
                response.setStatus(HttpStatus.SEE_OTHER.value());
                response.setHeader(HttpHeaders.LOCATION, applicationProperties.getErrorPage());
            }
        };
    }

    /**
     * 인증 오류 시에 오류 메시지를 던지는 핸들러 구현체를 만든다.
     * @return AuthenticationFailureHandler 구현체
     */
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, ex) -> {
            Result<?> result = Result.withMessage(Constants.CommonCode.UNAUTHORIZED, ex.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), result);
        };
    }

}

