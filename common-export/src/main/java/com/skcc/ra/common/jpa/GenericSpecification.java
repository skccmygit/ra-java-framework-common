package com.skcc.ra.common.jpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    private List<SearchCriteria> list;

    public GenericSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            if (criteria.getValue() != null) {
                if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                    if (criteria.getValue() instanceof LocalDateTime) {
                        predicates.add(builder.greaterThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalDate) {
                        predicates.add(builder.greaterThan(root.get(criteria.getKey()), (LocalDate) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalTime) {
                        predicates.add(builder.greaterThan(root.get(criteria.getKey()), (LocalTime) criteria.getValue()));
                    } else {
                        predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    }
                } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                    if (criteria.getValue() instanceof LocalDateTime) {
                        predicates.add(builder.lessThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalDate) {
                        predicates.add(builder.lessThan(root.get(criteria.getKey()), (LocalDate) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalTime) {
                        predicates.add(builder.lessThan(root.get(criteria.getKey()), (LocalTime) criteria.getValue()));
                    } else {
                        predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
                    }
                } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                    if (criteria.getValue() instanceof LocalDateTime) {
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalDate) {
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDate) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalTime) {
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalTime) criteria.getValue()));
                    } else {
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    }
                } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                    if (criteria.getValue() instanceof LocalDateTime) {
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalDate) {
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDate) criteria.getValue()));
                    } else if (criteria.getValue() instanceof LocalTime) {
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalTime) criteria.getValue()));
                    } else {
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
                    }
                } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                } else if (criteria.getOperation().equals(SearchOperation.NOT_MATCH)) {
                    predicates.add(builder.notLike(builder.lower(root.get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                }
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
