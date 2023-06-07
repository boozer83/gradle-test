package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IamRolePage extends IamAbstractPage<IamRole> {

    private final List<IamRole> roles;

    @JsonCreator
    public IamRolePage(
        @JsonProperty("offset") Long offset,
        @JsonProperty("size") Long size,
        @JsonProperty("total") Long total,
        @JsonProperty("roles") List<IamRole> roles
    ) {
        super(offset, size, total);
        this.roles = roles;
    }

    public List<IamRole> getRoles() {
        return roles;
    }

    @Override
    public List<IamRole> getElements() {
        return this.getRoles();
    }

}
