package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Equipments;

public interface EquipmentsService {
	
	List<Equipments> getAllEquipments();
	
	Equipments saveEquipment(Equipments equipment);
	
	Equipments getEquipment(int id);
	
	void deleteEquipment(int id);
	
	Page<Equipments> getEquipmentsForDatatable(String queryString, Pageable pageable);

}
