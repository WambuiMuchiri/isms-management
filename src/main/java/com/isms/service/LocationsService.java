package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Locations;

public interface LocationsService {
	
	List<Locations> getAllLocations();
	
	Locations saveLocation(Locations location);
	
	Locations getLocation(int id);
	
	void deleteLocation(int id);
	
	Page<Locations> getLocationsForDatatable(String queryString, Pageable pageable);

}
