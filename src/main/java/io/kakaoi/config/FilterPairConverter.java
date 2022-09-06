package io.kakaoi.config;

import io.kakaoi.web.rest.vm.FilterVM;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * "filter,condition" 형태의 String To FilterPair 컨버터
 */
@Component
public class FilterPairConverter implements Converter<String, FilterVM.FilterPair> {

    @Override
    public FilterVM.FilterPair convert(String source) {
        String[] split = source.split(",");
        if (split.length >= 2) {
            String keywords = String.join(",", Arrays.copyOfRange(split, 1, split.length));
            return new FilterVM.FilterPair(split[0], keywords);
        }
        throw new IllegalArgumentException("Cannot convert to FilterPair: '" + source + "'");
    }

}
