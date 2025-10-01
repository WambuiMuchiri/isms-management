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
import com.isms.dto.InstallationsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Installations;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.InstallationsService;
import com.isms.service.SitesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/installations")
public class InstallationsController {

	@Autowired
	private InstallationsService installationsService;
	@Autowired
	private SitesService sitesService;
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
        return "installations/index";
    }

    @GetMapping("/create")	
    public String create(Model model) {
        model.addAttribute("installationsdto", new InstallationsDTO());
        model.addAttribute("listSites", sitesService.getAllSites());
        return "installations/create";
    }

      
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("installationsdto") InstallationsDTO installationsdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Installations installation = new Installations();
            installation.setInstallationName(installationsdto.getInstallationName());
            installation.setDescription(installationsdto.getDescription());
            installation.setRemarks(installationsdto.getRemarks());
            installation.setSites(installationsdto.getSites());
            
            installation.setAuditIdentifierId(auditIdentifier);
            installationsService.saveInstallation(installation);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Installation " + installation.getInstallationName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
				 
			 
            redirectAttributes.addFlashAttribute("success", "Installation saved successfully!!");
            return "redirect:/installations/index";
        } else {
            return "installations/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Installations installation = installationsService.getInstallation(id);
        InstallationsDTO installationdto = new InstallationsDTO(installation.getId(), installation.getInstallationName(), installation.getDescription(), installation.getRemarks(), installation.getSites());
        model.addAttribute("installationsdto", installationdto);
        model.addAttribute("listSites", sitesService.getAllSites());
        return "installations/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("installationsdto") InstallationsDTO installationdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            Installations installation = installationsService.getInstallation(installationdto.getId());
            installation.setInstallationName(installationdto.getInstallationName());
            installation.setDescription(installationdto.getDescription());
            installation.setRemarks(installationdto.getRemarks());  
            installation.setSites(installationdto.getSites());
            installationsService.saveInstallation(installation);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Installation " + installation.getInstallationName() + " updated.", requestService.getClientIp(request), userAgent, installation.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
			 
            redirectAttributes.addFlashAttribute("success", "Installation updated successfully!!");
            return "redirect:/installations/index";
        } else {
            return "installations/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Installations installation = installationsService.getInstallation(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Installation " + installation.getInstallationName() + " has been deleted.", requestService.getClientIp(request), userAgent, installation.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.installationsService.deleteInstallation(id);
        redirectAttributes.addFlashAttribute("warning", "Installation has been deleted successfully!!");
        return "redirect:/installations/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Installations> installations = installationsService.getInstallationsForDatatable(queryString, pageRequest);
        return this.getJsonData(installations, draw);
    }
    
    
    private String getJsonData(Page<Installations> installations, int draw) {
        long totalRecords = installations.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        installations.forEach(installation -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", installation.getId());
            cellData.put("installationName", installation.getInstallationName());
            cellData.put("sites", installation.getSites().getSiteName());
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