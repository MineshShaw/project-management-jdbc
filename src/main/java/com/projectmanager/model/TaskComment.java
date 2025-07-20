package com.projectmanager.model;

import java.time.LocalDateTime;

public class TaskComment {
    private String taskId;
    private String employeeId;
    private LocalDateTime timestamp;
    private String comment;

    public TaskComment() {}
    public TaskComment(String taskId, String employeeId, LocalDateTime timestamp, String comment) {
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}


