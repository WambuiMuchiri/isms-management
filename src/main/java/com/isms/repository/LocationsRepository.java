package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Locations;

public interface LocationsRepository extends JpaRepository<Locations, Integer> , JpaSpecificationExecutor<Locations>{
		
}
