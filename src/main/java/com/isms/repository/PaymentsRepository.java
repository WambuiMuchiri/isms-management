package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.Payments;

public interface PaymentsRepository extends JpaRepository<Payments, Integer>, JpaSpecificationExecutor<Payments>{

}
