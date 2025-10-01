package com.isms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.util.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.dto.EquipmentsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Equipments;
import com.isms.model.Personnels;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.CategoriesService;
import com.isms.service.EquipmentsService;
import com.isms.service.LocationsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/equipments")
public class EquipmentsController {
	
	@Autowired
	private EquipmentsService equipmentsService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private CategoriesService categoriesService;
	@Autowired
	private LocationsService locationsService;
	
	@GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
        return "equipments/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("equipmentsdto", new EquipmentsDTO());
        model.addAttribute("listCategories", categoriesService.getAllCategories());
        model.addAttribute("listLocations", locationsService.getAllLocations());
        return "equipments/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("equipmentsdto") EquipmentsDTO equipmentsdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("equipmentPicture") MultipartFile multipartFile) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Equipments equipment = new Equipments();
            equipment.setRecordDate(equipmentsdto.getRecordDate());
            equipment.setEquipmentName(equipmentsdto.getEquipmentName());
            equipment.setSerialNumber(equipmentsdto.getSerialNumber());
            equipment.setStatus(equipmentsdto.getStatus());
            equipment.setEquipmentValue(equipmentsdto.getEquipmentValue());
            equipment.setEquipmentPicture(equipmentsdto.getEquipmentPicture());
            equipment.setCategories(equipmentsdto.getCategories());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
   		 	equipment.setEquipmentPicture(fileName);   		 	
   		 	equipment.setAuditIdentifierId(auditIdentifier);
            Equipments savedEquipment = equipmentsService.saveEquipment(equipment);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Equipment " + equipment.getEquipmentName() + " updated.", requestService.getClientIp(request), userAgent, equipment.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./equipment-pictures/"+savedEquipment.getId();
//		     String uploadDir = "/var/spring/equipment-pictures/"+savedEquipment.getId();
			 Path uploadPath = Paths.get(uploadDir);
			 
			 if(!Files.exists(uploadPath)) {
				 Files.createDirectories(uploadPath);
			 }			 
			 try(InputStream inputStream = multipartFile.getInputStream()){
				 Path filePath = uploadPath.resolve(fileName);
				 System.out.println(filePath.toString());
				 System.out.println(filePath.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Equipment Picture : " + fileName, e);
			}
			 		 
            redirectAttributes.addFlashAttribute("success", "Equipment saved successfully!!");
            return "redirect:/equipments/index";
        } else {
            return "equipments/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Equipments equipment = equipmentsService.getEquipment(id);
        EquipmentsDTO equipmentdto = new EquipmentsDTO(equipment.getId(), equipment.getRecordDate(), equipment.getEquipmentName(), equipment.getSerialNumber(), equipment.getStatus(), equipment.getEquipmentValue(), equipment.getEquipmentPicture(), equipment.getCategories());
        model.addAttribute("equipmentdto", equipmentdto);
        model.addAttribute("listCategories", categoriesService.getAllCategories());
        model.addAttribute("listLocations", locationsService.getAllLocations());
        return "equipments/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("equipment") EquipmentsDTO equipmentdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("equipmentPicture") MultipartFile multipartFile) throws IOException {
        if (!bindingResult.hasErrors()) {
            Equipments equipment = equipmentsService.getEquipment(equipmentdto.getId());
            equipment.setRecordDate(equipmentdto.getRecordDate());
            equipment.setEquipmentName(equipmentdto.getEquipmentName());
            equipment.setSerialNumber(equipmentdto.getSerialNumber());
            equipment.setStatus(equipmentdto.getStatus());
            equipment.setEquipmentValue(equipmentdto.getEquipmentValue());
            equipment.setEquipmentPicture(equipmentdto.getEquipmentPicture());
            equipment.setCategories(equipmentdto.getCategories());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
   		 	equipment.setEquipmentPicture(fileName);
   		 	
   		 	
            Equipments savedEquipment = equipmentsService.saveEquipment(equipment);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Equipment " + equipment.getEquipmentName() + " updated.", requestService.getClientIp(request), userAgent, equipment.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./equipment-pictures/"+savedEquipment.getId();
//		     String uploadDir = "/var/spring/equipment-pictures/"+savedEquipment.getId();
			 Path uploadPath = Paths.get(uploadDir);
			 
			 if(!Files.exists(uploadPath)) {
				 Files.createDirectories(uploadPath);
			 }			 
			 try(InputStream inputStream = multipartFile.getInputStream()){
				 Path filePath = uploadPath.resolve(fileName);
				 System.out.println(filePath.toString());
				 System.out.println(filePath.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Equipment Picture : " + fileName, e);
			}		 
            redirectAttributes.addFlashAttribute("success", "Equipment updated successfully!!");
            return "redirect:/equipments/index";
        } else {
            return "equipments/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Equipments equipment = equipmentsService.getEquipment(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Equipment " + equipment.getEquipmentName() + " has been deleted.", requestService.getClientIp(request), userAgent, equipment.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.equipmentsService.deleteEquipment(id);
        redirectAttributes.addFlashAttribute("warning", "Equipment has been deleted successfully!!");
        return "redirect:/equipments/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Equipments> equipments = equipmentsService.getEquipmentsForDatatable(queryString, pageRequest);
        return this.getJsonData(equipments, draw);
    }
    
    
    private String getJsonData(Page<Equipments> equipments, int draw) {
        long totalRecords = equipments.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        equipments.forEach(equipment -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", equipment.getId());
            cellData.put("equipmentName", equipment.getEquipmentName());
            cellData.put("serialNumber", equipment.getSerialNumber());
            cellData.put("status", equipment.getStatus());
            cellData.put("categories", equipment.getCategories().getName());
            cellData.put("equipmentPicture", equipment.getEquipmentPicturePath());
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
