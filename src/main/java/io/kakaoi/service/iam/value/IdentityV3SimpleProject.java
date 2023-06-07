package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3SimpleProject implements IamIdName {

    private final IdentityV3SimpleDomain domain;

    private final String id;

    private final String name;

    @JsonCreator
    public IdentityV3SimpleProject(
        @JsonProperty("domain") IdentityV3SimpleDomain domain,
        @JsonProperty("id") String id,
        @JsonProperty("name") String name
    ) {
        this.domain = domain;
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

    public IdentityV3SimpleDomain getDomain() {
        return domain;
    }

}
