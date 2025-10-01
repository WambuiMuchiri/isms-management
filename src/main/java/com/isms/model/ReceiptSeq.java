package com.isms.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "receipt_seq", catalog = "isms_db", schema = "")
public class ReceiptSeq implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "seq_date", nullable = false)
	private LocalDate seqDate;

	@Column(name = "seq_value", nullable = false)
	private Integer seqValue = 0;

	public ReceiptSeq() {
		
	}

	public ReceiptSeq(LocalDate date, Integer last) {
		this.seqDate = date;
		this.seqValue = last;
	}

	public LocalDate getSeqDate() {
		return seqDate;
	}

	public void setSeqDate(LocalDate seqDate) {
		this.seqDate = seqDate;
	}

	public Integer getSeqValue() {
		return seqValue;
	}

	public void setSeqValue(Integer seqValue) {
		this.seqValue = seqValue;
	}
}
