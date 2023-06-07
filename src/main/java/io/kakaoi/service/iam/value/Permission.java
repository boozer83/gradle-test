package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Permission {

    private final String id;

    private final String kicServiceName;

    private final String httpMethod;

    private final String path;

    private final String description;

    @JsonCreator
    public Permission(
        @JsonProperty("id") String id,
        @JsonProperty("kic_service_name") String kicServiceName,
        @JsonProperty("http_method") String httpMethod,
        @JsonProperty("path") String path,
        @JsonProperty("description") String description
    ) {
        this.id = id;
        this.kicServiceName = kicServiceName;
        this.httpMethod = httpMethod;
        this.path = path;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getKicServiceName() {
        return kicServiceName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return id.equals(that.id)
                && kicServiceName.equals(that.kicServiceName)
                && Objects.equals(httpMethod, that.httpMethod)
                && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kicServiceName, httpMethod, path);
    }

    @Override
    public String toString() {
        return id;
    }

}
