package io.kakaoi.web.rest.vm.vaild;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class SampleNamingRuleValidator implements ConstraintValidator<SampleNamingRule, String> {

    private static final int NAME_LENGTH = 100;

    private static final Pattern NAMING_PATTERN = Pattern.compile("[0-9a-zA-Z_]+");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value)
                && value.length() <= NAME_LENGTH
                && NAMING_PATTERN.matcher(value).matches();
    }

}
