package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Installations;

public interface InstallationsRepository extends JpaRepository<Installations, Integer> , JpaSpecificationExecutor<Installations>{
	
}
