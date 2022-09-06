package io.kakaoi.service.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kakaoi.service.dto.IAMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cloud IAM 연결하지 않는 테스트 목적의 mocking service
 */
@Service
@Profile("mock-iam")
public class CloudIAMMockService implements CloudIAMService {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String MOCK_DOMAIN_ID = "__DOMAIN_ID__";

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

    @Value("classpath:data/mock_iam_userinfo.json")
    private Resource mockIamUserInfo;

    @Value("classpath:data/mock_iam_userinfo_member.json")
    private Resource mockIamUserInfoMember;

    @Value("classpath:data/mock_iam_userinfo_reader.json")
    private Resource mockIamUserInfoReader;

    @Value("classpath:data/mock_iam_userinfo_another.json")
    private Resource mockIamUserInfoAnother;

    @Override
    public IAMDTO.Info getUserInfo(String token) {
        return this.getUserInfoDummy(token);
    }

    /**
     * 사용자 정보 dummy
     * @return 더미 사용자 정보
     */
    private IAMDTO.Info getUserInfoDummy(String token) {
        try {
            if ("member".equals(token)) {
                return objectMapper.readValue(mockIamUserInfoMember.getInputStream(), IAMDTO.Info.class);
            }
            if ("reader".equals(token)) {
                return objectMapper.readValue(mockIamUserInfoReader.getInputStream(), IAMDTO.Info.class);
            }
            if ("another".equals(token)) {
                return objectMapper.readValue(mockIamUserInfoAnother.getInputStream(), IAMDTO.Info.class);
            }
            return objectMapper.readValue(mockIamUserInfo.getInputStream(), IAMDTO.Info.class);
        }
        catch (IOException ignore) {
            return null;
        }
    }

    @Override
    public IAMDTO.User getUser(String token, String userId) {
        return fetchAllUsers().stream()
            .filter(user -> user.getId().equals(userId))
            .findFirst().orElse(null);
    }

    @Override
    public IAMDTO.User getUserByName(String token, String domainId, String username) {
        return fetchAllUsers().stream()
            .filter(user -> user.getName().equals(username))
            .findFirst().orElse(null);
    }

    @Override
    public IAMDTO.User getUserByNameInProject(String token, String projectId, String username) {
        return fetchUsers(projectId).stream()
            .filter(user -> user.getName().equals(username))
            .findFirst().orElse(null);
    }

    @Override
    public List<IAMDTO.User> getUsersByNames(String token, String domainId, List<String> usernames) {
        if (!MOCK_DOMAIN_ID.equals(domainId) || usernames == null || usernames.isEmpty()) {
            return Collections.emptyList();
        }
        return fetchAllUsers().stream()
            .filter(user -> usernames.contains(user.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public IAMDTO.Domain getDomain(String token, String domainId) {
        if (MOCK_DOMAIN_ID.equals(domainId)) {
            return fetchDomain();
        }
        return null;
    }

    @Override
    public IAMDTO.Project getProject(String token, String projectId) {
        return fetchProject(projectId);
    }

    @Override
    public List<IAMDTO.User> getAllUsersInDomain(String token, String domainId) {
        if (MOCK_DOMAIN_ID.equals(domainId)) {
            return fetchAllUsers();
        }
        return Collections.emptyList();
    }

    @Override
    public List<IAMDTO.User> getAllUsersInDomain(String token, String domainId, String query) {
        if (MOCK_DOMAIN_ID.equals(domainId)) {
            return fetchAllUsers().stream()
                .filter(user -> user.getName().startsWith(query))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<IAMDTO.User> getAllUsersInProject(String token, String projectId) {
        return fetchUsers(projectId);
    }

    @Override
    public List<IAMDTO.User> getAllUsersInProject(String token, String projectId, String query) {
        return fetchUsers(projectId).stream()
                .filter(user -> user.getName().startsWith(query))
                .collect(Collectors.toList());
    }

    private List<IAMDTO.User> fetchUsers(String projectId) {
        try {
            if (MOCK_ANOTHER_PROJECT_ID.equals(projectId)) {
                return objectMapper.readValue(mockIamUserListAnotherResource.getInputStream(), IAMDTO.UsersPage.class)
                    .getUsers();
            }
            if (MOCK_PROJECT_ID.equals(projectId)) {
                return objectMapper.readValue(mockIamUserListResource.getInputStream(), IAMDTO.UsersPage.class).getUsers();
            }
        }
        catch (IOException ignore) { }

        return Collections.emptyList();
    }

    private List<IAMDTO.User> fetchAllUsers() {
        try {
            IAMDTO.UsersPage page = objectMapper.readValue(mockIamUserListResource.getInputStream(), IAMDTO.UsersPage.class);
            IAMDTO.UsersPage anotherPage = objectMapper.readValue(mockIamUserListAnotherResource.getInputStream(), IAMDTO.UsersPage.class);

            return Stream.concat(page.getUsers().stream(), anotherPage.getUsers().stream())
                .collect(Collectors.toList());
        }
        catch (IOException ignore) {
            return Collections.emptyList();
        }
    }

    private IAMDTO.Project fetchProject(String projectId) {
        try {
            IAMDTO.ProjectsPage projectsPage = objectMapper.readValue(mockIamProjectListResource.getInputStream(), IAMDTO.ProjectsPage.class);
            List<IAMDTO.Project> projects = projectsPage.getProjects();
            return projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst().orElse(null);
        }
        catch (IOException ignore) {
            return null;
        }
    }

    private IAMDTO.Domain fetchDomain() {
        try {
            IAMDTO.DomainsPage domainsPage = objectMapper.readValue(mockIamDomainListResource.getInputStream(), IAMDTO.DomainsPage.class);
            return domainsPage.getDomains().get(0);
        }
        catch (IOException ignore) {
            return null;
        }
    }

}
