package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionCheckResponse implements IdentityV3TokenResponse {

    private final Boolean allow;

    private final IdentityV3Token token;

    @JsonCreator
    public PermissionCheckResponse(
        @JsonProperty("allow") Boolean allow,
        @JsonProperty("token") IdentityV3Token token
    ) {
        this.allow = allow;
        this.token = token;
    }

    public boolean isAllow() {
        return allow != null && allow;
    }

    public Boolean getAllow() {
        return allow;
    }

    @Override
    public IdentityV3Token getToken() {
        return token;
    }

}
