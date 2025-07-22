package com.projectmanager.model;

import java.util.UUID;

public class EmployeeUtilization {
    private UUID employeeId;
    private int taskCount;

    public EmployeeUtilization() {}
    public EmployeeUtilization(UUID employeeId, int taskCount) {
        this.employeeId = employeeId;
        this.taskCount = taskCount;
    }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
}

