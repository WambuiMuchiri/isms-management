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
import com.isms.dto.CashOutDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.CashOut;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.CashOutService;
import com.isms.service.ClientsService;
import com.isms.service.PaymentJobItemService;
import com.isms.service.PersonnelsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cashOut")
public class CashOutController {
	
	@Autowired
	private CashOutService cashOutService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private PersonnelsService personnelPersonnelsService;
	@Autowired
	private ClientsService clientsService;
	@Autowired
	private PaymentJobItemService paymentJobItemService;

	
	
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
		this.paymentJobItemService.refreshPaymentJobItem();
        return "cashOut/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("cashOutdto", new CashOutDTO());
	        model.addAttribute("listClients", clientsService.getAllClients());
	        model.addAttribute("listPersonnels", personnelPersonnelsService.getAllPersonnels());
        return "cashOut/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("cashOutdto") CashOutDTO cashOutdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            CashOut cashOut = new CashOut();
            cashOut.setRecordDate(cashOutdto.getRecordDate());
            cashOut.setAmount(cashOutdto.getAmount());
            cashOut.setRemarks(cashOutdto.getRemarks());
            cashOut.setClients(cashOutdto.getClients());
            cashOut.setPersonnels(cashOutdto.getPersonnels());

            cashOut.setAuditIdentifierId(auditIdentifier);
            cashOutService.saveCashOut(cashOut);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New CashOut " + cashOut.getAmount() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "CashOut saved successfully!!");
            return "redirect:/cashOut/index";
        } else {
            return "cashOut/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        CashOut cashOut = cashOutService.getCashOut(id);
        CashOutDTO cashOutdto = new CashOutDTO(cashOut.getId(), cashOut.getRecordDate(), cashOut.getAmount(), cashOut.getRemarks(), cashOut.getClients(), cashOut.getPersonnels());
        model.addAttribute("cashOutdto", cashOutdto);
	        model.addAttribute("listClients", clientsService.getAllClients());
	        model.addAttribute("listPersonnels", personnelPersonnelsService.getAllPersonnels());
        return "cashOut/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("cashOutdto") CashOutDTO cashOutdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            CashOut cashOut = cashOutService.getCashOut(cashOutdto.getId());
            cashOut.setRecordDate(cashOutdto.getRecordDate());
            cashOut.setAmount(cashOutdto.getAmount());
            cashOut.setRemarks(cashOutdto.getRemarks());
            cashOut.setClients(cashOutdto.getClients());
            cashOut.setPersonnels(cashOutdto.getPersonnels());
            
            cashOutService.saveCashOut(cashOut);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "CashOut " + cashOut.getAmount() + " updated.", requestService.getClientIp(request), userAgent, cashOut.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "CashOut updated successfully!!");
            return "redirect:/cashOut/index";
        } else {
            return "cashOut/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        CashOut cashOut = cashOutService.getCashOut(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "CashOut " + cashOut.getAmount() + " has been deleted.", requestService.getClientIp(request), userAgent, cashOut.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.cashOutService.deleteCashOut(id);
        redirectAttributes.addFlashAttribute("warning", "CashOut has been deleted successfully!!");
        return "redirect:/cashOut/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<CashOut> cashOut = cashOutService.getCashOutForDatatable(queryString, pageRequest);
        return this.getJsonData(cashOut, draw);
    }
    
    
    private String getJsonData(Page<CashOut> cashOut, int draw) {
        long totalRecords = cashOut.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        cashOut.forEach(cashOuts -> {
            Map<String, Object> cellData = new HashMap<>();
            
            String personnelName  = (cashOuts.getPersonnels()  != null && cashOuts.getPersonnels().getPersonnelName()  != null)
                    ? cashOuts.getPersonnels().getPersonnelName()  : "N/A";
            String clientName = (cashOuts.getClients() != null && cashOuts.getClients().getClientName() != null)
                    ? cashOuts.getClients().getClientName() : "N/A";

            cellData.put("id", cashOuts.getId());
            cellData.put("date", cashOuts.getRecordDate());
            cellData.put("amount", cashOuts.getAmount());
            cellData.put("personnels", personnelName);
            cellData.put("clients", clientName);
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