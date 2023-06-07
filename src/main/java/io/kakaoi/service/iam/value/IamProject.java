package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamProject implements IamIdName {

    private final String id;

    private final String name;

    private final String nickname;

    private final Boolean enabled;

    private final String domainId;

    private final String description;

    private final String disabledAt;

    private final String createdAt;

    @JsonCreator
    public IamProject(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("nickname") String nickname,
        @JsonProperty("enabled") Boolean enabled,
        @JsonProperty("domain_id") String domainId,
        @JsonProperty("description") String description,
        @JsonProperty("disabled_at") String disabledAt,
        @JsonProperty("created_at") String createdAt
    ) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.enabled = enabled;
        this.domainId = domainId;
        this.description = description;
        this.disabledAt = disabledAt;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getDescription() {
        return description;
    }

    public String getDisabledAt() {
        return disabledAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
