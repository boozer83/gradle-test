package io.kakaoi.service.iam.security;

import io.kakaoi.service.iam.value.IamIdName;
import io.kakaoi.service.iam.value.IamUserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cloud IAM 인증 정보를 보관하는 클래스
 */
public class CloudIamAuthenticationToken extends AbstractAuthenticationToken {

    private final IamUserInfo iamUserInfo;

    private final String username;

    private final String xAuthToken;

    /**
     * 인증 전 Token String 만 전달 시에 사용하는 생성자.
     * @param xAuthToken Token String
     */
    public CloudIamAuthenticationToken(String xAuthToken) {
        this(null, xAuthToken);
    }

    /**
     * 인증 되어 토큰 정보가 포함된 토큰 생성자
     * @param iamUserInfo 토큰 정보
     * @param xAuthToken Token string
     */
    public CloudIamAuthenticationToken(IamUserInfo iamUserInfo, String xAuthToken) {
        super(extractRoles(iamUserInfo));
        if (iamUserInfo != null) {
            this.setAuthenticated(true);
        }
        this.iamUserInfo = iamUserInfo;
        if (iamUserInfo != null && iamUserInfo.extractUser().isPresent()) {
            this.username = iamUserInfo.extractUser()
                .map(IamIdName::getName)
                .orElse(null);
        }
        else {
            this.username = null;
        }
        this.xAuthToken = xAuthToken;
    }

    private static List<? extends GrantedAuthority> extractRoles(IamUserInfo iamUserInfo) {
        if (iamUserInfo == null) {
            return Collections.emptyList();
        }
        return iamUserInfo.extractRoles().stream()
            .map(IamIdName::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return xAuthToken;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getDetails() {
        return iamUserInfo;
    }

    public IamUserInfo getIamUserInfo() {
        return iamUserInfo;
    }

}
