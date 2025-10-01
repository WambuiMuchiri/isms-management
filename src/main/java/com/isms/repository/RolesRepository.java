package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> , JpaSpecificationExecutor<Roles>{
	
	Roles findByName(String name);

}
