package com.projectmanager.model;

import java.time.LocalDateTime;
import com.projectmanager.model.enums.ProjectStatus;

public class Project {
    private String projectId;
    private String projectName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String teamId;
    private ProjectStatus status;
    private boolean completed;
    private double progressPercent;

    // Constructors
    public Project() {}

    public Project(String projectId, String projectName, String description,
                   LocalDateTime startDate, LocalDateTime endDate,
                   String teamId, ProjectStatus status, boolean completed, double progressPercent) {
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

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
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

    public String getTeamId() {
        return teamId;
    }
    public void setTeamId(String teamId) {
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

    public double getProgressPercent() {
        return progressPercent;
    }
    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }
}

