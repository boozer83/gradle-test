package io.kakaoi.config;

import io.kakaoi.aop.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.NOT_PRODUCTION_PROFILES_STR)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
