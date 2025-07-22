package com.projectmanager.model;

import java.util.UUID;

public class TaskDependency {
    private UUID taskId;
    private UUID dependsOn;

    public TaskDependency() {}
    public TaskDependency(UUID taskId, UUID dependsOn) {
        this.taskId = taskId;
        this.dependsOn = dependsOn;
    }

    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }

    public UUID getDependsOn() { return dependsOn; }
    public void setDependsOn(UUID dependsOn) { this.dependsOn = dependsOn; }
}

