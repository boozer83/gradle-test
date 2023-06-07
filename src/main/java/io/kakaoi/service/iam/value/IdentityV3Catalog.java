package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IdentityV3Catalog implements IamIdName {

    private final String id;

    private final String name;

    private final String type;

    private final List<IdentityV3Endpoint> endpoints;

    @JsonCreator
    public IdentityV3Catalog(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("endpoints") List<IdentityV3Endpoint> endpoints
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.endpoints = endpoints;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<IdentityV3Endpoint> getEndpoints() {
        return endpoints;
    }

}
