package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Categories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CategoriesDataFilter implements Specification<Categories> {

	String userQuery;
	
	public CategoriesDataFilter(String queryString) {
		this.userQuery = queryString;
	}
	
	@Override
	public Predicate toPredicate(Root<Categories> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		if(userQuery!=null && userQuery!="") {
			predicates.add(criteriaBuilder.like(root.get("initials"), '%'+userQuery+'%'));
			predicates.add(criteriaBuilder.like(root.get("name"), '%'+userQuery+'%'));
		}
		return(!predicates.isEmpty()?criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])):null);
	}

	

}
