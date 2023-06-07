package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3TrustUser {

    private final String id;

    @JsonCreator
    public IdentityV3TrustUser(@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
