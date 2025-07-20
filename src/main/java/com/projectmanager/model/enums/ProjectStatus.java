package com.projectmanager.model.enums;

public enum ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD;

    public static ProjectStatus fromString(String value) {
        return ProjectStatus.valueOf(value.toUpperCase());
    }

    public String toDbValue() {
        return this.name().toLowerCase();
    }
}
