package com.isms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.isms.exception.ResourceNotFoundException;
import com.isms.filter.ClientsDataFilter;
import com.isms.model.Clients;
import com.isms.repository.ClientsRepository;
import com.isms.service.ClientsService;

@Service
public class ClientsServiceImpl implements ClientsService{

	private ClientsRepository clientsRepository;
	public ClientsServiceImpl(ClientsRepository clientsRepository) {
		super();
		this.clientsRepository = clientsRepository;
	}
	
	
	@Override
	public List<Clients> getAllClients() {
		return this.clientsRepository.findAll();
	}
	
	
	@Override
	public Clients saveClient(Clients client) {
		return this.clientsRepository.save(client);
	}
	
	
	@Override
	public Clients getClient(int id) {
		Optional<Clients> optional = clientsRepository.findById(id);
        Clients client = null;
        if (optional.isPresent()) {
            client = optional.get();
        } else {
            throw new RuntimeException("Client not found for id : " + id);
        }
        return client;
    }
	
	
	@Override
	public void deleteClient(int id) {
		clientsRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Client does not exist in the db with the id : ", "Id", id));
		clientsRepository.deleteById(id);
		
	}
	
	
	@Override
	public Page<Clients> getClientsForDatatable(String queryString, Pageable pageable) {
		ClientsDataFilter clientsDataFilter = new ClientsDataFilter(queryString);
		return clientsRepository.findAll(clientsDataFilter, pageable);
	}

}
