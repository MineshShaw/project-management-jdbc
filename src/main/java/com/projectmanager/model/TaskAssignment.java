package com.projectmanager.model;

public class TaskAssignment {
    private String employeeId;
    private String taskId;
    private String role;

    public TaskAssignment() {}
    public TaskAssignment(String employeeId, String taskId, String role) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.role = role;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}


