package io.kakaoi.service.iam;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("!mock-iam")
public class CloudIAMTokenProductionService implements CloudIAMTokenService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Application Credential 형태로 토큰 발급
     * @param appCreId Application Credential ID
     * @param appCreSecret Application Credential Secret
     * @return IAM 토큰 String
     */
    public String issueIAMToken(String appCreId, String appCreSecret) {
        return this.requestToken(makeRequestTokenBody(appCreId, appCreSecret));
    }

    /**
     * 계정 정보와 비밀번호로 토큰 발급
     * @param domainName 도메인 이름
     * @param projectName 프로젝트 이름
     * @param username 유저 이름
     * @param password 패스워드
     * @return IAM 토큰 String
     */
    public String issueIAMToken(String domainName, String projectName, String username, String password) {
        return this.requestToken(makeRequestTokenBody(domainName, projectName, username, password));
    }

    /**
     * 시스템 어드민 계정의 토큰을 발급한다.
     * @return 시스템 어드민 계정 토큰 String
     */
    public String issueSystemAdminToken() {
        ApplicationProperties.Account account = applicationProperties.getSystemAccount();

        return this.requestToken(
            makeRequestTokenBody(
                account.getDomainName(),
                account.getProjectName(),
                account.getUsername(),
                account.getPassword()
            )
        );
    }

    /**
     * 토큰 요청을 위한 메소드.
     * @param body 토큰 요청 본문
     * @return 토큰 문자열
     */
    private String requestToken(Object body) {
        URI uri = UriComponentsBuilder.fromUri(applicationProperties.getIam().getEndpoint())
            .path("/identity/v3/auth/tokens")
            .build().toUri();

        RequestEntity<Object> request = RequestEntity.post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(body);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);

            return response.getHeaders().getFirst(Constants.Headers.X_SUBJECT_TOKEN);
        }
        catch (HttpClientErrorException.NotFound | HttpClientErrorException.Unauthorized ignore) {
            throw new BusinessException(Constants.CommonCode.UNAUTHORIZED);
        }
        catch (RestClientException e) {
            throw new BusinessException(e, Constants.CommonCode.KIC_IAM_SERVICE_ERROR);
        }
    }

    /**
     * 계정 로그인 정보를 가지고 토큰을 요청시 필요한 body 항목을 만든다.
     * body Map 구성은 아래와 같다.
     * <pre>
     * {
     *   "auth": {
     *     "identity": {
     *       "method": ["password"],
     *       "password": {
     *         "user": {
     *           "domain": {
     *             "name": "__DOMAIN_NAME__"
     *           },
     *           "name": "__USERNAME__",
     *           "password": "__PASSWORD__"
     *         }
     *       }
     *     },
     *     "scope": {
     *       "project": {
     *         "domain": {
     *           "name": "__DOMAIN_NAME__"
     *         },
     *         "name": "__PROJECT_NAME__"
     *       }
     *     }
     *   }
     * }
     * </pre>
     * @param domainName 도메인 이름
     * @param projectName 프로젝트 이름 (빈 문자열 또는 null은 scope 포함하지 않음)
     * @param username 유저 이름
     * @param password 패스워드
     * @return 토큰 요청 Map Object
     */
    private static Map<String, Object> makeRequestTokenBody(String domainName, String projectName, String username, String password) {
        Map<String, Object> auth = new HashMap<>(2);

        Map<String, Object> identity = new HashMap<>(2);

        identity.put("methods", Collections.singletonList("password"));

        Map<String, Object> user = new HashMap<>(3);

        user.put("domain", Collections.singletonMap("name", domainName));
        user.put("name", username);
        user.put("password", password);

        identity.put("password", Collections.singletonMap("user", user));

        auth.put("identity", identity);

        if (StringUtils.hasText(projectName)) {
            Map<String, Object> project = new HashMap<>(2);

            project.put("name", projectName);
            project.put("domain", Collections.singletonMap("name", domainName));

            auth.put("scope", Collections.singletonMap("project", project));
        }

        return Collections.singletonMap("auth", auth);
    }

    /**
     * 계정 로그인 정보를 가지고 토큰을 요청시 필요한 body 항목을 만든다.
     * body Map 구성은 아래와 같다.
     * <pre>
     * {
     *   "auth": {
     *     "identity": {
     *       "method": ["application_credential"],
     *       "application_credential": {
     *         "id": "{appCreId}",
     *         "secret": "{appCreSecret}"
     *       }
     *     }
     *   }
     * }
     * </pre>
     * @param appCreId Application Credential ID
     * @param appCreSecret Application Credential Secret
     * @return 토큰 요청 Map Object
     */
    private static Map<String, Object> makeRequestTokenBody(String appCreId, String appCreSecret) {
        Map<String, Object> auth = new HashMap<>(2);

        Map<String, Object> identity = new HashMap<>(2);

        identity.put("methods", Collections.singletonList("application_credential"));

        Map<String, Object> appCre = new HashMap<>(3);

        appCre.put("id", appCreId);
        appCre.put("secret", appCreSecret);

        identity.put("application_credential", appCre);

        auth.put("identity", identity);

        return Collections.singletonMap("auth", auth);
    }

}
