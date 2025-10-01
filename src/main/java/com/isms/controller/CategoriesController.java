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
import com.isms.dto.CategoriesDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Categories;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.CategoriesService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoriesController {
	
	@Autowired
	private CategoriesService categoriesService;
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
        return "categories/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("categoriesdto", new CategoriesDTO());
        return "categories/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoriesdto") CategoriesDTO categoriesdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Categories category = new Categories();
            category.setInitials(categoriesdto.getInitials());
            category.setName(categoriesdto.getName());

            category.setAuditIdentifierId(auditIdentifier);
            categoriesService.saveCategory(category);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Category " + category.getName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "Category saved successfully!!");
            return "redirect:/categories/index";
        } else {
            return "categories/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Categories category = categoriesService.getCategory(id);
        CategoriesDTO categorydto = new CategoriesDTO(category.getId(), category.getInitials(), category.getName());
        model.addAttribute("categorydto", categorydto);
        return "categories/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("category") CategoriesDTO categorydto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            Categories category = categoriesService.getCategory(categorydto.getId());
            category.setInitials(categorydto.getInitials());
            category.setName(categorydto.getName());
            
            categoriesService.saveCategory(category);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Category " + category.getName() + " updated.", requestService.getClientIp(request), userAgent, category.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

            redirectAttributes.addFlashAttribute("success", "Category updated successfully!!");
            return "redirect:/categories/index";
        } else {
            return "categories/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Categories category = categoriesService.getCategory(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Category " + category.getName() + " has been deleted.", requestService.getClientIp(request), userAgent, category.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.categoriesService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("warning", "Category has been deleted successfully!!");
        return "redirect:/categories/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Categories> categories = categoriesService.getCategoriesForDatatable(queryString, pageRequest);
        return this.getJsonData(categories, draw);
    }
    
    
    private String getJsonData(Page<Categories> categories, int draw) {
        long totalRecords = categories.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        categories.forEach(category -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", category.getId());
            cellData.put("initials", category.getInitials());
            cellData.put("name", category.getName());
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