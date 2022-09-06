package io.kakaoi.service.iam;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.dto.IAMDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Cloud IAM 연동 서비스
 */
@Service
@Profile("!mock-iam")
public class CloudIAMProductionService implements CloudIAMService {

    private final ApplicationProperties applicationProperties;

    private final RestTemplate restTemplate;

    public CloudIAMProductionService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public IAMDTO.Info getUserInfo(String token) {
        URI uri = UriComponentsBuilder.fromUri(endpoint())
                    .path("/api/v1/auth/userinfo")
                    .build().toUri();

        return this.request(HttpMethod.GET, token, uri, null, IAMDTO.Info.class);
    }

    @Override
    public IAMDTO.User getUser(String token, String userId) {
        URI uri = UriComponentsBuilder.fromUri(endpoint())
                    .path("/api/v1/users/{userId}")
                    .build(userId);

        return this.request(HttpMethod.GET, token, uri, null, IAMDTO.User.class);
    }

    @Override
    public IAMDTO.User getUserByName(String token, String domainId, String username) {
        List<IAMDTO.User> users = this.getAllUsersInDomain(token, domainId, username);

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    @Override
    public IAMDTO.User getUserByNameInProject(String token, String domainId, String username) {
        List<IAMDTO.User> users = this.getAllUsersInProject(token, domainId, username);

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    @Override
    public List<IAMDTO.User> getUsersByNames(String token, String domainId, List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return Collections.emptyList();
        }

        return usernames.stream()
            .map(username -> this.getAllUsersInDomain(token, domainId, username))
            .filter(users -> !users.isEmpty())
            .map(users -> users.get(0))
            .collect(Collectors.toList());
    }

    @Override
    public IAMDTO.Domain getDomain(String token, String domainId) {
        URI uri = UriComponentsBuilder.fromUri(endpoint())
                    .path("/api/v1/domains/{domainId}")
                    .build(domainId);

        return this.request(HttpMethod.GET, token, uri, null, IAMDTO.Domain.class);
    }

    @Override
    public IAMDTO.Project getProject(String token, String projectId) {
        URI uri = UriComponentsBuilder.fromUri(endpoint())
                    .path("/api/v1/projects/{projectId}")
                    .build(projectId);

        return this.request(HttpMethod.GET, token, uri, null, IAMDTO.Project.class);
    }

    // 주의! API 최대 갯수는 100개.
    private static final int EACH_FETCH_ROW_SIZE = 100;

    @Override
    public List<IAMDTO.User> getAllUsersInDomain(String token, String domainId) {
        return fetchUsers(nextOffset ->
            UriComponentsBuilder.fromUri(endpoint())
                .path("/api/v1/domains/{domainId}/users")
                .queryParam("offset", nextOffset)
                .queryParam("size", EACH_FETCH_ROW_SIZE)
                .build(domainId),
            token);
    }

    @Override
    public List<IAMDTO.User> getAllUsersInDomain(String token, String domainId, String query) {
        return fetchUsers(nextOffset ->
            UriComponentsBuilder.fromUri(endpoint())
                .path("/api/v1/domains/{domainId}/users")
                .queryParam("offset", nextOffset)
                .queryParam("size", EACH_FETCH_ROW_SIZE)
                .queryParam("name", "{query}")
                .encode()
                .build(domainId, query),
            token);
    }

    @Override
    public List<IAMDTO.User> getAllUsersInProject(String token, String projectId) {
        return fetchUsers(nextOffset ->
            UriComponentsBuilder.fromUri(endpoint())
                .path("/api/v1/projects/{projectId}/users")
                .queryParam("offset", nextOffset)
                .queryParam("size", EACH_FETCH_ROW_SIZE)
                .build(projectId),
            token);
    }

    @Override
    public List<IAMDTO.User> getAllUsersInProject(String token, String projectId, String query) {
        return fetchUsers(nextOffset ->
            UriComponentsBuilder.fromUri(endpoint())
                .path("/api/v1/projects/{projectId}/users")
                .queryParam("offset", nextOffset)
                .queryParam("size", EACH_FETCH_ROW_SIZE)
                .queryParam("name", "{query}")
                .encode()
                .build(projectId, query),
            token);
    }

    /**
     * 요청 Uri 제작 함수를 가지고 유저 정보를 가져오는 메소드이다.
     * @param uriCreation 요청 URI 생성 Function (현재 offset을 가져와 요청할 새 Uri를 생성한다.)
     * @return 전체 유저 목록
     */
    private List<IAMDTO.User> fetchUsers(Function<Integer, URI> uriCreation, String token) {
        URI uri = uriCreation.apply(0);

        IAMDTO.UsersPage firstUsers = this.request(HttpMethod.GET, token, uri, null, IAMDTO.UsersPage.class);

        if (firstUsers == null || firstUsers.getUsers() == null) {
            return Collections.emptyList();
        }

        if (firstUsers.getTotal() <= firstUsers.getSize()) {
            return firstUsers.getUsers();
        }
        // 페이지 내 모든 정보가 담기지 않았을 경우 재요청 후 모두 가져온다.
        return fetchContinuousUsers(firstUsers, token, uriCreation);
    }

    /**
     * 유저 목록을 가져올 때 전체를 가져오지 못했으면 페이지를 순한하며 가져온다.
     * @param firstUsersPage 응답받은 첫 페이지 데이터
     * @param token IAM 토큰
     * @param uriCreation 요청 URI 생성 Function (현재 offset을 가져와 요청할 새 Uri를 생성한다.)
     * @return 전체 유저 목록
     */
    private List<IAMDTO.User> fetchContinuousUsers(IAMDTO.UsersPage firstUsersPage, String token, Function<Integer, URI> uriCreation) {
        int offset = firstUsersPage.getSize();

        ArrayList<IAMDTO.User> result = new ArrayList<>(firstUsersPage.getTotal());
        result.addAll(firstUsersPage.getUsers());

        while (offset < firstUsersPage.getTotal()) {
            URI uri = uriCreation.apply(offset);

            IAMDTO.UsersPage users = this.request(HttpMethod.GET, token, uri, null, IAMDTO.UsersPage.class);
            result.addAll(users.getUsers());
            offset += users.getSize();
        }
        return result;
    }

    private URI endpoint() {
        return applicationProperties.getIam().getEndpoint();
    }

    /**
     * IAM 공통 요청을 위한 메소드.
     * @param method HTTP Method
     * @param token IAM Token
     * @param uri 요청 URI
     * @param body 요청 본문 (nullable)
     * @param clz 응답 시 반환 타입 클래스
     * @param <T> 반환 타입 클래스
     * @return 응답 본문을 맵핑한 객체
     */
    private <T> T request(HttpMethod method, String token, URI uri, Object body, Class<T> clz) {
        RequestEntity.BodyBuilder reqBuilder = RequestEntity.method(method, uri);

        if (body != null) {
            reqBuilder.headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON));
        }
        if (StringUtils.hasText(token)) {
            reqBuilder.header(Constants.Headers.X_AUTH_TOKEN, token);
        }
        reqBuilder.accept(MediaType.APPLICATION_JSON);

        RequestEntity<?> request = body == null
            ? reqBuilder.build()
            : reqBuilder.body(body);

        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(request, clz);

            return responseEntity.getBody();
        }
        catch (HttpClientErrorException.NotFound ignore) {
            throw new BusinessException(Constants.CommonCode.NOT_FOUND);
        }
        catch (HttpClientErrorException.Unauthorized ignore) {
            throw new BusinessException(Constants.CommonCode.UNAUTHORIZED);
        }
        catch (RestClientException e) {
            throw new BusinessException(e, Constants.CommonCode.KIC_IAM_SERVICE_ERROR);
        }
    }

}
