package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.Users;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class UsersDataFilter implements Specification<Users> {

    String userQuery;

    public UsersDataFilter(String queryString) {
        this.userQuery = queryString;
    }

    @Override
    public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (userQuery != null && userQuery != "") {
            predicates.add(criteriaBuilder.like(root.get("userName"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("status"), '%' + userQuery + '%'));
//            predicates.add(criteriaBuilder.like(root.get("userLogo"), '%' + userQuery + '%'));
        }
        return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }

}
