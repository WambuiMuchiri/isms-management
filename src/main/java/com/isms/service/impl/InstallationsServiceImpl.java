package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.InstallationsDataFilter;
import com.isms.model.Installations;
import com.isms.repository.InstallationsRepository;
import com.isms.service.InstallationsService;

@Service
public class InstallationsServiceImpl implements InstallationsService{

	private InstallationsRepository installationsRepository;
	public InstallationsServiceImpl(InstallationsRepository installationsRepository) {
		super();
		this.installationsRepository = installationsRepository;
	}
	
	
	@Override
	public List<Installations> getAllInstallations() {
		return this.installationsRepository.findAll();
	}
	
	
	@Override
	public Installations saveInstallation(Installations installation) {
		return this.installationsRepository.save(installation);
	}
	
	
	@Override
	public Installations getInstallation(int id) {
		Optional<Installations> optional = installationsRepository.findById(id);
        Installations installation = null;
        if (optional.isPresent()) {
            installation = optional.get();
        } else {
            throw new RuntimeException("Installation not found for id : " + id);
        }
        return installation;
    }
	
	
	@Override
	public void deleteInstallation(int id) {
		installationsRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Installation does not exist in the db with the id : ", "Id", id));
		installationsRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Installations> getInstallationsForDatatable(String queryString, Pageable pageable) {
		InstallationsDataFilter installationsDataFilter = new InstallationsDataFilter(queryString);
		return installationsRepository.findAll(installationsDataFilter, pageable);
	}

}
