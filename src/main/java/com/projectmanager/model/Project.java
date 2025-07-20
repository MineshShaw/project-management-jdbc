package com.projectmanager.model;

import java.math.*;
import java.time.LocalDateTime;
import java.util.*;

import com.projectmanager.model.enums.ProjectStatus;

public class Project {
    private UUID projectId;
    private String projectName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID teamId;
    private ProjectStatus status;
    private boolean completed;
    private BigDecimal progressPercent;

    public Project() {}

    public Project(UUID projectId, String projectName, String description,
                   LocalDateTime startDate, LocalDateTime endDate,
                   UUID teamId, ProjectStatus status, boolean completed, BigDecimal progressPercent) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamId = teamId;
        this.status = status;
        this.completed = completed;
        this.progressPercent = progressPercent;
    }
    
    public UUID getProjectId() {
        return projectId;
    }
    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public UUID getTeamId() {
        return teamId;
    }
    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public ProjectStatus getStatus() {
        return status;
    }
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public BigDecimal getProgressPercent() {
        return progressPercent;
    }
    public void setProgressPercent(BigDecimal progressPercent) {
        this.progressPercent = progressPercent;
    }
}

