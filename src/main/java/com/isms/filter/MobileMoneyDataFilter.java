package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.B2C_C2B_Entries;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class MobileMoneyDataFilter implements Specification<B2C_C2B_Entries> {

	String userQuery;
	
	public MobileMoneyDataFilter(String queryString) {
		this.userQuery = queryString;
	}

	@Override
	public Predicate toPredicate(Root<B2C_C2B_Entries> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.like(root.get("msisdn"), '%'+ userQuery +'%'));
		predicates.add(criteriaBuilder.like(root.get("conversationId"), '%'+ userQuery +'%'));
		predicates.add(criteriaBuilder.like(root.get("amount"), '%'+ userQuery +'%'));
		return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])):null);
	}
}
