package com.isms.controller;

import java.io.IOException;
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
import com.isms.dto.RolesDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Roles;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.RolesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RolesController {

	@Autowired
	private RolesService rolesService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
        return "roles/index";
    }

    @GetMapping("/create")	
    public String create(Model model) {
        model.addAttribute("rolesdto", new RolesDTO());
        return "roles/create";
    }

      
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("rolesdto") RolesDTO rolesdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Roles role = new Roles();
            role.setName(rolesdto.getName());
            role.setDescription(rolesdto.getDescription());
            
            role.setAuditIdentifierId(auditIdentifier);
            rolesService.saveRole(role);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Role " + role.getName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
				 
			 
            redirectAttributes.addFlashAttribute("success", "Role saved successfully!!");
            return "redirect:/roles/index";
        } else {
            return "roles/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Roles role = rolesService.getRole(id);
        RolesDTO roledto = new RolesDTO(role.getId(), role.getName(), role.getDescription());
        model.addAttribute("rolesdto", roledto);
        return "roles/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("rolesdto") RolesDTO roledto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            Roles role = rolesService.getRole(roledto.getId());
            role.setName(roledto.getName());
            role.setDescription(roledto.getDescription());   		 	
            rolesService.saveRole(role);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Role " + role.getName() + " updated.", requestService.getClientIp(request), userAgent, role.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
			 
            redirectAttributes.addFlashAttribute("success", "Role updated successfully!!");
            return "redirect:/roles/index";
        } else {
            return "roles/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Roles role = rolesService.getRole(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Role " + role.getName() + " has been deleted.", requestService.getClientIp(request), userAgent, role.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.rolesService.deleteRole(id);
        redirectAttributes.addFlashAttribute("warning", "Role has been deleted successfully!!");
        return "redirect:/roles/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Roles> roles = rolesService.getRolesForDatatable(queryString, pageRequest);
        return this.getJsonData(roles, draw);
    }
    
    
    private String getJsonData(Page<Roles> roles, int draw) {
        long totalRecords = roles.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        roles.forEach(role -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", role.getId());
            cellData.put("name", role.getName());
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