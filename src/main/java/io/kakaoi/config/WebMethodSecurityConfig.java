package io.kakaoi.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final ApplicationContext applicationContext;

    private final ResourcePermissionEvaluator resourcePermissionEvaluator;

    public WebMethodSecurityConfig(ApplicationContext applicationContext, ResourcePermissionEvaluator resourcePermissionEvaluator) {
        this.applicationContext = applicationContext;
        this.resourcePermissionEvaluator = resourcePermissionEvaluator;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(resourcePermissionEvaluator);
        handler.setApplicationContext(applicationContext);
        return handler;
    }
}
