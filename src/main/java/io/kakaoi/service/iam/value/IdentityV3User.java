package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdentityV3User implements IamIdName {

    private final IdentityV3SimpleDomain domain;

    private final String id;

    private final String name;

    private final String passwordExpiresAt;

    @JsonCreator
    public IdentityV3User(
        @JsonProperty("domain") IdentityV3SimpleDomain domain,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("password_expires_at") String passwordExpiresAt
    ) {
        this.domain = domain;
        this.id = id;
        this.name = name;
        this.passwordExpiresAt = passwordExpiresAt;
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

    public String getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

}
