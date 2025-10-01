package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.SitesDataFilter;
import com.isms.model.Sites;
import com.isms.repository.SitesRepository;
import com.isms.service.SitesService;

@Service
public class SitesServiceImpl implements SitesService{

	private SitesRepository sitesRepository;
	public SitesServiceImpl(SitesRepository sitesRepository) {
		super();
		this.sitesRepository = sitesRepository;
	}
	
	
	@Override
	public List<Sites> getAllSites() {
		return this.sitesRepository.findAll();
	}
	
	
	@Override
	public Sites saveSite(Sites site) {
		return this.sitesRepository.save(site);
	}
	
	
	@Override
	public Sites getSite(int id) {
		Optional<Sites> optional = sitesRepository.findById(id);
        Sites site = null;
        if (optional.isPresent()) {
            site = optional.get();
        } else {
            throw new RuntimeException("Site not found for id : " + id);
        }
        return site;
    }
	
	
	@Override
	public void deleteSite(int id) {
		sitesRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Site does not exist in the db with the id : ", "Id", id));
		sitesRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Sites> getSitesForDatatable(String queryString, Pageable pageable) {
		SitesDataFilter sitesDataFilter = new SitesDataFilter(queryString);
		return sitesRepository.findAll(sitesDataFilter, pageable);
	}

}
