package io.kakaoi.service.iam;

import io.kakaoi.config.ApplicationProperties;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.iam.value.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Profile("!mock-iam")
public class CloudIamInfoRequestService implements CloudIamInfoService {

    /**
     * 목록 API 호출 시 가져올 개수.
     * 최대 100개까지 가능.
     */
    private static final int EACH_FETCH_ROW_SIZE = 100;

    private final ApplicationProperties applicationProperties;

    private final RestTemplate restTemplate;

    public CloudIamInfoRequestService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
    }

    private URI iamEndpoint() {
        return applicationProperties.getIam().getEndpoint();
    }

    @Override
    public Optional<IdentityV3Token> getUserInfo(String token) {
        URI uri = UriComponentsBuilder.fromUri(iamEndpoint())
            .path("/identity/v3/auth/tokens")
            .build().toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Constants.Headers.X_AUTH_TOKEN, token);
        httpHeaders.add(Constants.Headers.X_SUBJECT_TOKEN, token);

        IdentityV3TokenSimpleResponse identityV3Token =
            this.request(HttpMethod.GET, httpHeaders, uri, null, false, IdentityV3TokenSimpleResponse.class);

        if (identityV3Token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(identityV3Token.getToken());
    }

    @Override
    public Optional<IamUser> getUser(String token, String userId) {
        URI uri = UriComponentsBuilder.fromUri(iamEndpoint())
            .path("/api/v1/users/{userId}")
            .build(userId);

        IamUser iamUser = this.requestGet(token, uri, IamUser.class);

        return Optional.ofNullable(iamUser);
    }

    @Override
    public Optional<IamUser> getUserByName(String token, String domainId, String username) {
        List<IamUser> users = this.getAllUsersInDomain(token, domainId, username);

        if (users == null || users.isEmpty()) {
            return Optional.empty();
        }

        return users.stream().findFirst();
    }

    @Override
    public Optional<IamUser> getUserByNameInProject(String token, String projectId, String username) {
        List<IamUser> users = this.getAllUsersInProject(token, projectId, username);

        if (users == null || users.isEmpty()) {
            return Optional.empty();
        }

        return users.stream().findFirst();
    }

    @Override
    public List<IamUser> getUsersByNames(String token, String domainId, List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return Collections.emptyList();
        }

        // TODO: 요청을 한건으로 단순화 할 방법 찾기
        return usernames.stream()
            .map(username -> this.getAllUsersInDomain(token, domainId, username))
            .filter(users -> !users.isEmpty())
            .map(users -> users.get(0))
            .collect(Collectors.toList());
    }

    @Override
    public List<IamUser> getUsersByNamesInProject(String token, String projectId, Set<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return Collections.emptyList();
        }

        return this.getAllUsersInProject(token, projectId).stream()
                .filter(user -> usernames.contains(user.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IamDomain> getDomain(String token, String domainId) {
        URI uri = UriComponentsBuilder.fromUri(iamEndpoint())
            .path("/api/v1/domains/{domainId}")
            .build(domainId);

        IamDomain iamDomain = this.requestGet(token, uri, IamDomain.class);

        return Optional.ofNullable(iamDomain);
    }

    @Override
    public Optional<IamProject> getProject(String token, String projectId) {
        URI uri = UriComponentsBuilder.fromUri(iamEndpoint())
            .path("/api/v1/projects/{projectId}")
            .build(projectId);

        IamProject iamProject = this.requestGet(token, uri, IamProject.class);

        return Optional.ofNullable(iamProject);
    }

    @Override
    public List<IamUser> getAllUsersInDomain(String token, String domainId) {
        return fetchUsers(nextOffset ->
                              UriComponentsBuilder.fromUri(iamEndpoint())
                                  .path("/api/v1/domains/{domainId}/users")
                                  .queryParam("offset", nextOffset)
                                  .queryParam("size", EACH_FETCH_ROW_SIZE)
                                  .build(domainId),
                          token);
    }

    @Override
    public List<IamUser> getAllUsersInDomain(String token, String domainId, String namePrefix) {
        return fetchUsers(nextOffset ->
                              UriComponentsBuilder.fromUri(iamEndpoint())
                                  .path("/api/v1/domains/{domainId}/users")
                                  .queryParam("offset", nextOffset)
                                  .queryParam("size", EACH_FETCH_ROW_SIZE)
                                  .queryParam("name", "{namePrefix}")
                                  .encode()
                                  .build(domainId, namePrefix),
                          token);
    }

    @Override
    public List<IamUser> getAllUsersInProject(String token, String projectId) {
        return fetchUsers(nextOffset ->
                              UriComponentsBuilder.fromUri(iamEndpoint())
                                  .path("/api/v1/projects/{projectId}/users")
                                  .queryParam("offset", nextOffset)
                                  .queryParam("size", EACH_FETCH_ROW_SIZE)
                                  .build(projectId),
                          token);
    }

    @Override
    public List<IamUser> getAllUsersInProject(String token, String projectId, String namePrefix) {
        return fetchUsers(nextOffset ->
                              UriComponentsBuilder.fromUri(iamEndpoint())
                                  .path("/api/v1/projects/{projectId}/users")
                                  .queryParam("offset", nextOffset)
                                  .queryParam("size", EACH_FETCH_ROW_SIZE)
                                  .queryParam("name", "{namePrefix}")
                                  .encode()
                                  .build(projectId, namePrefix),
                          token);
    }

    /**
     * 요청 Uri 제작 함수를 가지고 유저 정보를 가져오는 메소드이다.
     * @param uriCreation 요청 URI 생성 Function (현재 offset을 가져와 요청할 새 Uri를 생성한다.)
     * @return 전체 유저 목록
     */
    private List<IamUser> fetchUsers(Function<Long, URI> uriCreation, String token) {
        URI uri = uriCreation.apply(0L);

        IamUserPage firstUsers = this.requestGet(token, uri, IamUserPage.class);

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
     * 유저 목록을 가져올 때 전체를 가져오지 못했으면 페이지를 순환하며 가져온다.
     * @param firstUsersPage 응답받은 첫 페이지 데이터
     * @param token IAM 토큰
     * @param uriCreation 요청 URI 생성 Function (현재 offset을 가져와 요청할 새 Uri를 생성한다.)
     * @return 전체 유저 목록
     */
    private List<IamUser> fetchContinuousUsers(IamUserPage firstUsersPage, String token, Function<Long, URI> uriCreation) {
        long offset = firstUsersPage.getSize();

        List<IamUser> result = new LinkedList<>(firstUsersPage.getUsers());

        while (offset < firstUsersPage.getTotal()) {
            URI uri = uriCreation.apply(offset);

            try {
                IamUserPage users = this.requestGet(token, uri, true, IamUserPage.class);
                if (users == null) {
                    continue;
                }
                result.addAll(users.getUsers());
                offset += users.getSize();
            }
            catch (NoSuchElementException ignore) {
                break;
            }
        }
        return result;
    }

    /**
     * IAM GET 공통 요청을 위한 메소드.
     * @param token IAM Token
     * @param uri 요청 URI
     * @param clz 응답 시 반환 타입 클래스
     * @param <T> 반환 타입 클래스
     * @return 응답 본문을 맵핑한 객체
     */
    private <T> T requestGet(String token, URI uri, Class<T> clz) {
        return this.requestGet(token, uri, false, clz);
    }

    /**
     * IAM GET 공통 요청을 위한 메소드.
     * @param token IAM Token
     * @param uri 요청 URI
     * @param is404Exception 404 에러 시에 예외 발생 여부
     * @param clz 응답 시 반환 타입 클래스
     * @param <T> 반환 타입 클래스
     * @return 응답 본문을 맵핑한 객체
     */
    private <T> T requestGet(String token, URI uri, boolean is404Exception, Class<T> clz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            httpHeaders.add(Constants.Headers.X_AUTH_TOKEN, token);
        }
        return this.request(HttpMethod.GET, httpHeaders, uri, null, is404Exception, clz);
    }

    /**
     * IAM 공통 요청을 위한 메소드.
     * @param method HTTP Method
     * @param headers HTTP Headers
     * @param uri 요청 URI
     * @param body 요청 본문 (nullable)
     * @param is404Exception 404 에러 시에 예외 발생 여부
     * @param clz 응답 시 반환 타입 클래스
     * @param <T> 반환 타입 클래스
     * @return 응답 본문을 맵핑한 객체
     */
    private <T> T request(HttpMethod method, HttpHeaders headers, URI uri, Object body, boolean is404Exception, Class<T> clz) {
        RequestEntity.BodyBuilder reqBuilder = RequestEntity.method(method, uri);

        reqBuilder.headers(headers);
        if (body != null) {
            reqBuilder.contentType(MediaType.APPLICATION_JSON);
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
            if (is404Exception) {
                throw new NoSuchElementException();
            }
            return null;
        }
        catch (HttpClientErrorException.Forbidden ignore) {
            throw new BusinessException(Constants.CommonCode.ACCESS_DENIED);
        }
        catch (HttpClientErrorException.Unauthorized ignore) {
            throw new BusinessException(Constants.CommonCode.UNAUTHORIZED);
        }
        catch (RestClientException e) {
            throw new BusinessException(e, Constants.CommonCode.KIC_IAM_SERVICE_ERROR);
        }
    }

}
