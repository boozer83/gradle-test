package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamRoleExtra {

    private final String displayName;

    private final Integer displayOrder;

    private final String description;

    private final String scope;

    @JsonCreator
    public IamRoleExtra(
        @JsonProperty("display_name") String displayName,
        @JsonProperty("display_order") Integer displayOrder,
        @JsonProperty("description") String description,
        @JsonProperty("scope") String scope
    ) {
        this.displayName = displayName;
        this.displayOrder = displayOrder;
        this.description = description;
        this.scope = scope;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public String getScope() {
        return scope;
    }

}
