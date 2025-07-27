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
        return switch (this) {
            case NOT_STARTED -> "not_started";
            case IN_PROGRESS -> "in_progress";
            case COMPLETED   -> "completed";
            case ON_HOLD     -> "on_hold";
            default          -> null;
        };
    }
}
