package com.isms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isms.model.JobCodeSeq;

import jakarta.persistence.LockModeType;

@Repository
public interface JobCodeSeqRepository extends JpaRepository<JobCodeSeq, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from JobCodeSeq s where s.scope = :scope")
    Optional<JobCodeSeq> findByScopeForUpdate(@Param("scope") String scope);
}
