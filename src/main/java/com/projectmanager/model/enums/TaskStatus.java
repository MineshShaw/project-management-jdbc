package com.projectmanager.model.enums;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED;

    public static TaskStatus fromString(String value) {
        return TaskStatus.valueOf(value.toUpperCase());
    }

    public String toDbValue() {
        return this.name().toLowerCase();
    }
}
