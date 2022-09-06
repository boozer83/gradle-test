package io.kakaoi.config;

import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;

/**
 * Application constants.
 */
public final class Constants {

    public static final String PRODUCTION_PROFILES_STR = "(stg | prod)";
    public static final String NOT_PRODUCTION_PROFILES_STR = "!" + PRODUCTION_PROFILES_STR;
    public static final Profiles PRODUCTION_PROFILES = Profiles.of(PRODUCTION_PROFILES_STR);
    public static final Profiles NOT_PRODUCTION_PROFILES = Profiles.of(NOT_PRODUCTION_PROFILES_STR);
    public static final Profiles NOT_EXACT_PROD_PROFILES = Profiles.of("!prod");

    public static class Headers {

        // IAM 인증 토큰 헤더
        public static final String X_AUTH_TOKEN = "X-Auth-Token";

        // IAM 토큰 발급 헤더
        public static final String X_SUBJECT_TOKEN = "X-Subject-Token";

    }

    private Constants() {}

    public enum CommonCode {

        OK("0200", "OK", HttpStatus.OK),
        ACCEPTED("0202", "Accepted", HttpStatus.ACCEPTED),

        BAD_REQUEST("0400", "Bad Request", HttpStatus.BAD_REQUEST),

        // 400 Bad Request Custom Code Error
        INVALID_INPUT_VALUE("1000", "Invalid Input Value", HttpStatus.BAD_REQUEST),
        INVALID_TYPE_VALUE("1001", "Invalid Type Value", HttpStatus.BAD_REQUEST),
        INVALID_JSON_SHAPE("1002", "Invalid Json Shape", HttpStatus.BAD_REQUEST),
        // TODO: 400 Bad Request Custom Code

        UNAUTHORIZED("0401", "Unauthorized", HttpStatus.UNAUTHORIZED),

        ACCESS_DENIED("0403", "Resource Access is Denied", HttpStatus.FORBIDDEN),

        NOT_FOUND("0404", "Resource Not Found", HttpStatus.NOT_FOUND),
        METHOD_NOT_ALLOWED("0405", "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
        CONFLICT("0409", "Conflict", HttpStatus.CONFLICT),
        UNSUPPORTED_MEDIA_TYPE("0415", "Unsupported Media Type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

        INTERNAL_SERVER_ERROR("9000", "Server Error", HttpStatus.INTERNAL_SERVER_ERROR), // 특정 불가능 서버 에러

        // 500 Internal Server Error Code Error
        KIC_IAM_SERVICE_ERROR("9001", "Server Error", HttpStatus.INTERNAL_SERVER_ERROR), // KIC IAM Service Error
        // TODO: 500 Internal Service Custom Code

        ;

        private final String code;
        private final String message;
        private final HttpStatus defaultHttpStatus;

        CommonCode(String code, String message, HttpStatus defaultHttpStatus) {
            this.code = code;
            this.message = message;
            this.defaultHttpStatus = defaultHttpStatus;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public HttpStatus getDefaultHttpStatus() {
            return defaultHttpStatus;
        }
    }

}
