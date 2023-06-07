package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamDomain {

    private final String id;

    private final String name;

    private final String ownerId;

    private final Boolean enabled;

    private final String description;

    private final String projectDefaultEndpointGroup;

    private final Boolean projectCreationEnabled;

    private final Long projectLimit;

    private final Boolean kicLoginEnabled;

    private final Boolean mfaEnabled;

    private final String createdAt;

    private final String disabledAt;

    private final List<Map<String, Object>> federatedDomain;

    @JsonCreator
    public IamDomain(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("owner_id") String ownerId,
        @JsonProperty("enabled") Boolean enabled,
        @JsonProperty("description") String description,
        @JsonProperty("project_default_endpoint_group") String projectDefaultEndpointGroup,
        @JsonProperty("project_creation_enabled") Boolean projectCreationEnabled,
        @JsonProperty("project_limit") Long projectLimit,
        @JsonProperty("kic_login_enabled") Boolean kicLoginEnabled,
        @JsonProperty("mfa_enabled") Boolean mfaEnabled,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("disabled_at") String disabledAt,
        @JsonProperty("federated_domain") List<Map<String, Object>> federatedDomain
    ) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.enabled = enabled;
        this.description = description;
        this.projectDefaultEndpointGroup = projectDefaultEndpointGroup;
        this.projectCreationEnabled = projectCreationEnabled;
        this.projectLimit = projectLimit;
        this.kicLoginEnabled = kicLoginEnabled;
        this.mfaEnabled = mfaEnabled;
        this.createdAt = createdAt;
        this.disabledAt = disabledAt;
        this.federatedDomain = federatedDomain;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectDefaultEndpointGroup() {
        return projectDefaultEndpointGroup;
    }

    public Boolean getProjectCreationEnabled() {
        return projectCreationEnabled;
    }

    public Long getProjectLimit() {
        return projectLimit;
    }

    public Boolean getKicLoginEnabled() {
        return kicLoginEnabled;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDisabledAt() {
        return disabledAt;
    }

    public List<Map<String, Object>> getFederatedDomain() {
        return federatedDomain;
    }

}
