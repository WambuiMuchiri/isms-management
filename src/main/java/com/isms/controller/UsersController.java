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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.isms.dto.UserDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Users;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.RolesService;
import com.isms.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
    private UserService usersService;

    @Autowired
    private AuditIdentifierService auditIdentifierService;

    @Autowired
    private AuditEventsService auditEventsService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private AuditTypesService auditTypesService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
	private RolesService rolesService;

    @GetMapping(value = {"/index", "/", ""})
    public String index(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "users/index";
    }


    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("usersDTO", new UserDTO());
        	model.addAttribute("listRoles", rolesService.getAllRoles());
        return "users/create";
    }

    @PostMapping("/create")
    //public String create(@Valid @ModelAttribute("usersDTO") UserDTO usersDTO,
    public String create(@ModelAttribute("usersDTO") UserDTO usersDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("userLogo") MultipartFile multipartFile ) throws IOException {
        if (!bindingResult.hasErrors()) {
            AuditIdentifier auditIdentifier = new AuditIdentifier();
            auditIdentifierService.saveAuditIdentifier(auditIdentifier);
           
            Users user = new Users();
                    user.setFirstName(usersDTO.getFirstName());
                    user.setLastName(usersDTO.getLastName());
                    user.setUsername(usersDTO.getUsername());
                    user.setEmail(usersDTO.getEmail());
                    user.setStatus(usersDTO.getStatus());
                    user.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
                    user.setRoles(usersDTO.getRoles());
                    user.setRemarks(usersDTO.getRemarks()); 
                    //user.setUserLogo(usersDTO.getUserLogo());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
   		 	user.setUserLogo(fileName);
            
            user.setAuditIdentifierId(auditIdentifier);
            Users savedUser = usersService.saveUser(user);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "New User of email " + user.getEmail() + " created.", requestService.getClientIp(request), userAgent, auditIdentifier, auditTypesService.findAuditTypesByName("CREATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./user-logos/"+savedUser.getId();
//		     String uploadDir = "/var/spring/user-logos/"+savedUser.getId();
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
				throw new IOException("Could not save the uploaded User Logo File : " + fileName, e);
			}				 
		 
            redirectAttributes.addFlashAttribute("success", "User saved successfully!!");
            return "redirect:/users/index";
        } else {
            return "users/create";
        }
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, Model model) {
        Users users = usersService.getUserOld(id);
        UserDTO usersDTO = new UserDTO(users.getId(), users.getFirstName(), users.getLastName(), users.getUsername(), users.getEmail(), users.getStatus(), users.getPassword(), users.getRoles(), users.getRemarks(), users.getUserLogo());
        model.addAttribute("usersDTO", usersDTO);
        	model.addAttribute("listRoles", rolesService.getAllRoles());
        return "users/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @ModelAttribute("usersDTO") UserDTO usersDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "User-Agent") String userAgent,
            HttpServletRequest request,
            Model model, 
            @RequestParam("userLogo") MultipartFile multipartFile ) throws IOException {
        if (!bindingResult.hasErrors()) {
            Users user = usersService.getUserOld(id);

            if (user.getAuditIdentifierId() == null) {
                AuditIdentifier auditIdentifier = new AuditIdentifier();
                auditIdentifierService.saveAuditIdentifier(auditIdentifier);
                user.setAuditIdentifierId(auditIdentifier);
            }

            user.setFirstName(usersDTO.getFirstName());
            user.setLastName(usersDTO.getLastName());
            user.setUsername(usersDTO.getUsername());
            user.setEmail(usersDTO.getEmail());
            user.setStatus(usersDTO.getStatus());
            user.setRoles(usersDTO.getRoles());
            user.setRemarks(usersDTO.getRemarks());
            user.setUserLogo(usersDTO.getUserLogo());
           
            user.setPassword(passwordEncoder.encode(usersDTO.getPassword()));

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());            
   		 	user.setUserLogo(fileName);
   		 	
            System.out.println("User Details for update : "+user);
            Users savedUser = usersService.saveUser(user);

            SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "User of email address " + user.getEmail() + " updated.", requestService.getClientIp(request), userAgent, user.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("UPDATE"));
            auditEventsService.saveAuditEvents(saveEventsHelper);

		     String uploadDir = "./user-logos/"+savedUser.getId();
//		     String uploadDir = "/var/spring/user-logos/"+savedUser.getId();
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
				throw new IOException("Could not save the uploaded User Logo : " + fileName, e);
			}
		 		
            redirectAttributes.addFlashAttribute("success", "User updated successfully!!");
            return "redirect:/users/index";
        } else {
            return "/users/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
        Users users = usersService.getUserById(id);
        redirectAttributes.addFlashAttribute("warning", "User deleted successfully!!");
        SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(), "User " + users.getEmail() + " deleted.", requestService.getClientIp(request), userAgent, users.getAuditIdentifierId(), auditTypesService.findAuditTypesByName("DELETE"));
        this.usersService.deleteUser(id);
        auditEventsService.saveAuditEvents(saveEventsHelper);
        redirectAttributes.addFlashAttribute("warning", "User deleted successfully!!");
        return "redirect:/users/index";
    }

    @RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);

        Pageable pageRequest = dataForDatatable.getPageable();

        String queryString = (String) (params.get("search[value]"));
        System.err.println(queryString);
        Page<Users> users = usersService.getUsersForDatatable(queryString, pageRequest);
        return this.getJsonData(users, draw);
    }
    
    private String getJsonData(Page<Users> users, int draw) {
        long totalRecords = users.getTotalElements();
        List<Map<String, Object>> cells = new ArrayList<>();
        users.forEach(user -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", user.getId());
            cellData.put("userName", user.getUsername());
            cellData.put("status", user.getStatus());
            cellData.put("userLogo", user.getUserLogoPath());
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
            e.printStackTrace();
        }
        return json;
    }
}
