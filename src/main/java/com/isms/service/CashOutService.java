package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.CashOut;

public interface CashOutService {

	List<CashOut> getAllCashOut();
	
	void saveCashOut(CashOut cashOut);
	
	CashOut getCashOut(int id);
	
	void deleteCashOut(int id);
	
	Page<CashOut> getCashOutForDatatable(String queryString, Pageable pageable);
}
