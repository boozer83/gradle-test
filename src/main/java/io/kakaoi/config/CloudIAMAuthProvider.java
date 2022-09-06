package io.kakaoi.config;

import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.dto.IAMDTO;
import io.kakaoi.service.iam.CloudIAMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Cloud IAM 토큰을 받아 인증 처리를 한다.
 */
@Component
public class CloudIAMAuthProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CloudIAMAuthProvider.class);

    private final CloudIAMService cloudIAMService;

    public CloudIAMAuthProvider(CloudIAMService cloudIAMService) {
        this.cloudIAMService = cloudIAMService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 이미 인증이 되어 있는 경우.
        if (authentication.isAuthenticated() && authentication.getPrincipal() != null) {
            return authentication;
        }

        Object credentials = authentication.getCredentials();

        if (credentials != null && StringUtils.hasText(credentials.toString())) {
            IAMDTO.Info userInfo;

            try {
                userInfo = cloudIAMService.getUserInfo(credentials.toString());
            }
            catch (BusinessException e) {
                if (e.getErrorCode() == Constants.CommonCode.KIC_IAM_SERVICE_ERROR) {
                    throw new AuthenticationServiceException("Server Error", e);
                }
                throw new BadCredentialsException(e.getMessage(), e);
            }

            if (userInfo == null) {
                log.debug("Cloud IAM User Not Found. Token: {}", credentials);
                throw new BadCredentialsException("Cloud IAM User Not Found.");
            }

            if (userInfo.getProject() == null) {
                log.debug("IAM Token is not Project Scope. Token: {}", credentials);
                throw new BadCredentialsException("Token is not Project Scope.");
            }

            // 계정이 정상적으로 불러와 졌고, 계정이 활성화 되어있으면 인증 완료.
            if (userInfo.getUser().isEnabled()) {
                CloudIAMAuthToken cloudIAMAuthToken = new CloudIAMAuthToken(userInfo, credentials.toString());
                cloudIAMAuthToken.setAuthenticated(true);
                return cloudIAMAuthToken;
            }
            else {
                throw new DisabledException("Account is Disabled.");
            }
        }

        throw new BadCredentialsException("Token is Blank.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(CloudIAMAuthToken.class, authentication);
    }
}
