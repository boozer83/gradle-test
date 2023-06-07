package io.kakaoi.service.iam.value;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * mocking token.
 * issuedAt, expiresAt 변경 메서드 포함.
 */
public class IdentityV3TokenMock extends IdentityV3Token {

    public IdentityV3TokenMock(
        IdentityV3OSTrust osTrust,
        IdentityV3ApplicationCredential applicationCredential,
        IdentityV3System system,
        IdentityV3SimpleDomain domain,
        IdentityV3SimpleProject project,
        IdentityV3User user,
        List<IdentityV3Role> roles,
        List<String> methods,
        List<String> auditIds,
        List<IdentityV3Catalog> catalog,
        Boolean isDomain,
        OffsetDateTime expiresAt,
        OffsetDateTime issuedAt
    ) {
        super(osTrust, applicationCredential, system, domain, project, user, roles, methods, auditIds, catalog,
              isDomain,
              expiresAt, issuedAt);
    }


    /**
     * 주의! 디버그용. 토큰 이슈, 만료 시간 재 설정 메서드.
     * @param issuedAt 토큰 발급 시간
     * @return 토큰 발급, 만료 시간이 재 설정된 토큰
     */
    public IdentityV3TokenMock withIssuedAt(OffsetDateTime issuedAt) {
        return new IdentityV3TokenMock(
            this.getOsTrust(),
            this.getApplicationCredential(),
            this.getSystem(),
            this.getDomain(),
            this.getProject(),
            this.getUser(),
            this.getRoles(),
            this.getMethods(),
            this.getAuditIds(),
            this.getCatalog(),
            this.isDomain(),
            issuedAt.plusHours(12),
            issuedAt
        );
    }

}
