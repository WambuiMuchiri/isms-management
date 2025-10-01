package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Personnels;

public interface PersonnelsService {
	
	List<Personnels> getAllPersonnels();
	
	Personnels savePersonnel(Personnels personnel);
	
	Personnels getPersonnel(int id);
	
	void deletePersonnel(int id);
	
	Page<Personnels> getPersonnelsForDatatable(String queryString, Pageable pageable);

}
