package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdentityV3OSTrust {

    private final String id;

    private final Boolean impersonation;

    private final IdentityV3TrustUser trusteeUser;

    private final IdentityV3TrustUser trustorUser;

    @JsonCreator
    public IdentityV3OSTrust(
        @JsonProperty("id") String id,
        @JsonProperty("impersonation") Boolean impersonation,
        @JsonProperty("trustee_user") IdentityV3TrustUser trusteeUser,
        @JsonProperty("trustor_user") IdentityV3TrustUser trustorUser
    ) {
        this.id = id;
        this.impersonation = impersonation;
        this.trusteeUser = trusteeUser;
        this.trustorUser = trustorUser;
    }

    public String getId() {
        return id;
    }

    public Boolean getImpersonation() {
        return impersonation;
    }

    public IdentityV3TrustUser getTrusteeUser() {
        return trusteeUser;
    }

    public IdentityV3TrustUser getTrustorUser() {
        return trustorUser;
    }

}
