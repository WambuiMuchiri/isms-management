package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Payments;

public interface PaymentsService {

	List<Payments> getAllPayments();
	
	Payments savePayment(Payments payment);
	
	Payments getPayment(int id);
	
	void deletePayment(int id);
	
	Page<Payments> getPaymentsForDatatable(String queryString, Pageable pageable);
}
