package com.isms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isms.model.Visitors;
import com.isms.repository.VisitorsRepository;

@Service
public class VisitorsService {

	@Autowired
	private VisitorsRepository repository;

	public Visitors saveVisitorInfo(Visitors visitor) {
		return repository.save(visitor);
	}
	
	public List<Visitors> getAllVisits(){
		return repository.findAll();
	}

}