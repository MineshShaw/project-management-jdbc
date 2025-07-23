package com.projectmanager.model.enums;

public enum ProjectStatus {
    NULL,
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD;

    public static ProjectStatus fromString(String value) {
        if (value == null) return NULL;

        return switch (value.toLowerCase()) {
            case "not_started" -> NOT_STARTED;
            case "in_progress" -> IN_PROGRESS;
            case "completed"   -> COMPLETED;
            case "on_hold"     -> ON_HOLD;
            default            -> NULL;
        };
    }



    public String toDbValue() {
        return this.name().toLowerCase();
    }
}
