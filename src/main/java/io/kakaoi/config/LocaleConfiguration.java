package io.kakaoi.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.locale.AngularCookieLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Bean
    public LocaleResolver localeResolver() {
        AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
        cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
        return cookieLocaleResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsConfiguration config = jHipsterProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            registry.addMapping("/**")
                .allowedOrigins(config.getAllowedOrigins().toArray(new String[config.getAllowedOrigins().size()]))
                .allowedHeaders(config.getAllowedHeaders().toArray(new String[config.getAllowedHeaders().size()]))
                .allowedHeaders(config.getExposedHeaders().toArray(new String[config.getExposedHeaders().size()]))
                .allowedMethods(config.getAllowedMethods().toArray(new String[config.getAllowedMethods().size()]));
        }
    }
}
