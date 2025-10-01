package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.CashIn;

public interface CashInService {

	List<CashIn> getAllCashIn();
	
	void saveCashIn(CashIn cashIn);
	
	CashIn getCashIn(int id);
	
	void deleteCashIn(int id);
	
	Page<CashIn> getCashInForDatatable(String queryString, Pageable pageable);
}
