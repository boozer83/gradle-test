package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3SimpleDomain implements IamIdName {

    private final String id;

    private final String name;

    @JsonCreator
    public IdentityV3SimpleDomain(
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

}
