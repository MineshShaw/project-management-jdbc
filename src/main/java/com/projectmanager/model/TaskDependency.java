package com.projectmanager.model;

public class TaskDependency {
    private String taskId;
    private String dependsOn;

    public TaskDependency() {}
    public TaskDependency(String taskId, String dependsOn) {
        this.taskId = taskId;
        this.dependsOn = dependsOn;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getDependsOn() { return dependsOn; }
    public void setDependsOn(String dependsOn) { this.dependsOn = dependsOn; }
}

