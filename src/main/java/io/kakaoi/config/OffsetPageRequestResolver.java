package io.kakaoi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Pageable Handler 상속 구현하여 OffsetPageRequest 기반으로 덮어쓴 클래스
 */
public class OffsetPageRequestResolver extends PageableHandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(OffsetPageRequestResolver.class);

    private final SortArgumentResolver sortArgumentResolver;

    public OffsetPageRequestResolver(SortArgumentResolver sortArgumentResolver) {
        this.sortArgumentResolver = sortArgumentResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ClassUtils.isAssignable(Pageable.class, parameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // <Long-term> Pageable page(offset) parse long
        String page = webRequest.getParameter(getParameterNameToUse(getPageParameterName(), parameter));
        String pageSize = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), parameter));

        Sort sort = sortArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        Pageable pageable = getPageable(parameter, page, pageSize);

        log.info("request pageable: {}", pageable);

        if (sort.isSorted()) {
            return new OffsetPageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return new OffsetPageRequest(pageable.getPageNumber(), pageable.getPageSize());
    }
}
