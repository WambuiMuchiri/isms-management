// com.isms.model.JobCodeSeq
package com.isms.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "job_code_seq", catalog = "isms_db", indexes = {
    @Index(name = "ux_job_code_seq_scope", columnList = "scope", unique = true)
})
public class JobCodeSeq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scope", nullable = false, unique = true, length = 64)
    private String scope;

    @Column(name = "seq_value", nullable = false)
    private Long seqValue = 0L;

    @Version
    @Column(name = "lock_version")    // ← was "version"
    private Long lockVersion;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected JobCodeSeq() {}
    public JobCodeSeq(String scope) { this.scope = scope; }

    public Long getId() { return id; }
    public String getScope() { return scope; }
    public Long getSeqValue() { return seqValue; }
    public void setSeqValue(Long v) { this.seqValue = v; }

    @PrePersist @PreUpdate
    void touch() { this.updatedAt = Instant.now(); }
}
