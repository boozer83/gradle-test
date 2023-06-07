package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

public class IdentityV3System {

    private final Boolean all;

    @JsonCreator
    public IdentityV3System(@JsonProperty("all") Boolean all) {
        this.all = all;
    }

    @Nullable
    public Boolean getAll() {
        return all;
    }

    public boolean isAll() {
        return all != null && all;
    }

}
