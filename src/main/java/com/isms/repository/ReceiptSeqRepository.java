package com.isms.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.isms.model.ReceiptSeq;

import jakarta.persistence.LockModeType;

public interface ReceiptSeqRepository extends JpaRepository<ReceiptSeq, LocalDate> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM ReceiptSeq r WHERE r.seqDate = :date")
	Optional<ReceiptSeq> findForUpdate(@Param("date") LocalDate date);
}
