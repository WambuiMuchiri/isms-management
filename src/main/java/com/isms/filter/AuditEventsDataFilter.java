package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.AuditEvents;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AuditEventsDataFilter implements Specification<AuditEvents> {

    String userQuery;

    public AuditEventsDataFilter(String queryString) {
        this.userQuery = queryString;
    }

    @Override
    public Predicate toPredicate(Root<AuditEvents> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (userQuery != null && userQuery != "") {
           // predicates.add(criteriaBuilder.like(root.get("eventDate"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("description"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("ipAddress"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("browser"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("userId").get("email"), '%' + userQuery + '%'));
           // predicates.add(criteriaBuilder.like(root.get("auditTypeId"), '%' + userQuery + '%'));
        }
        return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }

}
