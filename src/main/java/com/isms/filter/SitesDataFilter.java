package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Sites;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SitesDataFilter implements Specification<Sites> {

	String userQuery;
	
	public SitesDataFilter(String queryString) {
		this.userQuery=queryString;
	}
	
	
	@Override
	public Predicate toPredicate(Root<Sites> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("siteName"), '%' +userQuery+ '%'));
			predicates.add(criteriaBuilder.like(root.get("projects").get("projectName"), '%' +userQuery+ '%'));
		}
		return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
	}

	

}
