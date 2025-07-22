package com.projectmanager.model;

import java.util.UUID;

public class Team {
    private UUID teamId;
    private String teamName;
    private UUID projectId;
    private boolean active;

    public Team() {}
    public Team(UUID teamId, String teamName, UUID projectId, boolean active) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.projectId = projectId;
        this.active = active;
    }

    public UUID getTeamId() { return teamId; }
    public void setTeamId(UUID teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
