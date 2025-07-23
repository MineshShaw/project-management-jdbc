package com.projectmanager.dao;

import com.projectmanager.model.TaskComment;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskCommentDAO {

    private Connection conn;

    public TaskCommentDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TaskComment> getCommentsByTaskId(UUID taskId) {
        List<TaskComment> comments = new ArrayList<>();
        String query = "SELECT * FROM task_comments WHERE task_id = ? ORDER BY timestamp";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaskComment comment = extractFromResultSet(rs);
                    comments.add(comment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public boolean insertComment(TaskComment comment) {
        String query = "INSERT INTO task_comments (task_id, employee_id, comment, timestamp) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (comment.getTaskId() == null || comment.getEmployeeId() == null || comment.getComment() == null || comment.getTimestamp() == null) {
                throw new IllegalArgumentException("Task ID, Employee ID, Comment, and Timestamp cannot be null");
            }
            stmt.setString(1, comment.getTaskId().toString());
            stmt.setString(2, comment.getEmployeeId().toString());
            stmt.setString(3, comment.getComment());
            stmt.setTimestamp(4, Timestamp.valueOf(comment.getTimestamp()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteComment(UUID taskId, UUID employeeId, Timestamp timestamp) {
        String query = "DELETE FROM task_comments WHERE task_id = ? AND employee_id = ? AND timestamp = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            stmt.setString(2, employeeId.toString());
            stmt.setTimestamp(3, timestamp);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private TaskComment extractFromResultSet(ResultSet rs) throws SQLException {
        TaskComment comment = new TaskComment();
        comment.setTaskId(UUID.fromString(rs.getString("task_id")));
        comment.setEmployeeId(UUID.fromString(rs.getString("employee_id")));
        comment.setComment(rs.getString("comment"));
        comment.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return comment;
    }
}
