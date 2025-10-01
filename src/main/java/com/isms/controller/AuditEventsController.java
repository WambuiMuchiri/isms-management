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
import com.isms.model.AuditEvents;
import com.isms.service.AuditEventsService;
import com.isms.service.RequestService;

@Controller
@RequestMapping("/auditEvents")
public class AuditEventsController {
	
	@Autowired
	private AuditEventsService auditEventsService;	
	@Autowired
	private RequestService requestService;
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
        return "auditTrail/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<AuditEvents> auditEvents = auditEventsService.getAuditEventsForDatatable(queryString, pageRequest);
        return this.getJsonData(auditEvents, draw);
    }
    
    
    private String getJsonData(Page<AuditEvents> auditEvents, int draw) {
        long totalRecords = auditEvents.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        auditEvents.forEach(auditEvent -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("eventDate", auditEvent.getEventDate());
            cellData.put("description", auditEvent.getDescription());
            cellData.put("userId", auditEvent.getUserId().getEmail());
            cellData.put("type", auditEvent.getAuditTypeId().getName());
            cellData.put("browser", auditEvent.getBrowser());
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