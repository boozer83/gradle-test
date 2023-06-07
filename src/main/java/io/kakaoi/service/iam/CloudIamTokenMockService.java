package io.kakaoi.service.iam;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock-iam")
public class CloudIamTokenMockService implements CloudIamTokenService {

    @Override
    public String issueIAMToken(String appCreId, String appCreSecret) {
        return appCreId;
    }

    @Override
    public String issueIAMToken(String domainName, String projectName, String username, String password) {
        return username;
    }

    @Override
    public String issueSystemAdminToken() {
        return "test";
    }
}
