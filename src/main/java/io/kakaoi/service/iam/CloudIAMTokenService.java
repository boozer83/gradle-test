package io.kakaoi.service.iam;

public interface CloudIAMTokenService {

    /**
     * Application Credential 형태로 토큰 발급
     * @param appCreId Application Credential ID
     * @param appCreSecret Application Credential Secret
     * @return IAM 토큰 String
     */
    String issueIAMToken(String appCreId, String appCreSecret);

    /**
     * 계정 정보와 비밀번호로 토큰 발급
     * @param domainName 도메인 이름
     * @param projectName 프로젝트 이름
     * @param username 유저 이름
     * @param password 패스워드
     * @return IAM 토큰 String
     */
    String issueIAMToken(String domainName, String projectName, String username, String password);

    /**
     * 시스템 어드민 계정의 토큰을 발급한다.
     * @return 시스템 어드민 계정 토큰 String
     */
    String issueSystemAdminToken();

}
