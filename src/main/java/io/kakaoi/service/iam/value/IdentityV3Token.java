package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IdentityV3Token implements IamUserInfo {

    private final IdentityV3OSTrust osTrust;

    private final IdentityV3ApplicationCredential applicationCredential;

    private final IdentityV3System system;

    private final IdentityV3SimpleDomain domain;

    private final IdentityV3SimpleProject project;

    private final IdentityV3User user;

    private final List<IdentityV3Role> roles;

    private final List<String> methods;

    private final List<String> auditIds;

    private final List<IdentityV3Catalog> catalog;

    private final Boolean isDomain;

    private final OffsetDateTime expiresAt;

    private final OffsetDateTime issuedAt;

    @JsonCreator
    public IdentityV3Token(
            @JsonProperty("OS-TRUST:trust") IdentityV3OSTrust osTrust,
            @JsonProperty("application_credential") IdentityV3ApplicationCredential applicationCredential,
            @JsonProperty("system") IdentityV3System system,
            @JsonProperty("domain") IdentityV3SimpleDomain domain,
            @JsonProperty("project") IdentityV3SimpleProject project,
            @JsonProperty("user") IdentityV3User user,
            @JsonProperty("roles") List<IdentityV3Role> roles,
            @JsonProperty("methods") List<String> methods,
            @JsonProperty("audit_ids") List<String> auditIds,
            @JsonProperty("catalog") List<IdentityV3Catalog> catalog,
            @JsonProperty("is_domain") Boolean isDomain,
            @JsonProperty("expires_at") OffsetDateTime expiresAt,
            @JsonProperty("issued_at") OffsetDateTime issuedAt
    ) {
        this.osTrust = osTrust;
        this.applicationCredential = applicationCredential;
        this.system = system;
        this.domain = domain;
        this.project = project;
        this.user = user;
        this.roles = roles;
        this.methods = methods;
        this.auditIds = auditIds;
        this.catalog = catalog;
        this.isDomain = isDomain;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
    }

    @JsonProperty("OS-TRUST:trust")
    public IdentityV3OSTrust getOsTrust() {
        return osTrust;
    }

    // application_credential
    public IdentityV3ApplicationCredential getApplicationCredential() {
        return applicationCredential;
    }

    public IdentityV3System getSystem() {
        return system;
    }

    public IdentityV3SimpleDomain getDomain() {
        return domain;
    }

    public IdentityV3SimpleProject getProject() {
        return project;
    }

    public IdentityV3User getUser() {
        return user;
    }

    public List<IdentityV3Role> getRoles() {
        return roles;
    }

    public List<String> getMethods() {
        return methods;
    }

    public List<String> getAuditIds() {
        return auditIds;
    }

    public List<IdentityV3Catalog> getCatalog() {
        return catalog;
    }

    public boolean isDomain() {
        return isDomain != null && isDomain;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    @Override
    public Optional<IdentityV3User> extractUser() {
        return Optional.ofNullable(this.getUser());
    }

    @Override
    public Optional<IdentityV3SimpleProject> extractProject() {
        return Optional.ofNullable(this.getProject());
    }

    @Override
    public Optional<IdentityV3SimpleDomain> extractUserDomain() {
        return extractUser().map(IdentityV3User::getDomain);
    }

    @Override
    public Optional<IdentityV3SimpleDomain> extractProjectDomain() {
        return extractProject().map(IdentityV3SimpleProject::getDomain);
    }

    @Override
    public List<IdentityV3Role> extractRoles() {
        if (this.getRoles() == null) {
            return Collections.emptyList();
        }
        return this.getRoles();
    }

    @Override
    public boolean isValid(@NonNull OffsetDateTime now) {
        return now.isBefore(this.getExpiresAt());
    }

}
