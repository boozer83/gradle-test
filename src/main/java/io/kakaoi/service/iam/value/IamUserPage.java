package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IamUserPage extends IamAbstractPage<IamUser> {

    private final List<IamUser> users;

    @JsonCreator
    public IamUserPage(
        @JsonProperty("offset") Long offset,
        @JsonProperty("size") Long size,
        @JsonProperty("total") Long total,
        @JsonProperty("users") List<IamUser> users
    ) {
        super(offset, size, total);
        this.users = users;
    }

    public List<IamUser> getUsers() {
        return users;
    }

    @Override
    public List<IamUser> getElements() {
        return this.getUsers();
    }

}
