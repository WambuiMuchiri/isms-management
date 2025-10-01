package com.isms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.helper.DataForDatatableHelper;
import com.isms.model.PaymentJobItem;
import com.isms.service.PaymentJobItemService;

@Controller
@RequestMapping("/paymentJobItem")
public class PaymentJobItemController {

	@Autowired
	private PaymentJobItemService paymentJobItemService;
	
	@GetMapping("/refreshRecords")
	public String truncatePaymentJobItemTables() {
		this.paymentJobItemService.refreshPaymentJobItem();
		return "redirect:/PaymentJobItem/list";
	}
		
	@GetMapping("/list")
	public String getAllPaymentJobItem(Model model) {
		List<PaymentJobItem> paymentJobItem = paymentJobItemService.listPaymentJobItem();
		List<PaymentJobItem> paymentJobItem_Response = paymentJobItemService.listPaymentJobItem_Response();
		model.addAttribute("paymentJobItemList", paymentJobItem);
		model.addAttribute("paymentJobItemList_Response", paymentJobItem_Response);
		return "payment_jobItem/payment_jobItem_list";
	}
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
		this.paymentJobItemService.refreshPaymentJobItem();	
        return "paymentJobItem/index";
    }
	
	
    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<PaymentJobItem> paymentJobItem = paymentJobItemService.getPaymentJobItemForDatatable(queryString, pageRequest);
//        Page<PaymentJobItem> paymentJobItem_Response = paymentJobItemService.getPaymentJobItem_ResponseForDatatable(queryString, pageRequest);
        return this.getJsonData(paymentJobItem, draw);
//        return this.getJsonData(paymentJobItem_Response, draw);
    }
    
    
    private String getJsonData(Page<PaymentJobItem> paymentJobItems, int draw) {
        long totalRecords = paymentJobItems.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        paymentJobItems.forEach(paymentJobItem -> {
            Map<String, Object> cellData = new HashMap<>();
           
            cellData.put("id", paymentJobItem.getId());
            cellData.put("recordDate", paymentJobItem.getRecordDate()); 
            cellData.put("clientName", paymentJobItem.getClientId());
            cellData.put("personnelName", paymentJobItem.getPersonnelId());
            cellData.put("paymentId", paymentJobItem.getPaymentId());
            cellData.put("jobItemId", paymentJobItem.getJobItemId());
            cellData.put("cashInId", paymentJobItem.getCashInId());
            cellData.put("cashOutId", paymentJobItem.getCashOutId());
            cellData.put("amount", paymentJobItem.getAmount());
            cells.add(cellData);
        });

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("draw", draw);
        jsonMap.put("recordsTotal", totalRecords);
        jsonMap.put("recordsFiltered", totalRecords);
        jsonMap.put("data", cells);
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
        }
        return json;
    }
}

