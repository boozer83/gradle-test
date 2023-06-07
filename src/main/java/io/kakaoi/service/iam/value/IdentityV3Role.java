package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

public class IdentityV3Role implements IamIdName, GrantedAuthority {

    private final String id;

    private final String name;

    @JsonCreator
    public IdentityV3Role(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return name;
    }

}
