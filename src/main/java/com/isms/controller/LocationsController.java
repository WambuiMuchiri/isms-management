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
import com.isms.dto.LocationsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Locations;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.LocationsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/locations")
public class LocationsController {

	@Autowired
	private LocationsService locationsService;
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
        return "locations/index";
    }

    @GetMapping("/create")	
    public String create(Model model) {
        model.addAttribute("locationsdto", new LocationsDTO());
        return "locations/create";
    }

      
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("locationsdto") LocationsDTO locationsdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Locations location = new Locations();
            location.setLocationName(locationsdto.getLocationName());
            location.setLocationCode(locationsdto.getLocationCode());
            
            location.setAuditIdentifierId(auditIdentifier);
            locationsService.saveLocation(location);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Location " + location.getLocationName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
				 
			 
            redirectAttributes.addFlashAttribute("success", "Location saved successfully!!");
            return "redirect:/locations/index";
        } else {
            return "locations/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Locations location = locationsService.getLocation(id);
        LocationsDTO locationdto = new LocationsDTO(location.getId(), location.getLocationName(), location.getLocationCode());
        model.addAttribute("locationsdto", locationdto);
        return "locations/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("locationsdto") LocationsDTO locationdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            Locations location = locationsService.getLocation(locationdto.getId());
            location.setLocationName(locationdto.getLocationName());
            location.setLocationCode(locationdto.getLocationCode());   		 	
            locationsService.saveLocation(location);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Location " + location.getLocationName() + " updated.", requestService.getClientIp(request), userAgent, location.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);
			 
            redirectAttributes.addFlashAttribute("success", "Location updated successfully!!");
            return "redirect:/locations/index";
        } else {
            return "locations/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Locations location = locationsService.getLocation(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Location " + location.getLocationName() + " has been deleted.", requestService.getClientIp(request), userAgent, location.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.locationsService.deleteLocation(id);
        redirectAttributes.addFlashAttribute("warning", "Location has been deleted successfully!!");
        return "redirect:/locations/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Locations> locations = locationsService.getLocationsForDatatable(queryString, pageRequest);
        return this.getJsonData(locations, draw);
    }
    
    
    private String getJsonData(Page<Locations> locations, int draw) {
        long totalRecords = locations.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        locations.forEach(location -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", location.getId());
            cellData.put("locationName", location.getLocationName());
            cellData.put("location_code", location.getLocationCode());
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