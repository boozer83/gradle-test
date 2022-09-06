package io.kakaoi.config;

import io.kakaoi.service.dto.IAMDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Cloud IAM 인증 시에 Spring Security 내에 담기는 토큰 정보이다.
 * 실제 유저 정보는 {@link #getPrincipal()}를 호출하면 받을 수 있다.
 */
public class CloudIAMAuthToken extends AbstractAuthenticationToken {

    private final IAMDTO.Info iamInfo;

    private final String username;

    private final String headerToken;

    /**
     * 인증 전 Token String 만 전달 시에 사용하는 생성자.
     * @param headerToken Token String
     */
    public CloudIAMAuthToken(String headerToken) {
        super(Collections.emptyList());
        this.iamInfo = null;
        this.username = null;
        this.headerToken = headerToken;
    }

    /**
     * 인증 후 정보가 같이 담기는 형태의 생성자.
     * @param iamInfo 인증 사용자의 {@link IAMDTO.Info} 형태의 정보
     * @param headerToken Token String
     */
    public CloudIAMAuthToken(IAMDTO.Info iamInfo, String headerToken) {
        super(mappingAuthorities(iamInfo));
        this.iamInfo = iamInfo;
        this.username = iamInfo.getUser().getName();
        this.headerToken = headerToken;
    }

    /**
     * IAM 정보를 가지고 권한 정보를 맵핑한다. IAM 권한을 보고 CR 내 권한을 삽입한다.
     * @param iamInfo IAM 정보
     * @return 권한 Set List
     */
    private static Collection<? extends GrantedAuthority> mappingAuthorities(IAMDTO.Info iamInfo) {
        // TODO: Mapping IAM Role
        return iamInfo.getUser().getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * IAM 인증 시 사용한 Token String을 반환한다.
     * @return Token String
     */
    @Override
    public Object getCredentials() {
        return headerToken;
    }

    /**
     * Cloud IAM에 등록한 Username을 가져온다.
     * @return 유저 이름
     */
    @Override
    public Object getPrincipal() {
        return username;
    }

    /**
     * 인증한 {@link IAMDTO.Info} 형태의 유저 정보를 반환한다.
     * 만약 인증이 안되어 있다면, null이 담겨있다.
     * @return 유저 정보
     */
    @Override
    public Object getDetails() {
        return iamInfo;
    }
}
