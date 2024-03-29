package io.kakaoi.config;

import io.kakaoi.service.iam.security.CloudIamAuthenticationToken;
import io.kakaoi.service.iam.value.IamIdName;
import io.kakaoi.service.iam.value.IamUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 애노테이션 @PreAuthorize 내에서 hasPermission 사용을 위한 클래스.
 */
@Component
public class ResourcePermissionEvaluator implements PermissionEvaluator {

    private static final Logger log = LoggerFactory.getLogger(ResourcePermissionEvaluator.class);

    /**
     * hasPermission 메소드 사용 시 접근 권한 판단.
     * 예를 들어, 컨트롤러 전역에서 @PreAuthorize("hasPermission(null, 'cr_admin')") 형태로 사용
     * @param authentication 인증 토큰
     * @param targetDomainObject 사용하지 않음 (null)
     * @param permission 필요 역할 타입 (CR_ADMIN, CR_EDITOR, CR_VIEWER)
     * @return 승인 여부
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.debug("hasPermission(targetDomainObject: {}, permission: {})", targetDomainObject, permission);
        if (StringUtils.isEmpty(permission)) {
            return false;
        }

        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(permission.toString()));
    }

    /**
     * hasPermission 메소드 사용 시 접근 권한 판단.
     * 예를 들어, 컨트롤러 전역에서 @PreAuthorize("hasPermission(#repositoryId, 'repositoryId', 'cr_admin')") 형태로 사용
     * @param authentication 인증 토큰
     * @param targetId 파라메터
     * @param targetType 파라메터 타입 (projectName, repositoryName, repositoryId, imageId, tagId)
     * @param permission 필요 역할 타입 (CR_ADMIN, CR_EDITOR, CR_VIEWER)
     * @return 승인 여부
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        log.debug("hasPermission(targetId: {}, targetType: {}, permission: {})", targetId, targetType, permission);
        if (StringUtils.isEmpty(targetId) || StringUtils.isEmpty(targetType) || StringUtils.isEmpty(permission)) {
            return false;
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(permission.toString()))) {
            return false;
        }

        return false;
    }

    private String extractProjectId(Authentication authentication) {
        if (authentication instanceof CloudIamAuthenticationToken) {
            CloudIamAuthenticationToken token = (CloudIamAuthenticationToken) authentication;
            IamUserInfo iamUserInfo = token.getIamUserInfo();

            return iamUserInfo.extractProject()
                .map(IamIdName::getId)
                .orElse("");
        }
        return "";
    }

}
