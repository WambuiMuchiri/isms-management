package com.isms.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.isms.model.ReceiptSeq;
import com.isms.repository.ReceiptSeqRepository;
import com.isms.service.ReceiptNumberGeneratorService;

@Component
public class ReceiptNumberGeneratorServiceImpl implements ReceiptNumberGeneratorService {

  private final ReceiptSeqRepository repo;

  public ReceiptNumberGeneratorServiceImpl(ReceiptSeqRepository repo) {
    this.repo = repo;
  }

  /** RCPT-YYYYMMDD-0001 (counter resets per day). */
  @Transactional
  public String next() {
    LocalDate today = LocalDate.now();

    ReceiptSeq seq = repo.findForUpdate(today)
        .orElseGet(() -> repo.save(new ReceiptSeq(today, 0)));

    int next = seq.getSeqValue() + 1;
    seq.setSeqValue(next);
    repo.save(seq);

    String yyyymmdd = today.toString().replace("-", "");
    return String.format("RCPT-%s-%04d", yyyymmdd, next);
  }
}
