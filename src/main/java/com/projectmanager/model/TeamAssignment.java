package com.projectmanager.model;

import java.util.UUID;

public class TeamAssignment {
    private UUID teamId;
    private UUID employeeId;

    public TeamAssignment() {}
    public TeamAssignment(UUID teamId, UUID employeeId) {
        this.teamId = teamId;
        this.employeeId = employeeId;
    }

    public UUID getTeamId() { return teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
}

