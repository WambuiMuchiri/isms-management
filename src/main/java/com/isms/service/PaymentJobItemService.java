package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.dto.BillingsDTO;
import com.isms.model.ClientList_Report;
import com.isms.model.PaymentJobItem;

public interface PaymentJobItemService {

	void refreshPaymentJobItem();
	
	BillingsDTO findPaymentJobItem(int id);
	
	List<PaymentJobItem> listPaymentJobItem();
	
	List<PaymentJobItem> listPaymentJobItem_Response();
	
	Page<PaymentJobItem> getPaymentJobItemForDatatable(String queryString, Pageable pageable);
	
//	Page<PaymentJobItem> getPaymentJobItem_ResponseForDatatable(String queryString, Pageable pageable);

	Page<ClientList_Report> getClientsListForDatatable(String queryString, Pageable pageable);
	
	
}
