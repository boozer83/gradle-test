package io.kakaoi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;


@Configuration
@EnableJpaRepositories("io.kakaoi.repository")
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    /**
     * AuditorAware interface를 람다 형태의 익명 객체로 생성한다.
     * 람다에서는 AbstractAuditingEntity 내 CreatedBy, LastModifiedBy 정보를
     * 자동으로 담아주기 위해 인증 정보의 ID String을 추출한다.
     * @return AuditorAware 객체
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .map(Object::toString);
    }

}
