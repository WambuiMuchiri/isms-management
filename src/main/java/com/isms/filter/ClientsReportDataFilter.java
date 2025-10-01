package com.isms.filter;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.isms.model.ClientList_Report;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ClientsReportDataFilter implements Specification<ClientList_Report> {

    String userQuery;

	public ClientsReportDataFilter(String queryString) {
		this.userQuery = queryString;
	}

	@Override
	public Predicate toPredicate(Root<ClientList_Report> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		ArrayList<Predicate> predicates = new ArrayList<>();
		if (userQuery != null && userQuery != "") {
//            predicates.add(criteriaBuilder.like(root.get("billingDate"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("clientName"), '%' + userQuery + '%'));
        }
        return (!predicates.isEmpty() ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }

}
