package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IamDomainPage extends IamAbstractPage<IamDomain> {

    private final List<IamDomain> domains;

    @JsonCreator
    public IamDomainPage(
        @JsonProperty("offset") Long offset,
        @JsonProperty("size") Long size,
        @JsonProperty("total") Long total,
        @JsonProperty("domains") List<IamDomain> domains
    ) {
        super(offset, size, total);
        this.domains = domains;
    }

    public List<IamDomain> getDomains() {
        return domains;
    }

    @Override
    public List<IamDomain> getElements() {
        return getDomains();
    }

}
