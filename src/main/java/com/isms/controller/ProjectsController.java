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
import com.isms.dto.ProjectsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Projects;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.ProjectsService;
import com.isms.service.ClientsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/projects")
public class ProjectsController {

	@Autowired
	private ProjectsService projectsService;
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
	
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
        return "projects/index";
    }

    @GetMapping("/create")	
    public String create(Model model) {
        model.addAttribute("projectsdto", new ProjectsDTO());
        model.addAttribute("listClients", clientsService.getAllClients());
        return "projects/create";
    }

      
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("projectsdto") ProjectsDTO projectsdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Projects project = new Projects();
            project.setProjectName(projectsdto.getProjectName());
            project.setProjectCost(projectsdto.getProjectCost());
            project.setDescription(projectsdto.getDescription());
            project.setRemarks(projectsdto.getRemarks());
            project.setClients(projectsdto.getClients());
            
            project.setAuditIdentifierId(auditIdentifier);
            projectsService.saveProject(project);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Project " + project.getProjectName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
				 
			 
            redirectAttributes.addFlashAttribute("success", "Project saved successfully!!");
            return "redirect:/projects/index";
        } else {
            return "projects/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Projects project = projectsService.getProject(id);
        ProjectsDTO projectdto = new ProjectsDTO(project.getId(), project.getProjectName(), project.getProjectCost(), project.getDescription(), project.getRemarks(), project.getClients());
        model.addAttribute("projectsdto", projectdto);
        model.addAttribute("listClients", clientsService.getAllClients());
        return "projects/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("projectsdto") ProjectsDTO projectdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            Projects project = projectsService.getProject(projectdto.getId());
            project.setProjectName(projectdto.getProjectName());
            project.setProjectCost(projectdto.getProjectCost());
            project.setDescription(projectdto.getDescription());
            project.setRemarks(projectdto.getRemarks());  
            project.setClients(projectdto.getClients());
            projectsService.saveProject(project);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Project " + project.getProjectName() + " updated.", requestService.getClientIp(request), userAgent, project.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
			 
            redirectAttributes.addFlashAttribute("success", "Project updated successfully!!");
            return "redirect:/projects/index";
        } else {
            return "projects/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Projects project = projectsService.getProject(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Project " + project.getProjectName() + " has been deleted.", requestService.getClientIp(request), userAgent, project.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.projectsService.deleteProject(id);
        redirectAttributes.addFlashAttribute("warning", "Project has been deleted successfully!!");
        return "redirect:/projects/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Projects> projects = projectsService.getProjectsForDatatable(queryString, pageRequest);
        return this.getJsonData(projects, draw);
    }
    
    
    private String getJsonData(Page<Projects> projects, int draw) {
        long totalRecords = projects.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        projects.forEach(project -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", project.getId());
            cellData.put("projectName", project.getProjectName());
            cellData.put("projectCost", project.getProjectCost());
            cellData.put("clients", project.getClients().getClientName());
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