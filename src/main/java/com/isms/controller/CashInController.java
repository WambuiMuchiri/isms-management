package com.isms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.dto.CashInDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.CashIn;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.CashInService;
import com.isms.service.ClientsService;
import com.isms.service.PaymentJobItemService;
import com.isms.service.PersonnelsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cashIn")
public class CashInController {
	
	@Autowired
	private CashInService cashInService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private PersonnelsService personnelsService;
	@Autowired
	private ClientsService clientsService;
	@Autowired
	private PaymentJobItemService paymentJobItemService;

	
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
		this.paymentJobItemService.refreshPaymentJobItem();
        return "cashIn/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("cashIndto", new CashInDTO());
	        model.addAttribute("listClients", clientsService.getAllClients());
	        model.addAttribute("listPersonnels", personnelsService.getAllPersonnels());
        return "cashIn/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("cashIndto") CashInDTO cashIndto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            CashIn cashIn = new CashIn();
            cashIn.setRecordDate(cashIndto.getRecordDate());
            cashIn.setAmount(cashIndto.getAmount());
            cashIn.setRemarks(cashIndto.getRemarks());
            cashIn.setClients(cashIndto.getClients());
            cashIn.setPersonnels(cashIndto.getPersonnels());

            cashIn.setAuditIdentifierId(auditIdentifier);
            cashInService.saveCashIn(cashIn);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New CashIn " + cashIn.getAmount() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "CashIn saved successfully!!");
            return "redirect:/cashIn/index";
        } else {
            return "cashIn/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        CashIn cashIn = cashInService.getCashIn(id);
        CashInDTO cashIndto = new CashInDTO(cashIn.getId(), cashIn.getRecordDate(), cashIn.getAmount(), cashIn.getRemarks(), cashIn.getClients(), cashIn.getPersonnels());
        model.addAttribute("cashIndto", cashIndto);
	        model.addAttribute("listClients", clientsService.getAllClients());
	        model.addAttribute("listPersonnels", personnelsService.getAllPersonnels());
        return "cashIn/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("cashIndto") CashInDTO cashIndto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            CashIn cashIn = cashInService.getCashIn(cashIndto.getId());
            cashIn.setRecordDate(cashIndto.getRecordDate());
            cashIn.setAmount(cashIndto.getAmount());
            cashIn.setRemarks(cashIndto.getRemarks());
            cashIn.setClients(cashIndto.getClients());
            cashIn.setPersonnels(cashIndto.getPersonnels());
            
            cashInService.saveCashIn(cashIn);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "CashIn " + cashIn.getAmount() + " updated.", requestService.getClientIp(request), userAgent, cashIn.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "CashIn updated successfully!!");
            return "redirect:/cashIn/index";
        } else {
            return "cashIn/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        CashIn cashIn = cashInService.getCashIn(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "CashIn " + cashIn.getAmount() + " has been deleted.", requestService.getClientIp(request), userAgent, cashIn.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.cashInService.deleteCashIn(id);
        redirectAttributes.addFlashAttribute("warning", "CashIn has been deleted successfully!!");
        return "redirect:/cashIn/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<CashIn> cashIn = cashInService.getCashInForDatatable(queryString, pageRequest);
        return this.getJsonData(cashIn, draw);
    }
    
    
    private String getJsonData(Page<CashIn> cashIn, int draw) {
        long totalRecords = cashIn.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        cashIn.forEach(cashIns -> {
            Map<String, Object> cellData = new HashMap<>();
                       
            String personnelName  = (cashIns.getPersonnels()  != null && cashIns.getPersonnels().getPersonnelName()  != null)
                    ? cashIns.getPersonnels().getPersonnelName()  : "N/A";
            String clientName = (cashIns.getClients() != null && cashIns.getClients().getClientName() != null)
                    ? cashIns.getClients().getClientName() : "N/A";

            cellData.put("id", cashIns.getId());
            cellData.put("date", cashIns.getRecordDate());
            cellData.put("amount", cashIns.getAmount());
            cellData.put("personnelName", personnelName);
            cellData.put("clientName", clientName);
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