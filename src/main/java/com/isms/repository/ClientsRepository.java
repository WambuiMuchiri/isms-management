package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Clients;

public interface ClientsRepository extends JpaRepository<Clients, Integer> , JpaSpecificationExecutor<Clients>{
		
}
