package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamRole implements IamIdName {

    private final String id;

    private final String name;

    private final String description;

    private final String domainId;

    private final List<IamRoleExtra> extra;

    @JsonCreator
    public IamRole(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("domain_id") String domainId,
        @JsonProperty("extra") List<IamRoleExtra> extra
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.domainId = domainId;
        this.extra = extra;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDomainId() {
        return domainId;
    }

    public List<IamRoleExtra> getExtra() {
        return extra;
    }

}
