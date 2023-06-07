package io.kakaoi.service.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.config.Constants;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.service.iam.value.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Profile("mock-iam")
public class CloudIamInfoMockService implements CloudIamInfoService {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String MOCK_DOMAIN_ID = "__DOMAIN_ID__";

    private static final String MOCK_SYS_DOMAIN_ID = "__SYS_DOMAIN_ID__";

    private static final String MOCK_SYS_PROJECT_ID = "__SYS_PROJECT_ID__";

    private static final String MOCK_ANOTHER_PROJECT_ID = "__ANOTHER_PROJECT_ID__";

    private static final String MOCK_PROJECT_ID = "__PROJECT_ID__";

    @Value("classpath:data/mock_iam_domain_list.json")
    private Resource mockIamDomainListResource;

    @Value("classpath:data/mock_iam_project_list.json")
    private Resource mockIamProjectListResource;

    @Value("classpath:data/mock_iam_user_list.json")
    private Resource mockIamUserListResource;

    @Value("classpath:data/mock_iam_user_list_another.json")
    private Resource mockIamUserListAnotherResource;

    @Value("classpath:data/mock_iam_user_list_system.json")
    private Resource mockIamUserListSystemResource;

    @Value("classpath:data/mock_iam_user_token_admin.json")
    private Resource iamDummyResource;

    @Value("classpath:data/mock_iam_user_token_member.json")
    private Resource iamMemberDummyResource;

    @Value("classpath:data/mock_iam_user_token_reader.json")
    private Resource iamReaderDummyResource;

    @Value("classpath:data/mock_iam_user_token_none.json")
    private Resource iamNoneDummyResource;

    @Value("classpath:data/mock_iam_user_token_sys.json")
    private Resource iamSysDummyResource;

    @Override
    public Optional<IdentityV3Token> getUserInfo(String token) {
        IdentityV3TokenSimpleResponseMock userInfoDummy = this.getUserInfoDummy(token);

        if (userInfoDummy == null) {
            return Optional.empty();
        }
        return Optional.of(userInfoDummy.getToken().withIssuedAt(OffsetDateTime.now().minus(Duration.ofMillis(500))));
    }

    /**
     * 사용자 정보 dummy
     * @return 더미 사용자 정보
     */
    private IdentityV3TokenSimpleResponseMock getUserInfoDummy(String token) {
        try {
            if ("sys".equals(token)) {
                return objectMapper.readValue(iamSysDummyResource.getInputStream(), IdentityV3TokenSimpleResponseMock.class);
            }
            if ("member".equals(token)) {
                return objectMapper.readValue(iamMemberDummyResource.getInputStream(), IdentityV3TokenSimpleResponseMock.class);
            }
            if ("reader".equals(token)) {
                return objectMapper.readValue(iamReaderDummyResource.getInputStream(), IdentityV3TokenSimpleResponseMock.class);
            }
            if ("another".equals(token)) {
                return objectMapper.readValue(iamNoneDummyResource.getInputStream(), IdentityV3TokenSimpleResponseMock.class);
            }
            return objectMapper.readValue(iamDummyResource.getInputStream(), IdentityV3TokenSimpleResponseMock.class);
        }
        catch (IOException e) {
            throw new BusinessException("User Info IO", Constants.CommonCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Optional<IamUser> getUser(String token, String userId) {
        return fetchAllUsers(token).stream()
            .filter(user -> user.getId().equals(userId))
            .findFirst();
    }

    @Override
    public Optional<IamUser> getUserByName(String token, String domainId, String username) {
        return fetchAllUsers(token).stream()
            .filter(user -> user.getName().equals(username))
            .findFirst();
    }

    @Override
    public Optional<IamUser> getUserByNameInProject(String token, String projectId, String username) {
        return fetchUsers(token, projectId).stream()
            .filter(user -> user.getName().equals(username))
            .findFirst();
    }

    @Override
    public List<IamUser> getUsersByNames(String token, String domainId, List<String> usernames) {
        if (!MOCK_DOMAIN_ID.equals(domainId) || usernames == null || usernames.isEmpty()) {
            return Collections.emptyList();
        }
        return fetchAllUsers(token).stream()
            .filter(user -> usernames.contains(user.getName()))
            .collect(Collectors.toList());
    }
    @Override
    public List<IamUser> getUsersByNamesInProject(String token, String projectId, Set<String> usernames) {
        return fetchUsers(token, projectId).stream()
                .filter(user -> usernames.contains(user.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IamDomain> getDomain(String token, String domainId) {
        return fetchDomain(domainId);
    }

    @Override
    public Optional<IamProject> getProject(String token, String projectId) {
        return fetchProject(projectId);
    }

    @Override
    public List<IamUser> getAllUsersInDomain(String token, String domainId) {
        return fetchAllUsers(token);
    }

    @Override
    public List<IamUser> getAllUsersInDomain(String token, String domainId, String namePrefix) {
        return fetchAllUsers(token).stream()
            .filter(user -> user.getName().startsWith(namePrefix))
            .collect(Collectors.toList());
    }

    @Override
    public List<IamUser> getAllUsersInProject(String token, String projectId) {
        return fetchUsers(token, projectId);
    }

    @Override
    public List<IamUser> getAllUsersInProject(String token, String projectId, String namePrefix) {
        return fetchUsers(projectId, projectId).stream()
            .filter(user -> user.getName().startsWith(namePrefix))
            .collect(Collectors.toList());
    }

    private List<IamUser> fetchUsers(String token, String projectId) {
        return fetchAllUsers(token).stream()
            .filter(user -> user.getDefaultProjectId().equals(projectId))
            .collect(Collectors.toList());
    }

    private List<IamUser> fetchAllUsers(String token) {
        Optional<IdentityV3Token> optionalToken = this.getUserInfo(token);
        if (!optionalToken.isPresent()) {
            throw new BusinessException(Constants.CommonCode.UNAUTHORIZED);
        }
        IdentityV3Token tokenInfo = optionalToken.get();

        IdentityV3SimpleProject project = tokenInfo.getProject();

        if (project == null) {
            return Collections.emptyList();
        }

        if (project.getId().equals(MOCK_PROJECT_ID)) {
            return this.parseJsonUsers(mockIamUserListResource);
        }
        if (project.getId().equals(MOCK_ANOTHER_PROJECT_ID)) {
            return this.parseJsonUsers(mockIamUserListAnotherResource);
        }
        if (project.getId().equals(MOCK_SYS_PROJECT_ID)) {
            return Stream.of(
                    this.parseJsonUsers(mockIamUserListResource).stream(),
                    this.parseJsonUsers(mockIamUserListAnotherResource).stream(),
                    this.parseJsonUsers(mockIamUserListSystemResource).stream())
                .flatMap(userStream -> userStream)
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<IamUser> parseJsonUsers(Resource resource) {
        try {
            IamUserPage page = objectMapper.readValue(resource.getInputStream(), IamUserPage.class);
            if (page == null) {
                return Collections.emptyList();
            }
            return page.getUsers();
        }
        catch (IOException e) {
            throw new BusinessException(e, Constants.CommonCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<IamProject> fetchProject(String projectId) {
        try {
            IamProjectPage projectsPage = objectMapper.readValue(mockIamProjectListResource.getInputStream(), IamProjectPage.class);
            List<IamProject> projects = projectsPage.getProjects();
            if (projects == null) {
                return Optional.empty();
            }
            return projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst();
        }
        catch (IOException e) {
            throw new BusinessException(e, Constants.CommonCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<IamDomain> fetchDomain(String domainId) {
        try {
            IamDomainPage domainsPage = objectMapper.readValue(mockIamDomainListResource.getInputStream(), IamDomainPage.class);
            List<IamDomain> domains = domainsPage.getDomains();
            if (domains == null) {
                return Optional.empty();
            }
            return domains.stream()
                .filter(domain -> domain.getId().equals(domainId))
                .findFirst();
        }
        catch (IOException e) {
            throw new BusinessException(e, Constants.CommonCode.INTERNAL_SERVER_ERROR);
        }
    }

}
