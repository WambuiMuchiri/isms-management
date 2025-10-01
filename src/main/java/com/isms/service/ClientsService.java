package com.isms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isms.model.Clients;

public interface ClientsService {
	
	List<Clients> getAllClients();
	
	Clients saveClient(Clients client);
	
	Clients getClient(int id);
	
	void deleteClient(int id);
	
	Page<Clients> getClientsForDatatable(String queryString, Pageable pageable);

}
