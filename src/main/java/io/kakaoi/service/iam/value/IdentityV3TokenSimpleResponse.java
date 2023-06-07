package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3TokenSimpleResponse implements IdentityV3TokenResponse {

    private final IdentityV3Token token;

    @JsonCreator
    public IdentityV3TokenSimpleResponse(@JsonProperty("token") IdentityV3Token token) {
        this.token = token;
    }

    @Override
    public IdentityV3Token getToken() {
        return token;
    }

}
