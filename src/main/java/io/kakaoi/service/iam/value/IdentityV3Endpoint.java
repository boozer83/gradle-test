package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdentityV3Endpoint {

    private final String id;

    private final String region;

    private final String regionId;

    private final String aInterface;

    private final String url;

    @JsonCreator
    public IdentityV3Endpoint(
        @JsonProperty("id") String id,
        @JsonProperty("region") String region,
        @JsonProperty("region_id") String regionId,
        @JsonProperty("interface") String aInterface,
        @JsonProperty("url") String url
    ) {
        this.id = id;
        this.region = region;
        this.regionId = regionId;
        this.aInterface = aInterface;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getInterface() {
        return aInterface;
    }

    public String getUrl() {
        return url;
    }

}
