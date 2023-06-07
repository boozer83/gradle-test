package io.kakaoi.util;

import io.kakaoi.service.iam.security.CloudIamAuthenticationToken;
import io.kakaoi.service.iam.value.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 유효한 인증 구간에서의 {@link SecurityContextHolder}의 Cloud IAM 인증 정보를 가져오는 Util 클래스.
 */
public final class IamContextUtils {

    /**
     * Cloud IAM 인증 토큰 정보를 가져온다.
     * @return 인증 토큰 정보
     */
    public static Optional<IamUserInfo> getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return extractToken(authentication);
    }

    /**
     * Cloud IAM 인증 토큰의 유저가 소속된 도메인 ID를 가져온다.
     * @return 인증 유저의 도메인 ID. 없을 경우 "" (empty string)
     */
    public static String getCurrentUserDomainId() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractUserDomain)
            .map(IamIdName::getId)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 유저가 소속된 도메인 이름을 가져온다.
     * @return 인증 유저의 도메인 이름. 없을 경우 "" (empty string)
     */
    public static String getCurrentUserDomainName() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractUserDomain)
            .map(IamIdName::getName)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 프로젝트가 소속된 도메인 ID를 가져온다.
     * @return 인증 유저의 도메인 ID. 없을 경우 "" (empty string)
     */
    public static String getCurrentProjectDomainId() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractProjectDomain)
            .map(IamIdName::getId)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 프로젝트가 소속된 도메인 이름을 가져온다.
     * @return 인증 유저의 도메인 이름. 없을 경우 "" (empty string)
     */
    public static String getCurrentProjectDomainName() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractProjectDomain)
            .map(IamIdName::getName)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 프로젝트 ID를 가져온다.
     * @return 인증 토큰의 프로젝트 ID. 없을 경우 "" (empty string)
     */
    public static String getCurrentProjectId() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractProject)
            .map(IamIdName::getId)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 프로젝트 이름을 가져온다.
     * @return 인증 토큰의 프로젝트 이름. 없을 경우 "" (empty string)
     */
    public static String getCurrentProjectName() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractProject)
            .map(IamIdName::getName)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 유저 ID를 가져온다.
     * @return 인증 유저 ID. 없을 경우 "" (empty string)
     */
    public static String getCurrentUserId() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractUser)
            .map(IamIdName::getId)
            .orElse("");
    }

    /**
     * Cloud IAM 인증 토큰의 유저 이름을 가져온다.
     * @return 인증 유저 이름. 없을 경우 "" (empty string)
     */
    public static String getCurrentUsername() {
        return getCurrentToken()
            .flatMap(IamUserInfo::extractUser)
            .map(IamIdName::getName)
            .orElse("");
    }

    /**
     * X-Auth-Token 헤더의 토큰 문자열을 가져온다.
     * @return 토큰 문자열. 없을 경우 "" (empty string)
     */
    public static String getCredentialString() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CloudIamAuthenticationToken) {
            return authentication.getCredentials().toString();
        }
        return "";
    }

    private static Optional<IamUserInfo> extractToken(Authentication authentication) {
        if (authentication instanceof CloudIamAuthenticationToken) {
            return Optional.of(((CloudIamAuthenticationToken) authentication).getIamUserInfo());
        }
        return Optional.empty();
    }

    private IamContextUtils() {}

}
