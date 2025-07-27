package com.projectmanager.model.enums;

public enum TaskStatus {
    NULL,
    TODO,
    IN_PROGRESS,
    DONE,
    BLOCKED;

    public static TaskStatus fromString(String value) {
        if (value == null) return NULL;

        return switch (value.toLowerCase()) {
            case "todo" -> TODO;
            case "in_progress" -> IN_PROGRESS;
            case "done" -> DONE;
            case "blocked" -> BLOCKED;
            default -> NULL;
        };
    }

    public String toDbValue() {
        return switch (this) {
            case TODO -> "todo";
            case IN_PROGRESS -> "in_progress";
            case DONE -> "done";
            case BLOCKED -> "blocked";
            default -> null;
        };
    }
}
