package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.CashOut;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CashOutDataFilter implements Specification<CashOut> {

	String userQuery;
	
	public CashOutDataFilter(String queryString) {
		this.userQuery = queryString;
	}
	
	@Override
	public Predicate toPredicate(Root<CashOut> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("personnels").get("personnelName"), '%'+userQuery+'%'));
			predicates.add(criteriaBuilder.like(root.get("clients").get("clientName"), '%'+userQuery+'%'));
		}
		return(!predicates.isEmpty()?criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])):null);
	}

	

}
