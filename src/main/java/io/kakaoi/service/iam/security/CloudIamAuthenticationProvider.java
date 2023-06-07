package io.kakaoi.service.iam.security;

import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.iam.CloudIamInfoService;
import io.kakaoi.service.iam.value.IamUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;

/**
 * Cloud IAM 인증 처리 프로바이더 클래스
 */
@Component
public class CloudIamAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CloudIamAuthenticationProvider.class);

    private final CloudIamInfoService cloudIamInfoService;

    public CloudIamAuthenticationProvider(CloudIamInfoService cloudIamInfoService) {
        this.cloudIamInfoService = cloudIamInfoService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 이미 인증이 되어 있는 경우.
        if (authentication.isAuthenticated() && authentication.getPrincipal() != null) {
            return authentication;
        }

        Object credentials = authentication.getCredentials();

        if (credentials != null && StringUtils.hasText(credentials.toString())) {
            IamUserInfo userInfo;

            try {
                userInfo = cloudIamInfoService.getUserInfo(credentials.toString())
                    .orElseThrow(() -> new BadCredentialsException("Cloud IAM User Not Found"));
            }
            catch (BusinessException e) {
                if (e.getErrorCode() == Constants.CommonCode.KIC_IAM_SERVICE_ERROR) {
                    throw new AuthenticationServiceException("Server Error", e);
                }
                throw new BadCredentialsException(e.getMessage(), e);
            }

            if (!userInfo.extractProject().isPresent()) {
                log.debug("IAM Token is not Project Scope. Token: {}", credentials);
                throw new BadCredentialsException("Token is not Project Scope");
            }

            // 계정이 정상적으로 불러와 졌고, 토큰이 유효하면(시간 만료가 되지 않으면) 인증 완료.
            if (userInfo.isValid(OffsetDateTime.now())) {
                return new CloudIamAuthenticationToken(userInfo, credentials.toString());
            }
            throw new BadCredentialsException("Token Expired");
        }

        throw new BadCredentialsException("Token is Blank");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(CloudIamAuthenticationToken.class, authentication);
    }

}
