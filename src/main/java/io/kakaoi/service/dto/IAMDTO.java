package io.kakaoi.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

public final class IAMDTO {

    /**
     * Cloud IAM User info
     */
    @Schema(name = "IAMInfo")
    public static class Info {
        private String authToken;
        private IdAndName domain;
        private ProjectSummery project;
        private List<Role> roles;
        private User user;

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public IdAndName getDomain() {
            return domain;
        }

        public void setDomain(IdAndName domain) {
            this.domain = domain;
        }

        public ProjectSummery getProject() {
            return project;
        }

        public void setProject(ProjectSummery project) {
            this.project = project;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    /**
     * User
     */
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class User extends IdAndName {

        private IdAndName domain;
        private String email;
        private String nickname;
        private String realname;
        private String defaultProjectId;
        private String passwordExpiresAt;
        private boolean enabled;
        private String profileUrl;
        private String status;
        private String createdAt;
        private List<String> roles;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDefaultProjectId() {
            return defaultProjectId;
        }

        public void setDefaultProjectId(String defaultProjectId) {
            this.defaultProjectId = defaultProjectId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPasswordExpiresAt() {
            return passwordExpiresAt;
        }

        public void setPasswordExpiresAt(String passwordExpiresAt) {
            this.passwordExpiresAt = passwordExpiresAt;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public IdAndName getDomain() {
            return domain;
        }

        public void setDomain(IdAndName domain) {
            this.domain = domain;
        }
    }

    /**
     * Role
     */
    public static class Role extends IdAndName {
        private String description;
        private String domain_id;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDomain_id() {
            return domain_id;
        }

        public void setDomain_id(String domain_id) {
            this.domain_id = domain_id;
        }

    }

    /**
     * ID And Name
     */
    public static class IdAndName {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdAndName idAndName = (IdAndName) o;
            return Objects.equals(id, idAndName.id) && Objects.equals(name, idAndName.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    public static abstract class AbstractPage<T> {

        private int offset;

        private int size;

        private int total;

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public abstract List<T> getElements();

        public abstract void setElements(List<T> elements);
    }

    public static class UsersPage extends AbstractPage<User> {

        private List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        @Override
        public List<User> getElements() {
            return this.getUsers();
        }

        @Override
        public void setElements(List<User> elements) {
            this.setUsers(elements);
        }
    }

    public static class ProjectsPage extends AbstractPage<Project> {

        private List<Project> projects;

        public List<Project> getProjects() {
            return projects;
        }

        public void setProjects(List<Project> projects) {
            this.projects = projects;
        }

        @Override
        public List<Project> getElements() {
            return this.getProjects();
        }

        @Override
        public void setElements(List<Project> elements) {
            this.setProjects(elements);
        }
    }

    public static class DomainsPage extends AbstractPage<Domain> {

        private List<Domain> domains;

        public List<Domain> getDomains() {
            return domains;
        }

        public void setDomains(List<Domain> domains) {
            this.domains = domains;
        }

        @Override
        public List<Domain> getElements() {
            return this.getDomains();
        }

        @Override
        public void setElements(List<Domain> elements) {
            this.setDomains(elements);
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Project extends IdAndName {

        private String nickname;

        private boolean enabled;

        private String domainId;

        private String description;

        private String deletedAt;

        private String createdAt;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Domain extends IdAndName {

        private String description;

        private boolean enabled;

        private String createdAt;

        private boolean userCreationEnabled;

        private boolean userDeletionEnabled;

        private boolean userModificationEnabled;

        private boolean projectCreationEnabled;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isUserCreationEnabled() {
            return userCreationEnabled;
        }

        public void setUserCreationEnabled(boolean userCreationEnabled) {
            this.userCreationEnabled = userCreationEnabled;
        }

        public boolean isUserDeletionEnabled() {
            return userDeletionEnabled;
        }

        public void setUserDeletionEnabled(boolean userDeletionEnabled) {
            this.userDeletionEnabled = userDeletionEnabled;
        }

        public boolean isUserModificationEnabled() {
            return userModificationEnabled;
        }

        public void setUserModificationEnabled(boolean userModificationEnabled) {
            this.userModificationEnabled = userModificationEnabled;
        }

        public boolean isProjectCreationEnabled() {
            return projectCreationEnabled;
        }

        public void setProjectCreationEnabled(boolean projectCreationEnabled) {
            this.projectCreationEnabled = projectCreationEnabled;
        }
    }

    public static class ProjectSummery extends IdAndName {

        private IdAndName domain;

        public IdAndName getDomain() {
            return domain;
        }

        public void setDomain(IdAndName domain) {
            this.domain = domain;
        }
    }

    public static class UsernameMap {

        private Map<String, String> users = new HashMap<>();

        public Map<String, String> getUsers() {
            return users;
        }

        public void setUsers(Map<String, String> users) {
            this.users = users;
        }
    }

    public static class SuggestedUsers {

        private List<SuggestedUser> users = new ArrayList<>();

        public List<SuggestedUser> getUsers() {
            return users;
        }

        public void setUsers(List<SuggestedUser> users) {
            this.users = users;
        }
    }

    public static class SuggestedUser extends IdAndName {

        private String nickname;

        private String realname;

        private String email;

        public SuggestedUser() {}

        public SuggestedUser(User user) {
            setId(user.getId());
            setName(user.getName());
            this.nickname = user.getNickname();
            this.realname = user.getRealname();
            this.email = user.getEmail();
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    private IAMDTO() {}
}
