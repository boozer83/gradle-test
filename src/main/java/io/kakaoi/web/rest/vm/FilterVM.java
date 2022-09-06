package io.kakaoi.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterVM {

    private List<@NotBlank String> keyword = new ArrayList<>();

    @Parameter(
        description = "검색 조건을 'filter,condition' 형태로 받는다.",
        array = @ArraySchema(schema = @Schema(type = "string", example = "filter,condition"))
    )
    private List<FilterPair> filter = new ArrayList<>();

    /**
     * 기본 생성자
     */
    public FilterVM() {}

    /**
     * 전체 검색에 대한 검색 키워드를 위한 생성자.
     * @param keyword 검색 키워드
     */
    public FilterVM(String keyword) {
        this.keyword = Collections.singletonList(keyword);
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public List<FilterPair> getFilter() {
        return filter;
    }

    public void setFilter(List<FilterPair> filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "FilterVM{" +
            "keywords=" + keyword +
            ", filters=" + filter +
            '}';
    }

    @Schema(type = "string", example = "filter,condition")
    public static class FilterPair {

        private String filter;

        private String condition;

        public FilterPair(String filter, String condition) {
            this.filter = filter;
            this.condition = condition;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        @JsonValue
        @Override
        public String toString() {
            return filter + "," + condition;
        }
    }
}
