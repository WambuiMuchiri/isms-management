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
import com.isms.dto.SitesDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Sites;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.SitesService;
import com.isms.service.ProjectsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/sites")
public class SitesController {

	@Autowired
	private SitesService sitesService;
	@Autowired
	private ProjectsService projectsService;
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
        return "sites/index";
    }

    @GetMapping("/create")	
    public String create(Model model) {
        model.addAttribute("sitesdto", new SitesDTO());
        model.addAttribute("listProjects", projectsService.getAllProjects());
        return "sites/create";
    }

      
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("sitesdto") SitesDTO sitesdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Sites site = new Sites();
            site.setSiteName(sitesdto.getSiteName());
            site.setDescription(sitesdto.getDescription());
            site.setRemarks(sitesdto.getRemarks());
            site.setProjects(sitesdto.getProjects());
            
            site.setAuditIdentifierId(auditIdentifier);
            sitesService.saveSite(site);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Site " + site.getSiteName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
				 
			 
            redirectAttributes.addFlashAttribute("success", "Site saved successfully!!");
            return "redirect:/sites/index";
        } else {
            return "sites/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Sites site = sitesService.getSite(id);
        SitesDTO sitedto = new SitesDTO(site.getId(), site.getSiteName(), site.getDescription(), site.getRemarks(), site.getProjects());
        model.addAttribute("sitesdto", sitedto);
        model.addAttribute("listProjects", projectsService.getAllProjects());
        return "sites/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("sitesdto") SitesDTO sitedto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            Sites site = sitesService.getSite(sitedto.getId());
            site.setSiteName(sitedto.getSiteName());
            site.setDescription(sitedto.getDescription());
            site.setRemarks(sitedto.getRemarks());  
            site.setProjects(sitedto.getProjects());
            sitesService.saveSite(site);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Site " + site.getSiteName() + " updated.", requestService.getClientIp(request), userAgent, site.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
			 
            redirectAttributes.addFlashAttribute("success", "Site updated successfully!!");
            return "redirect:/sites/index";
        } else {
            return "sites/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Sites site = sitesService.getSite(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Site " + site.getSiteName() + " has been deleted.", requestService.getClientIp(request), userAgent, site.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.sitesService.deleteSite(id);
        redirectAttributes.addFlashAttribute("warning", "Site has been deleted successfully!!");
        return "redirect:/sites/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Sites> sites = sitesService.getSitesForDatatable(queryString, pageRequest);
        return this.getJsonData(sites, draw);
    }
    
    
    private String getJsonData(Page<Sites> sites, int draw) {
        long totalRecords = sites.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        sites.forEach(site -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", site.getId());
            cellData.put("siteName", site.getSiteName());
            cellData.put("projects", site.getProjects().getProjectName());
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