package com.isms.service.impl;

import com.isms.model.JobCodeSeq;
import com.isms.repository.JobCodeSeqRepository;
import com.isms.service.JobCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class JobCodeServiceImpl implements JobCodeService {
    @Autowired
    public JobCodeSeqRepository repo;

    @Transactional
    public String nextJobCode(Integer projectId) {
        String ym = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String scope = "P" + projectId + "-" + ym;            // scope per project & month
        JobCodeSeq seq = repo.findByScopeForUpdate(scope).orElseGet(() -> new JobCodeSeq(scope));
        seq.setSeqValue(seq.getSeqValue() + 1);
        repo.saveAndFlush(seq);
        // JB-P{projectId}-{yyyymm}-{00001}
        return String.format("JB-%s-%05d", scope, seq.getSeqValue());
    }
}
