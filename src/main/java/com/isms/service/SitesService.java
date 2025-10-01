package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Sites;

public interface SitesService {
	
	List<Sites> getAllSites();
	
	Sites saveSite(Sites site);
	
	Sites getSite(int id);
	
	void deleteSite(int id);
	
	Page<Sites> getSitesForDatatable(String queryString, Pageable pageable);

}
