package com.isms.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.dto.UserDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.model.AuditIdentifier;
import com.isms.model.Users;
import com.isms.service.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UsersApi {

    @Autowired private UserService usersService;
    @Autowired private AuditIdentifierService auditIdentifierService;
    @Autowired private AuditEventsService auditEventsService;
    @Autowired private RequestService requestService;
    @Autowired private AuditTypesService auditTypesService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private RolesService rolesService;

    /** List all users as DTOs */
    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        List<Users> users = usersService.getAllUsers();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (Users user : users) {
            userDTOs.add(new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getRoles()
            ));
        }
        return ResponseEntity.ok(userDTOs);
    }

    /** Get user by ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        Users user = usersService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO dto = new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles()
        );
        return ResponseEntity.ok(dto);
    }

    /** Create user */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createUser(
            @ModelAttribute UserDTO usersDTO,
            @RequestParam("userLogo") MultipartFile multipartFile,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest request) throws IOException {

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

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setUserLogo(fileName);
        user.setAuditIdentifierId(auditIdentifier);

        Users savedUser = usersService.saveUser(user);

        auditEventsService.saveAuditEvents(new SaveEventsHelper(
                new Date(),
                "New User of email " + user.getEmail() + " created.",
                requestService.getClientIp(request),
                userAgent,
                auditIdentifier,
                auditTypesService.findAuditTypesByName("CREATE")
        ));

        saveUserLogo(savedUser.getId(), fileName, multipartFile);

        UserDTO dto = new UserDTO(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getStatus(),
                savedUser.getRoles()
        );
        return ResponseEntity.ok(dto);
    }

    /** Update user */
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUser(
            @PathVariable int id,
            @ModelAttribute UserDTO usersDTO,
            @RequestParam("userLogo") MultipartFile multipartFile,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest request) throws IOException {

        Users user = usersService.getUserOld(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

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
        user.setPassword(passwordEncoder.encode(usersDTO.getPassword()));

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setUserLogo(fileName);

        Users savedUser = usersService.saveUser(user);

        auditEventsService.saveAuditEvents(new SaveEventsHelper(
                new Date(),
                "User of email address " + user.getEmail() + " updated.",
                requestService.getClientIp(request),
                userAgent,
                user.getAuditIdentifierId(),
                auditTypesService.findAuditTypesByName("UPDATE")
        ));

        saveUserLogo(savedUser.getId(), fileName, multipartFile);

        UserDTO dto = new UserDTO(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getStatus(),
                savedUser.getRoles()
        );
        return ResponseEntity.ok(dto);
    }

    /** Delete user */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable int id,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest request) {

        Users user = usersService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        usersService.deleteUser(id);

        auditEventsService.saveAuditEvents(new SaveEventsHelper(
                new Date(),
                "User " + user.getEmail() + " deleted.",
                requestService.getClientIp(request),
                userAgent,
                user.getAuditIdentifierId(),
                auditTypesService.findAuditTypesByName("DELETE")
        ));

        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    /** Datatable JSON endpoint */
    @GetMapping("/datatable")
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
        Pageable pageRequest = dataForDatatable.getPageable();
        String queryString = (String) params.get("search[value]");
        Page<Users> users = usersService.getUsersForDatatable(queryString, pageRequest);
        return getJsonData(users, draw);
    }

    private String getJsonData(Page<Users> users, int draw) {
        long totalRecords = users.getTotalElements();
        List<UserDTO> dtoList = new ArrayList<>();
        users.forEach(user -> dtoList.add(new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles()
        )));

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("draw", draw);
        jsonMap.put("recordsTotal", totalRecords);
        jsonMap.put("recordsFiltered", totalRecords);
        jsonMap.put("data", dtoList);
        try {
            return new ObjectMapper().writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }

    private void saveUserLogo(int userId, String fileName, MultipartFile multipartFile) throws IOException {
        String uploadDir = "./user-logos/" + userId;
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
