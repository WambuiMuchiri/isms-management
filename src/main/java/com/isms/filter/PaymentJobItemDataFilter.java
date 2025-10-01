package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.PaymentJobItem;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PaymentJobItemDataFilter implements Specification<PaymentJobItem> {

    String userQuery;

	public PaymentJobItemDataFilter(String queryString) {
		this.userQuery = queryString;
	}

	@Override
	public Predicate toPredicate(Root<PaymentJobItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		if (userQuery != null && userQuery != "") {
            predicates.add(criteriaBuilder.like(root.get("clientId").get("clientName"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("paymentId"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("jobItemId"), '%' + userQuery + '%'));
        }
        return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }

}

