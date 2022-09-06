package io.kakaoi.service.predicate;

import io.kakaoi.web.rest.vm.FilterVM;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

/**
 * Filter 구현 시 Jpa Specification 을 이용한 팩토리 추상 클래스
 * @param <E> Filtering 할 Entity 클래스
 */
public abstract class AbstractFilterPredicateFactory<E> {

    /**
     * 필터의 조건을 지정할 메소드
     *
     * 예제는 다음과 같다.
     * <pre>
     *     String filterString = "%" + filterPair.getCondition() + "%";
     *     switch (filterPair.getField()) {
     *         case "NAME":
     *             return criteriaBuilder.like(root.get(CredentialEntity_.name), filterString);
     *         case "DESCRIPTION":
     *             return criteriaBuilder.like(root.get(CredentialEntity_.description), filterString);
     *         case "TYPE":
     *             return criteriaBuilder.equal(root.get(CredentialEntity_.code).get(CodeEntity_.code), filterPair.getCondition());
     *     }
     *     return null;
     * </pre>
     */
    protected abstract Predicate buildFilter(FilterVM.FilterPair filterPair, Root<E> root, CriteriaBuilder criteriaBuilder);

    /**
     * 키워드 컬럼을 선택할 선택자 메소드
     *
     * 예제는 다음과 같다.
     * <pre>
     *     List&lt;Path&lt;String>> columns = new ArrayList<>();
     *     columns.add(root.get(CredentialEntity_.name));
     *     columns.add(root.get(CredentialEntity_.description));
     *     columns.add(root.get(CredentialEntity_.code).get(CodeEntity_.description));
     *     return columns;
     * </pre>
     */
    protected abstract Collection<Path<String>> selectColumn(Root<E> root);

    /**
     * FilterVM 과 필터 조건을 만드는 Builder, 키워드 컬럼 선택자를 받아 필터 Specificaton 을 만든다.
     * @param filterVM Filter 조건이 담긴 객체
     * @return 완성한 Specification
     */
    public Specification<E> make(FilterVM filterVM) {
        return Specification.where(makeFilterSpec(filterVM.getFilter()))
                            .and(makeKeywordSpec(filterVM.getKeyword()));
    }

    /**
     * 키워드들은 선택한 칼럼에서 like 로 찾고 결과를 or로 연산한다.
     * @param keywords 요청한 키워드 리스트
     * @return 완성한 Keyword Specification
     */
    private Specification<E> makeKeywordSpec(List<String> keywords) {
        return keywords.stream()
            .map(keyword -> (Specification<E>) (root, query, criteriaBuilder) -> {
                Predicate[] predicates =
                    this.selectColumn(root).stream()
                        .map(column -> criteriaBuilder.like(column, "%" + keyword.toLowerCase() + "%"))
                        .toArray(Predicate[]::new);
                return criteriaBuilder.or(predicates);
            })
            .reduce(Specification.where(null), Specification::or);
    }

    /**
     * 필터 목록과 필터 연산에 대한 builder 를 통해 Filter Specification 을 만들고 결과를 and 로 연산한다.
     * @param filterPairs 필터의 field 와 condition 의 페어 목록
     * @return 완성한 Filter Specification
     */
    private Specification<E> makeFilterSpec(List<FilterVM.FilterPair> filterPairs) {
        return filterPairs.stream()
            .map(filterPair ->
                (Specification<E>) (root, query, criteriaBuilder) ->
                    this.buildFilter(filterPair, root, criteriaBuilder))
            .reduce(Specification.where(null), Specification::and);
    }

}
