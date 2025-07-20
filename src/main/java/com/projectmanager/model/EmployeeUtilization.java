package com.projectmanager.model;

public class EmployeeUtilization {
    private String employeeId;
    private int taskCount;

    public EmployeeUtilization() {}
    public EmployeeUtilization(String employeeId, int taskCount) {
        this.employeeId = employeeId;
        this.taskCount = taskCount;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
}

