package com.isms.dto;

import java.time.LocalDate;

import com.isms.model.JobStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class JobItemsDTO {

    private Integer id;

    @NotNull
    private Integer assignmentId;

    @NotNull
    private JobStatus status;

    @Size(max = 1000)
    private String remarks;

    private LocalDate dueDate;

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
