package com.projectmanager.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskComment {
    private UUID taskId;
    private UUID employeeId;
    private LocalDateTime timestamp;
    private String comment;

    public TaskComment() {}
    public TaskComment(UUID taskId, UUID employeeId, LocalDateTime timestamp, String comment) {
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}


