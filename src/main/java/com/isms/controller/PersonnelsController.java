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
import com.isms.dto.PersonnelsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Personnels;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.CategoriesService;
import com.isms.service.LocationsService;
import com.isms.service.PersonnelsService;
import com.isms.service.RequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/personnels")
public class PersonnelsController {
	
	@Autowired
	private PersonnelsService personnelsService;
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
        return "personnels/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("personnelsdto", new PersonnelsDTO());
        model.addAttribute("listCategories", categoriesService.getAllCategories());
        model.addAttribute("listLocations", locationsService.getAllLocations());
        return "personnels/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("personnelsdto") PersonnelsDTO personnelsdto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("personnelPicture") MultipartFile multipartFile , 
            @RequestParam("scannedDocument") MultipartFile documentScanned) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);

            Personnels personnel = new Personnels();
            personnel.setRecordDate(personnelsdto.getRecordDate());
            personnel.setFirstName(personnelsdto.getFirstName());
            personnel.setLastName(personnelsdto.getLastName());
            personnel.setPersonnelName(personnelsdto.getPersonnelName());
            personnel.setIdNo(personnelsdto.getIdNo());
            personnel.setTelNo(personnelsdto.getTelNo());
            personnel.setEmail(personnelsdto.getEmail());
            personnel.setGender(personnelsdto.getGender());
            personnel.setStatus(personnelsdto.getStatus());
            personnel.setDetailedResidence(personnelsdto.getDetailedResidence());
            personnel.setAchievements(personnelsdto.getAchievements());
            personnel.setSalaryAmount(personnelsdto.getSalaryAmount());
            personnel.setEmployeeNo(personnelsdto.getEmployeeNo());
            personnel.setNssfNo(personnelsdto.getNssfNo());
            personnel.setNhifNo(personnelsdto.getNhifNo());
            personnel.setScannedDocument(personnelsdto.getScannedDocument());
            personnel.setPersonnelPicture(personnelsdto.getPersonnelPicture());
            personnel.setCategories(personnelsdto.getCategories());
            personnel.setLocations(personnelsdto.getLocations());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String documentScan = StringUtils.cleanPath(documentScanned.getOriginalFilename());
   		 	personnel.setPersonnelPicture(fileName);
   		 	personnel.setScannedDocument(documentScan);
   		 	   		 	
            personnel.setAuditIdentifierId(auditIdentifier);
            Personnels savedPersonnel = personnelsService.savePersonnel(personnel);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New Personnel " + personnel.getPersonnelName() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./personnel-pictures/"+savedPersonnel.getId();
//		     String uploadDir = "/var/spring/personnel-pictures/"+savedPersonnel.getId();
		     String uploadDir_2 = "./personnel-scanned-documents/"+savedPersonnel.getId();
//		     String uploadDir_2 = "/var/spring/personnel-scanned-documents/"+savedPersonnel.getId();
			 Path uploadPath = Paths.get(uploadDir);
			 Path uploadPath_2 = Paths.get(uploadDir_2);
			 
			 if(!Files.exists(uploadPath)) {
				 Files.createDirectories(uploadPath);
			 }
			 if(!Files.exists(uploadPath_2)) {
				 Files.createDirectories(uploadPath_2);
			 }
			 try(InputStream inputStream = multipartFile.getInputStream()){
				 Path filePath = uploadPath.resolve(fileName);
				 System.out.println(filePath.toString());
				 System.out.println(filePath.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Pupil Picture : " + fileName, e);
			}
			 try(InputStream inputStream = documentScanned.getInputStream()){
				 Path filePath_2 = uploadPath_2.resolve(documentScan);
				 System.out.println(filePath_2.toString());
				 System.out.println(filePath_2.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath_2, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Scanned Document : " + documentScan, e);
			}
			 		 
            redirectAttributes.addFlashAttribute("success", "Personnel saved successfully!!");
            return "redirect:/personnels/index";
        } else {
            return "personnels/create";
        }
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Personnels personnel = personnelsService.getPersonnel(id);
        PersonnelsDTO personneldto = new PersonnelsDTO(personnel.getId(), personnel.getRecordDate(), personnel.getFirstName(), personnel.getLastName(), personnel.getPersonnelName(), personnel.getIdNo(), personnel.getTelNo(), personnel.getEmail(), personnel.getGender(), personnel.getStatus(), personnel.getDetailedResidence(), personnel.getAchievements(), personnel.getSalaryAmount(), personnel.getEmployeeNo(), personnel.getNssfNo(), personnel.getNhifNo(), personnel.getScannedDocument(), personnel.getPersonnelPicture(), personnel.getCategories(), personnel.getLocations());
        model.addAttribute("personneldto", personneldto);
        model.addAttribute("listCategories", categoriesService.getAllCategories());
        model.addAttribute("listLocations", locationsService.getAllLocations());
        return "personnels/update";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("personnel") PersonnelsDTO personneldto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("personnelPicture") MultipartFile multipartFile, 
            @RequestParam("scannedDocument") MultipartFile documentScanned) throws IOException {
        if (!bindingResult.hasErrors()) {
            Personnels personnel = personnelsService.getPersonnel(personneldto.getId());
            personnel.setRecordDate(personneldto.getRecordDate());
            personnel.setFirstName(personneldto.getFirstName());
            personnel.setLastName(personneldto.getLastName());
            personnel.setPersonnelName(personneldto.getPersonnelName());
            personnel.setIdNo(personneldto.getIdNo());
            personnel.setTelNo(personneldto.getTelNo());
            personnel.setEmail(personneldto.getEmail());
            personnel.setGender(personneldto.getGender());
            personnel.setStatus(personneldto.getStatus());
            personnel.setDetailedResidence(personneldto.getDetailedResidence());
            personnel.setAchievements(personneldto.getAchievements());
            personnel.setSalaryAmount(personneldto.getSalaryAmount());
            personnel.setEmployeeNo(personneldto.getEmployeeNo());
            personnel.setNssfNo(personneldto.getNssfNo());
            personnel.setNhifNo(personneldto.getNhifNo());
            personnel.setScannedDocument(personneldto.getScannedDocument());
            personnel.setPersonnelPicture(personneldto.getPersonnelPicture());
            personnel.setCategories(personneldto.getCategories());
            personnel.setLocations(personneldto.getLocations());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String documentScan = StringUtils.cleanPath(documentScanned.getOriginalFilename());
   		 	personnel.setPersonnelPicture(fileName);
   		 	personnel.setScannedDocument(documentScan);
   		 	
   		 	
            Personnels savedPersonnel = personnelsService.savePersonnel(personnel);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Personnel " + personnel.getPersonnelName() + " updated.", requestService.getClientIp(request), userAgent, personnel.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./personnel-pictures/"+savedPersonnel.getId();
//		     String uploadDir = "/var/spring/personnel-pictures/"+savedPersonnel.getId();
		     String uploadDir_2 = "./personnel-scanned-documents/"+savedPersonnel.getId();
//		     String uploadDir_2 = "/var/spring/personnel-scanned-documents/"+savedPersonnel.getId();
			 Path uploadPath = Paths.get(uploadDir);
			 Path uploadPath_2 = Paths.get(uploadDir_2);
			 
			 if(!Files.exists(uploadPath)) {
				 Files.createDirectories(uploadPath);
			 }
			 if(!Files.exists(uploadPath_2)) {
				 Files.createDirectories(uploadPath_2);
			 }
			 try(InputStream inputStream = multipartFile.getInputStream()){
				 Path filePath = uploadPath.resolve(fileName);
				 System.out.println(filePath.toString());
				 System.out.println(filePath.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Pupil Picture : " + fileName, e);
			}
			 try(InputStream inputStream = documentScanned.getInputStream()){
				 Path filePath_2 = uploadPath_2.resolve(documentScan);
				 System.out.println(filePath_2.toString());
				 System.out.println(filePath_2.toFile().getAbsolutePath());
				 Files.copy(inputStream, filePath_2, StandardCopyOption.REPLACE_EXISTING);
			 }catch (IOException e) {
				throw new IOException("Could not save the uploaded Scanned Document : " + documentScan, e);
			}
			 		 
            redirectAttributes.addFlashAttribute("success", "Personnel updated successfully!!");
            return "redirect:/personnels/index";
        } else {
            return "personnels/update";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Personnels personnel = personnelsService.getPersonnel(id);

        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "Personnel " + personnel.getPersonnelName() + " has been deleted.", requestService.getClientIp(request), userAgent, personnel.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        auditEventsService.saveAuditEvents(saveEventsHelper);

        this.personnelsService.deletePersonnel(id);
        redirectAttributes.addFlashAttribute("warning", "Personnel has been deleted successfully!!");
        return "redirect:/personnels/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) (params.get("search[value]"));
        Page<Personnels> personnels = personnelsService.getPersonnelsForDatatable(queryString, pageRequest);
        return this.getJsonData(personnels, draw);
    }
    
    
    private String getJsonData(Page<Personnels> personnels, int draw) {
        long totalRecords = personnels.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        personnels.forEach(personnel -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", personnel.getId());
            cellData.put("personnelName", personnel.getPersonnelName());
            cellData.put("status", personnel.getStatus());
            cellData.put("gender", personnel.getGender());
            cellData.put("telNo", personnel.getTelNo());
            cellData.put("personnelPicture", personnel.getPersonnelPicturePath());
            cellData.put("scannedDocument", personnel.getScannedDocumentPath());
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
