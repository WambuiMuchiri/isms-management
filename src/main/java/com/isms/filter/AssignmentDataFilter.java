package com.isms.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Assignment;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AssignmentDataFilter implements Specification<Assignment> {

    private static final long serialVersionUID = 1L;
    private String searchString;

    public AssignmentDataFilter(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public Predicate toPredicate(Root<Assignment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchString != null && !searchString.isEmpty()) {
            // Search in related installation name
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("installation").get("installationName")), 
                "%" + searchString.toLowerCase() + "%"
            ));
            
            // Search in related personnel name
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("personnel").get("personnelName")), 
                "%" + searchString.toLowerCase() + "%"
            ));
            
            // Search in remarks
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("remarks")), 
                "%" + searchString.toLowerCase() + "%"
            ));
        }

        return predicates.isEmpty() 
            ? criteriaBuilder.conjunction() 
            : criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}