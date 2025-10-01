package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Roles;

public interface RolesService {
	
	List<Roles> getAllRoles();
	
	Roles saveRole(Roles role);
	
	Roles getRole(int id);
	
	void deleteRole(int id);
	
	Page<Roles> getRolesForDatatable(String queryString, Pageable pageable);

}
