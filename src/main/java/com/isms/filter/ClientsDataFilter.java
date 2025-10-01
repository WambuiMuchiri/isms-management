package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Clients;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ClientsDataFilter implements Specification<Clients> {

	String userQuery;
	
	public ClientsDataFilter(String queryString) {
		this.userQuery=queryString;
	}
	
	
	@Override
	public Predicate toPredicate(Root<Clients> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("clientName"), '%' +userQuery+ '%'));
			predicates.add(criteriaBuilder.like(root.get("locations").get("locationName"), '%' +userQuery+ '%'));
		}
		return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
	}

	

}
