package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3ApplicationCredential implements IamIdName {

    private final String id;

    private final String name;

    private final Boolean restricted;

    @JsonCreator
    public IdentityV3ApplicationCredential(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("restricted") Boolean restricted
    ) {
        this.id = id;
        this.name = name;
        this.restricted = restricted;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Boolean getRestricted() {
        return restricted;
    }

}
