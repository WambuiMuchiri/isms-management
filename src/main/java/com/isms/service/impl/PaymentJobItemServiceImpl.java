package com.isms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isms.dto.BillingsDTO;
import com.isms.filter.ClientsReportDataFilter;
import com.isms.filter.PaymentJobItemDataFilter;
import com.isms.model.ClientList_Report;
import com.isms.model.PaymentJobItem;
import com.isms.repository.PaymentJobItemRepository;
import com.isms.service.PaymentJobItemService;

@Service
public class PaymentJobItemServiceImpl implements PaymentJobItemService{

	@Autowired
	private PaymentJobItemRepository paymentJobItemRepository;

	@Transactional
	@Override
	public void refreshPaymentJobItem() {
		paymentJobItemRepository.truncatePaymentJobItem();		
		paymentJobItemRepository.fillPaymentJobItem_Payments();		
		paymentJobItemRepository.fillPaymentJobItem_JobItem();
	}

	@Override
	public List<PaymentJobItem> listPaymentJobItem() {
		return paymentJobItemRepository.findAll();
	}

	
	@Override
	public BillingsDTO findPaymentJobItem(int id) {
		return paymentJobItemRepository.findPaymentJobItem(id);
	}

	@Override
	public List<PaymentJobItem> listPaymentJobItem_Response() {
		return paymentJobItemRepository.getAllPaymentJobItem_Response();
	}
	

	@Override
	public Page<PaymentJobItem> getPaymentJobItemForDatatable(String queryString, Pageable pageable) {
		PaymentJobItemDataFilter paymentJobItemDataFilter = new PaymentJobItemDataFilter(queryString);
		return paymentJobItemRepository.findAll(paymentJobItemDataFilter, pageable);
	}

	@Override
	public Page<ClientList_Report> getClientsListForDatatable(String queryString, Pageable pageable) {
		ClientsReportDataFilter clientsReportDataFilter = new ClientsReportDataFilter(queryString);
		return paymentJobItemRepository.getClientsReportForDatatable(clientsReportDataFilter, pageable);
	}

}
