package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Personnels;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PersonnelsDataFilter implements Specification<Personnels> {

	String userQuery;
	
	public PersonnelsDataFilter(String queryString) {
		this.userQuery=queryString;
	}
	
	
	@Override
	public Predicate toPredicate(Root<Personnels> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("personnelName"), '%'+userQuery+'%'));
			predicates.add(criteriaBuilder.like(root.get("telNo"), '%'+userQuery+'%'));
			predicates.add(criteriaBuilder.like(root.get("gender"), '%'+userQuery+'%'));
		}
		return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
	}

	

}
