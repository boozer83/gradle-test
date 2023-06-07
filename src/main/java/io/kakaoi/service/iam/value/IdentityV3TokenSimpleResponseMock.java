package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentityV3TokenSimpleResponseMock implements IdentityV3TokenResponse {

    private final IdentityV3TokenMock token;

    @JsonCreator
    public IdentityV3TokenSimpleResponseMock(@JsonProperty("token") IdentityV3TokenMock token) {
        this.token = token;
    }

    @Override
    public IdentityV3TokenMock getToken() {
        return token;
    }

}
