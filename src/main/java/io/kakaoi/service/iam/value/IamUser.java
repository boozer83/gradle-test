package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamUser implements IamIdName {

    private final IdentityV3SimpleDomain domain;

    private final String id;

    private final String name;

    private final String email;

    private final String nickname;

    private final String realname;

    private final String defaultProjectId;

    private final String passwordChangedAt;

    private final String passwordExpiresAt;

    private final Boolean enabled;

    private final String profileUrl;

    private final String status;

    private final String createdAt;

    private final String disabledAt;

    private final String telephone;

    private final String serviceAccountType;

    private final List<String> roles;

    private final List<String> groupNames;

    private final List<String> groupRoles;

    @JsonCreator
    public IamUser(
        @JsonProperty("domain") IdentityV3SimpleDomain domain,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("nickname") String nickname,
        @JsonProperty("realname") String realname,
        @JsonProperty("default_project_id") String defaultProjectId,
        @JsonProperty("password_changed_at") String passwordChangedAt,
        @JsonProperty("password_expires_at") String passwordExpiresAt,
        @JsonProperty("enabled") Boolean enabled,
        @JsonProperty("profile_url") String profileUrl,
        @JsonProperty("status") String status,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("disabled_at") String disabledAt,
        @JsonProperty("telephone") String telephone,
        @JsonProperty("service_account_type") String serviceAccountType,
        @JsonProperty("roles") List<String> roles,
        @JsonProperty("group_names") List<String> groupNames,
        @JsonProperty("group_roles") List<String> groupRoles
    ) {
        this.domain = domain;
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.realname = realname;
        this.defaultProjectId = defaultProjectId;
        this.passwordChangedAt = passwordChangedAt;
        this.passwordExpiresAt = passwordExpiresAt;
        this.enabled = enabled;
        this.profileUrl = profileUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.disabledAt = disabledAt;
        this.telephone = telephone;
        this.serviceAccountType = serviceAccountType;
        this.roles = roles;
        this.groupNames = groupNames;
        this.groupRoles = groupRoles;
    }

    public IdentityV3SimpleDomain getDomain() {
        return domain;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRealname() {
        return realname;
    }

    public String getDefaultProjectId() {
        return defaultProjectId;
    }

    public String getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public String getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDisabledAt() {
        return disabledAt;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getServiceAccountType() {
        return serviceAccountType;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public List<String> getGroupRoles() {
        return groupRoles;
    }

}
