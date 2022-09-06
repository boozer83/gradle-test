package io.kakaoi.service.iam;

import io.kakaoi.service.dto.IAMDTO;

import java.util.List;

/**
 * Cloud IAM 요청을 위한 서비스 인터페이스
 */
public interface CloudIAMService {

    /**
     * IAM Token 을 사용한 사용자 정보 요청
     * @param token IAM 토큰
     * @return 인증한 사용자 정보
     */
    IAMDTO.Info getUserInfo(String token);

    /**
     * 하나의 유저 정보를 가져온다.
     * @param userId 유저 ID
     * @return 유저 정보
     */
    IAMDTO.User getUser(String token, String userId);

    /**
     * 도메인 내에서 유저 이름을 바탕으로 유저 정보를 가져온다.
     * @param username 유저 이름
     * @param domainId domain ID
     * @param token IAM Token
     * @return 유저 정보
     */
    IAMDTO.User getUserByName(String token, String domainId, String username);

    /**
     * 프로젝트 내에서 유저 이름을 바탕으로 유저 정보를 가져온다.
     * @param username 유저 이름
     * @param projectId 프로젝트 ID
     * @param token IAM Token
     * @return 유저 정보
     */
    IAMDTO.User getUserByNameInProject(String token, String projectId, String username);

    /**
     * 유저 이름 목록을 벌크로 하여 유저 정보 리스트를 가져온다.
     * @param usernames 유저 이름 리스트
     * @return 유저 정보 리스트
     */
    List<IAMDTO.User> getUsersByNames(String token, String domainId, List<String> usernames);

    /**
     * 도메인 ID를 이용하여 도메인 정보를 불러온다.
     * @param domainId 도메인 ID
     * @return 도메인 정보
     */
    IAMDTO.Domain getDomain(String token, String domainId);

    /**
     * 프로젝트 ID를 이용하여 프로젝트 정보를 가져온다.
     * @param projectId 프로젝트 ID
     * @return 프로젝트 정보
     */
    IAMDTO.Project getProject(String token, String projectId);

    /**
     * 도메인 ID를 이용하여 도메인에 소속한 유저를 불러온다.
     * @param domainId 도메인 ID
     * @return 유저 정보 리스트
     */
    List<IAMDTO.User> getAllUsersInDomain(String token, String domainId);

    /**
     * 도메인 ID를 이용하여 도메인에 소속한 유저를 불러온다.
     * @param domainId 도메인 ID
     * @param query user name prefix query
     * @return 유저 정보 리스트
     */
    List<IAMDTO.User> getAllUsersInDomain(String token, String domainId, String query);

    /**
     * IAM으로 부터 프로젝트 내 모든 유저 들을 가져온다.
     * @param projectId 프로젝트 ID
     * @return 유저 정보 리스트
     */
    List<IAMDTO.User> getAllUsersInProject(String token, String projectId);

    /**
     * IAM으로 부터 프로젝트 내 모든 유저 들을 가져온다.
     * @param projectId 프로젝트 ID
     * @param query user name prefix query
     * @return 유저 정보 리스트
     */
    List<IAMDTO.User> getAllUsersInProject(String token, String projectId, String query);

}
