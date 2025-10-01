// com.isms.repository.JobItemRepository
package com.isms.repository;

import com.isms.model.JobItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobItemRepository extends JpaRepository<JobItem, Integer> {}
