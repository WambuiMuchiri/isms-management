package com.isms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isms.model.CashIn;

public interface CashInRepository extends JpaRepository<CashIn, Integer>, JpaSpecificationExecutor<CashIn>{

}
