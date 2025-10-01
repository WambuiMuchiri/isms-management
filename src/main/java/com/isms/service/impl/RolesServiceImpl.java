package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.RolesDataFilter;
import com.isms.model.Roles;
import com.isms.repository.RolesRepository;
import com.isms.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService{

	private RolesRepository rolesRepository;
	public RolesServiceImpl(RolesRepository rolesRepository) {
		super();
		this.rolesRepository = rolesRepository;
	}
	
	
	@Override
	public List<Roles> getAllRoles() {
		return this.rolesRepository.findAll();
	}
	
	
	@Override
	public Roles saveRole(Roles role) {
		return this.rolesRepository.save(role);
	}
	
	
	@Override
	public Roles getRole(int id) {
		Optional<Roles> optional = rolesRepository.findById(id);
        Roles role = null;
        if (optional.isPresent()) {
            role = optional.get();
        } else {
            throw new RuntimeException("Role not found for id : " + id);
        }
        return role;
    }
	
	
	@Override
	public void deleteRole(int id) {
		rolesRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Role does not exist in the db with the id : ", "Id", id));
		rolesRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Roles> getRolesForDatatable(String queryString, Pageable pageable) {
		RolesDataFilter rolesDataFilter = new RolesDataFilter(queryString);
		return rolesRepository.findAll(rolesDataFilter, pageable);
	}

}
