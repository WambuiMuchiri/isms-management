package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Projects;

public interface ProjectsRepository extends JpaRepository<Projects, Integer> , JpaSpecificationExecutor<Projects>{
	
}
