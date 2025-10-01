package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Sites;

public interface SitesRepository extends JpaRepository<Sites, Integer> , JpaSpecificationExecutor<Sites>{
	
}
