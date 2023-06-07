package io.kakaoi.service.iam.value;

import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Iam UserInfo 인터페이스
 */
public interface IamUserInfo {

    /**
     * User Id, Name 정보
     */
    Optional<? extends IamIdName> extractUser();

    /**
     * Project Id, Name 정보
     */
    Optional<? extends IamIdName> extractProject();

    /**
     * User Domain Id, Name 정보
     */
    Optional<? extends IamIdName> extractUserDomain();

    /**
     * Project Domain Id, Name 정보
     */
    Optional<? extends IamIdName> extractProjectDomain();

    /**
     * Role Id, Name 정보 리스트
     */
    List<? extends IamIdName> extractRoles();

    /**
     * 토큰이 유효한지 확인
     * @param now 현재 시간 (not null)
     * @return 토큰 유효 체크여부
     */
    boolean isValid(@NonNull OffsetDateTime now);

}
