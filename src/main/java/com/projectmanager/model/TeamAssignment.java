package com.projectmanager.model;

public class TeamAssignment {
    private String teamId;
    private String employeeId;

    public TeamAssignment() {}
    public TeamAssignment(String teamId, String employeeId) {
        this.teamId = teamId;
        this.employeeId = employeeId;
    }

    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}

