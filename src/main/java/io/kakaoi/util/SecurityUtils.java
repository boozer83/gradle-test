package io.kakaoi.util;

import io.kakaoi.config.CloudIAMAuthToken;
import io.kakaoi.security.AuthoritiesConstants;
import io.kakaoi.service.dto.IAMDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<IAMDTO.Info> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractDetails(securityContext.getAuthentication()));
    }

    /**
     * 현재 인증한 유저의 프로젝트 ID를 가져온다.
     * @return 프로젝트 ID
     */
    public static String getCurrentUserProjectId() {
        return getCurrentUserLogin()
            .map(IAMDTO.Info::getProject)
            .map(IAMDTO.IdAndName::getId)
            .orElse("");
    }

    /**
     * 현재 인증 유저의 이름을 가져온다.
     * @return username
     */
    public static String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return extractPrincipal(securityContext.getAuthentication());
    }

    /**
     * 인증 시 사용한 X-Auth-Token 의 값을 가져온다.
     * @return 인증 시 사용한 토큰 값
     */
    public static String getCredentialToken() {
        return extractCredentials(SecurityContextHolder.getContext().getAuthentication());
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication != null
                && authentication.getPrincipal() != null) {
            return authentication.getPrincipal().toString();
        }
        return "";
    }

    private static IAMDTO.Info extractDetails(Authentication authentication) {
        if (authentication != null
            && authentication.getDetails() != null
            && ClassUtils.isAssignableValue(CloudIAMAuthToken.class, authentication)) {
            return (IAMDTO.Info) authentication.getDetails();
        }
        return null;
    }

    private static String extractCredentials(Authentication authentication) {
        if (authentication != null
            && authentication.getCredentials() != null) {
            return authentication.getCredentials().toString();
        }
        return "";
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).anyMatch(authority::equals);
    }

    /**
     * 유저의 등록한 Role 정보가 존재하는지 체크한다.
     * @param authority Role 정보
     * @return Role 존재 여부
     */
    public static boolean containsRole(GrantedAuthority authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(authority);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority);
    }

}
