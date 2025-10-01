package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Equipments;

public interface EquipmentsRepository extends JpaRepository<Equipments, Integer> , JpaSpecificationExecutor<Equipments>{
	
}
