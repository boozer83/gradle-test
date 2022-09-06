package io.kakaoi.service.predicate;

import io.kakaoi.domain.ProjectEntity_;
import io.kakaoi.domain.SampleEntity;
import io.kakaoi.domain.SampleEntity_;
import io.kakaoi.web.rest.vm.FilterVM;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;

/**
 * SampleEntity Filtering Jpa Specification Factory 클래스
 */
@Component
public class SampleFilterPredicateFactory extends AbstractFilterPredicateFactory<SampleEntity> {

    public Specification<SampleEntity> makeWithProject(String projectId, FilterVM filterVM) {
        return super.make(filterVM).and(createProjectIdSpec(projectId));
    }

    @Override
    protected Predicate buildFilter(FilterVM.FilterPair filterPair, Root<SampleEntity> root, CriteriaBuilder criteriaBuilder) {
        String searchString = "%" + filterPair.getCondition().toLowerCase() + "%";
        switch (filterPair.getFilter().toUpperCase()) {
            case "NAME":
                return criteriaBuilder.like(root.get(SampleEntity_.name), searchString);
            case "DESCRIPTION":
                return criteriaBuilder.like(root.get(SampleEntity_.description), searchString);
        }
        return null;
    }

    @Override
    protected Collection<Path<String>> selectColumn(Root<SampleEntity> root) {
        ArrayList<Path<String>> columns = new ArrayList<>();
        columns.add(root.get(SampleEntity_.name));
        columns.add(root.get(SampleEntity_.description));
        return columns;
    }

    private Specification<SampleEntity> createProjectIdSpec(String projectId) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(SampleEntity_.project).get(ProjectEntity_.id), projectId);
    }
}
