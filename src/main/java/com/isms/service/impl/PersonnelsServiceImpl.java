package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.filter.PersonnelsDataFilter;
import com.isms.model.Personnels;
import com.isms.repository.PersonnelsRepository;
import com.isms.service.PersonnelsService;

@Service
public class PersonnelsServiceImpl implements PersonnelsService {

	@Autowired
	private PersonnelsRepository personnelsRepository;

	@Override
	public List<Personnels> getAllPersonnels() {		
		return this.personnelsRepository.findAll();
	}

	@Override
	public Personnels savePersonnel(Personnels personnel) {
		return this.personnelsRepository.save(personnel);
		
	}

	@Override
	public Personnels getPersonnel(int id) {
		Optional<Personnels> optional = personnelsRepository.findById(id);
		Personnels personnel = null;
		if(optional.isPresent()) {
			personnel = optional.get();
		}
		else {
			throw new RuntimeException("Personnel not found for id : "+id);
		}
		return personnel;
	}

	@Override
	public void deletePersonnel(int id) {
		this.personnelsRepository.deleteById(id);
		
	}

	@Override
	public Page<Personnels> getPersonnelsForDatatable(String queryString, Pageable pageable) {
		PersonnelsDataFilter personnelsDataFilter = new PersonnelsDataFilter(queryString);
		return personnelsRepository.findAll(personnelsDataFilter, pageable);
	}
	
	
}

