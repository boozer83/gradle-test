package io.kakaoi.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;

/**
 * OffsetPageRequest 기반으로 덮어쓴 Pageable로 기본 변경 설정 클래스
 */
@Configuration
public class PageableConfiguration extends SpringDataWebConfiguration {

    public PageableConfiguration(
        ApplicationContext context,
        @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService
    ) {
        super(context, conversionService);
    }

    @Bean
    @Override
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        OffsetPageRequestResolver resolver = new OffsetPageRequestResolver(sortResolver());
        resolver.setFallbackPageable(new OffsetPageRequest(0, 10));
        resolver.setPageParameterName("offset");
        resolver.setSizeParameterName("limit");
        return resolver;
    }

}
