package com.projectmanager.model;

import java.util.UUID;

public class Employee {
    private UUID employeeId;
    private String employeeName;
    private String employeeEmail;
    private String contact;

    public Employee() {}
    public Employee(UUID employeeId, String employeeName, String employeeEmail, String contact) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.contact = contact;
    }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
