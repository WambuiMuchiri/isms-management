package com.isms.api;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isms.dto.RolesDTO;
import com.isms.model.Roles;
import com.isms.service.RolesService;
import com.isms.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolesApi {
	
    @Autowired
    private UserService usersService;

    @Autowired
	private RolesService rolesService;

    @Autowired
	private ModelMapper modelMapper;
  
    
    @GetMapping("/list")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public ResponseEntity<?> getRoles() {
	    List<Roles> roles = rolesService.getAllRoles();
	    List<RolesDTO> roleDTO = new ArrayList<>();
	
	    System.out.println("Hi Sanky Roles are the following!");
	    for (Roles role : roles) {
	        roleDTO.add(new RolesDTO(role.getId(), role.getName(), role.getDescription()));
	    }
	    return ResponseEntity.ok(roleDTO);
	  }
  

}
