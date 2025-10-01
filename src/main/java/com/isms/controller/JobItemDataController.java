package com.isms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isms.dto.BillingsDTO;
import com.isms.model.ServiceResponse;
import com.isms.service.JobItemService;
import com.isms.service.PaymentJobItemService;

@RestController
public class JobItemDataController {

//	@Autowired
//	private ItemsService itemsService;

	@Autowired
	private PaymentJobItemService paymentJobItemService;
	
	

	@GetMapping("/getJobItemById/")
	public ResponseEntity<Object> getJobItemById(@RequestParam("id") int id) {	
		
		System.out.println(id);
		
		ServiceResponse<BillingsDTO> response = new ServiceResponse<BillingsDTO>("success", paymentJobItemService.findPaymentJobItem(id));
		System.out.println(response);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
//	@GetMapping("/getItemById/")
//	public ResponseEntity<Object> getItemById(@RequestParam("id") int id) {	
//		
//		System.out.println(id);
//		
//		ServiceResponse<Items> response = new ServiceResponse<Items>("success", itemsService.getItem(id));
//		System.out.println(response);
//		return new ResponseEntity<Object>(response, HttpStatus.OK);
//	}
}

