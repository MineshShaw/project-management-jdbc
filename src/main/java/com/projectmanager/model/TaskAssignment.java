package com.projectmanager.model;

import java.util.UUID;

public class TaskAssignment {
    private UUID employeeId;
    private UUID taskId;
    private String role;

    public TaskAssignment() {}
    public TaskAssignment(UUID employeeId, UUID taskId, String role) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.role = role;
    }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}


