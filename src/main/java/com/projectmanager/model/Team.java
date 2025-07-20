package com.projectmanager.model;

public class Team {
    private String teamId;
    private String teamName;
    private String projectId;
    private boolean active;

    public Team() {}
    public Team(String teamId, String teamName, String projectId, boolean active) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.projectId = projectId;
        this.active = active;
    }

    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
