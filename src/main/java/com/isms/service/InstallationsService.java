package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Installations;

public interface InstallationsService {
	
	List<Installations> getAllInstallations();
	
	Installations saveInstallation(Installations installation);
	
	Installations getInstallation(int id);
	
	void deleteInstallation(int id);
	
	Page<Installations> getInstallationsForDatatable(String queryString, Pageable pageable);

}
