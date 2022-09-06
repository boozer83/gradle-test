package io.kakaoi.config;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    description = "KIC IAM Access Token",
    name = "X-Auth-Token",
    in = SecuritySchemeIn.HEADER
)
@Configuration
public class SpringDocConfiguration {

    private final ApplicationProperties applicationProperties;

    private final BuildProperties buildProperties;

    public SpringDocConfiguration(ApplicationProperties applicationProperties, BuildProperties buildProperties) {
        this.applicationProperties = applicationProperties;
        this.buildProperties = buildProperties;

        // Pageable interface 를 openapi spec doc 에서 인식하기 위한 설정
        SpringDocUtils.getConfig()
            .replaceWithClass(Pageable.class, SwaggerPageable.class)
            .replaceWithClass(Sort.class, SwaggerSort.class)
            .addRequestWrapperToIgnore(HttpEntity.class);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(info())
            .security(securityRequirements())
            .servers(servers());
    }

    private Info info() {
        return new Info()
            .title("KIC Example API")
            .description("KIC Example API Documentation")
            .version(buildProperties.getVersion());
    }

    private List<SecurityRequirement> securityRequirements() {
        return Collections.singletonList(new SecurityRequirement().addList("X-Auth-Token"));
    }

    private List<Server> servers() {
        return Collections.singletonList(
            new Server()
                .url(applicationProperties.getOutboundUrl())
                .description("API Server"));
    }


    /**
     * Swagger UI 표시 용도의 Pageable Parameter
     */
    public static class SwaggerPageable {

        @Min(0)
        @Parameter(description = "0부터 시작하는 offset index (0..N)", schema = @Schema(type = "integer", defaultValue = "0"))
        private Integer offset;

        /**
         * The Limit.
         */
        @Min(1)
        @Parameter(description = "페이지 당 표시할 갯수", schema = @Schema(type = "integer", defaultValue = "10"))
        private Integer limit;

        /**
         * The Sort.
         */
        @Parameter(description =
            "정렬 조건을 다음과 같은 포멧으로 전달: property(,asc|desc). "
                + "정렬 순서는 오름차순(asc)이 기본값. "
                + "다중 정렬 조건 지원."
            , name = "sort"
            , array = @ArraySchema(schema = @Schema(type = "string")))
        private List<String> sort;

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public List<String> getSort() {
            return sort;
        }

        public void setSort(List<String> sort) {
            this.sort = sort;
        }
    }

    /**
     * Swagger UI 표시 용도의 Sort Parameter
     */
    public static class SwaggerSort {

        /**
         * The Sort.
         */
        @Parameter(description =
            "정렬 조건을 다음과 같은 포멧으로 전달: property(,asc|desc). "
                + "정렬 순서는 오름차순(asc)이 기본값. "
                + "다중 정렬 조건 지원."
            , name = "sort"
            , array = @ArraySchema(schema = @Schema(type = "string")))
        private List<String> sort;

        public List<String> getSort() {
            return sort;
        }

        public SwaggerSort setSort(List<String> sort) {
            this.sort = sort;
            return this;
        }
    }
}
