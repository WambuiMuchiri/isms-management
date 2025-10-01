package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Installations;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class InstallationsDataFilter implements Specification<Installations> {

	String userQuery;
	
	public InstallationsDataFilter(String queryString) {
		this.userQuery=queryString;
	}
	
	
	@Override
	public Predicate toPredicate(Root<Installations> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("installationName"), '%' +userQuery+ '%'));
			predicates.add(criteriaBuilder.like(root.get("sites").get("siteName"), '%' +userQuery+ '%'));
		}
		return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
	}

	

}
