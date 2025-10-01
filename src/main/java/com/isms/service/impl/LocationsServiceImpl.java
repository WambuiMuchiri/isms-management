package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.LocationsDataFilter;
import com.isms.model.Locations;
import com.isms.repository.LocationsRepository;
import com.isms.service.LocationsService;

@Service
public class LocationsServiceImpl implements LocationsService{

	private LocationsRepository locationsRepository;
	public LocationsServiceImpl(LocationsRepository locationsRepository) {
		super();
		this.locationsRepository = locationsRepository;
	}
	
	
	@Override
	public List<Locations> getAllLocations() {
		return this.locationsRepository.findAll();
	}
	
	
	@Override
	public Locations saveLocation(Locations location) {
		return this.locationsRepository.save(location);
	}
	
	
	@Override
	public Locations getLocation(int id) {
		Optional<Locations> optional = locationsRepository.findById(id);
        Locations location = null;
        if (optional.isPresent()) {
            location = optional.get();
        } else {
            throw new RuntimeException("Location not found for id : " + id);
        }
        return location;
    }
	
	
	@Override
	public void deleteLocation(int id) {
		locationsRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Location does not exist in the db with the id : ", "Id", id));
		locationsRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Locations> getLocationsForDatatable(String queryString, Pageable pageable) {
		LocationsDataFilter locationsDataFilter = new LocationsDataFilter(queryString);
		return locationsRepository.findAll(locationsDataFilter, pageable);
	}

}
