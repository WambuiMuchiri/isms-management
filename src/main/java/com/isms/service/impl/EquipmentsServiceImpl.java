package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.filter.EquipmentsDataFilter;
import com.isms.model.Equipments;
import com.isms.repository.EquipmentsRepository;
import com.isms.service.EquipmentsService;

@Service
public class EquipmentsServiceImpl implements EquipmentsService {

	@Autowired
	private EquipmentsRepository equipmentsRepository;

	@Override
	public List<Equipments> getAllEquipments() {		
		return this.equipmentsRepository.findAll();
	}

	@Override
	public Equipments saveEquipment(Equipments equipment) {
		return this.equipmentsRepository.save(equipment);
		
	}

	@Override
	public Equipments getEquipment(int id) {
		Optional<Equipments> optional = equipmentsRepository.findById(id);
		Equipments equipment = null;
		if(optional.isPresent()) {
			equipment = optional.get();
		}
		else {
			throw new RuntimeException("Equipment not found for id : "+id);
		}
		return equipment;
	}

	@Override
	public void deleteEquipment(int id) {
		this.equipmentsRepository.deleteById(id);
		
	}

	@Override
	public Page<Equipments> getEquipmentsForDatatable(String queryString, Pageable pageable) {
		EquipmentsDataFilter equipmentsDataFilter = new EquipmentsDataFilter(queryString);
		return equipmentsRepository.findAll(equipmentsDataFilter, pageable);
	}
	
	
}

