package com.projectmanager.model;

import java.time.LocalDateTime;
import com.projectmanager.model.enums.TaskStatus;

public class Task {
    private String taskId;
    private String projectId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private boolean completed;

    public Task() {}

    public Task(String taskId, String projectId, String title, String description,
                LocalDateTime dueDate, TaskStatus status, boolean completed) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.completed = completed;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

