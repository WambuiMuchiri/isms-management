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
import com.isms.dto.ClientsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Clients;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.ClientsService;
import com.isms.service.LocationsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientsController {

	@Autowired
	private ClientsService clientsService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private LocationsService locationsService;
	
	 @GetMapping(value = {"/index", "/", ""})
	    public String index(Model model) {
	        return "clients/index";
	    }

	    @GetMapping("/create")
	    public String create(Model model) {
	        model.addAttribute("clientsdto", new ClientsDTO());
	        	model.addAttribute("locationList", locationsService.getAllLocations());
	        return "clients/create";
	    }

	    @PostMapping("/create")
	    public String create(@Valid @ModelAttribute("clientsdto") ClientsDTO clientsdto,
	            BindingResult bindingResult,
	            RedirectAttributes redirectAttributes,
	            @RequestHeader(value = "User-Agent") String userAgent,
	            HttpServletRequest request,
	            Model model) {
	        if (!bindingResult.hasErrors()) {
	            AuditIdentifier auditIdentifier = new AuditIdentifier();
	            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

	            Clients client = new Clients();
	            client.setClientName(clientsdto.getClientName());
	            client.setTelNo(clientsdto.getTelNo());
	            client.setEmailAddress(clientsdto.getEmailAddress());
	            client.setLocations(clientsdto.getLocations());

	            client.setAuditIdentifierId(auditIdentifier);
	            clientsService.saveClient(client);

	            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Client " + client.getClientName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
	            auditEventsService.saveAuditEvents(saveEventsHelper);

	            redirectAttributes.addFlashAttribute("success", "Client saved successfully!!");
	            return "redirect:/clients/index";
	        } else {
	            return "clients/create";
	        }
	    }
	    
	    @GetMapping("/update/{id}")
	    public String update(@PathVariable(value = "id") int id, Model model) {
	        Clients client = clientsService.getClient(id);
	        ClientsDTO clientdto = new ClientsDTO(client.getId(), client.getClientName(), client.getEmailAddress(), client.getTelNo(), client.getLocations());
	        model.addAttribute("clientdto", clientdto);
	        	model.addAttribute("locationList", locationsService.getAllLocations());
	        return "clients/update";
	    }


	    @PostMapping("/update/{id}")
	    public String update(@PathVariable(value = "id") long id, @Valid @ModelAttribute("client") ClientsDTO clientdto,
	            BindingResult bindingResult,
	            RedirectAttributes redirectAttributes,
	            @RequestHeader(value = "User-Agent") String userAgent,
	            HttpServletRequest request,
	            Model model) {
	        if (!bindingResult.hasErrors()) {
	            Clients client = clientsService.getClient(clientdto.getId());
	            client.setClientName(clientdto.getClientName());
	            client.setTelNo(clientdto.getTelNo());
	            client.setEmailAddress(clientdto.getEmailAddress());
	            client.setLocations(clientdto.getLocations());
	            
	            clientsService.saveClient(client);

	            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Client " + client.getClientName() + " updated.", requestService.getClientIp(request), userAgent, client.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
	            auditEventsService.saveAuditEvents(saveEventsHelper);

	            redirectAttributes.addFlashAttribute("success", "Client updated successfully!!");
	            return "redirect:/clients/index";
	        } else {
	            return "clients/update";
	        }
	    }
	    
	    @PostMapping("/delete/{id}")
	    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
	        Clients client = clientsService.getClient(id);

	        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Client " + client.getClientName() + " has been deleted.", requestService.getClientIp(request), userAgent, client.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
	        auditEventsService.saveAuditEvents(saveEventsHelper);

	        this.clientsService.deleteClient(id);
	        redirectAttributes.addFlashAttribute("warning", "Client has been deleted successfully!!");
	        return "redirect:/clients/index";
	    }

	    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
	    @ResponseBody
	    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
	        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
	        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
	        Pageable pageRequest = dataForDatatable.getPageable();
	        String queryString = (String) (params.get("search[value]"));
	        Page<Clients> clients = clientsService.getClientsForDatatable(queryString, pageRequest);
	        return this.getJsonData(clients, draw);
	    }
	    
	    
	    private String getJsonData(Page<Clients> clients, int draw) {
	        long totalRecords = clients.getTotalElements();
	        List<Map<String, Object>> cells = new ArrayList<>();
	        clients.forEach(client -> {
	            Map<String, Object> cellData = new HashMap<>();
	            cellData.put("id", client.getId());
	            cellData.put("clientName", client.getClientName());
	            cellData.put("telNo", client.getTelNo());
	            cellData.put("locations", client.getLocations().getLocationName());
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
