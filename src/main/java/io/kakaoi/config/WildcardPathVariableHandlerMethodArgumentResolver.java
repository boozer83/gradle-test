package io.kakaoi.config;

import org.springframework.core.MethodParameter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class WildcardPathVariableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * AntPathMatcher
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(WildcardPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        // /path1/path2/**
        String pattern = (String) httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        // /path1/path2/path3/path4
        String path = (String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // 와일드카드 이후로 얻기
        String wildcardPath = antPathMatcher.extractPathWithinPattern(pattern, path);

        return "/"+wildcardPath;

        // "/"으로 분리
//        String[] splitPaths = StringUtils.split(wildcardPath, AntPathMatcher.DEFAULT_PATH_SEPARATOR);

//        return splitPaths;
    }



}
