package com.isms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "job_items", indexes = {
    @Index(name = "ux_job_items_job_code", columnList = "job_code", unique = true),
    @Index(name = "ix_job_items_assignment_id", columnList = "assignment_id"),
    @Index(name = "ix_job_items_client_id", columnList = "client_id")
})
public class JobItem {

    public enum Status { PENDING, IN_PROGRESS, COMPLETE, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_code", nullable = false, unique = true, length = 64)
    private String jobCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    /** denormalized snapshot of client FK for fast filtering/reporting */
    @Column(name = "client_id", nullable = false)
    private Integer clientId;

    /** optional read-only navigation to the same client_id */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Clients client;

    @Column(name = "equipment_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal equipmentCost;

    @Column(name = "project_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal projectCost;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.totalCost == null) {
            this.totalCost = safe(this.equipmentCost).add(safe(this.projectCost));
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
        this.totalCost = safe(this.equipmentCost).add(safe(this.projectCost));
    }

    private static BigDecimal safe(BigDecimal v) { return v != null ? v : BigDecimal.ZERO; }

    // getters/setters
    public Integer getId() { return id; }
    public String getJobCode() { return jobCode; }
    public void setJobCode(String jobCode) { this.jobCode = jobCode; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    public Clients getClient() { return client; } // read-only (managed by clientId)

    public BigDecimal getEquipmentCost() { return equipmentCost; }
    public void setEquipmentCost(BigDecimal equipmentCost) { this.equipmentCost = equipmentCost; }

    public BigDecimal getProjectCost() { return projectCost; }
    public void setProjectCost(BigDecimal projectCost) { this.projectCost = projectCost; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
