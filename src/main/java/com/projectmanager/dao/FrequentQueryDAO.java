package com.projectmanager.dao;

import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.*;

public class FrequentQueryDAO {

    // 1. List all tasks for a project with their statuses
    public List<Map<String, Object>> listTasksForProject(UUID projectId) {
        String query = "SELECT task_id, title, status FROM tasks WHERE project_id = ?";
        List<Map<String, Object>> tasks = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("taskId", rs.getString("task_id"));
                row.put("title", rs.getString("title"));
                row.put("status", rs.getString("status"));
                tasks.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 2. Retrieve overdue tasks assigned to an employee
    public List<Map<String, Object>> getOverdueTasks(UUID employeeId) {
        String query = """
            SELECT t.task_id, t.title, t.due_date
            FROM tasks t
            JOIN task_assignment ta ON t.task_id = ta.task_id
            WHERE ta.employee_id = ?
              AND t.completed = false
              AND t.due_date < CURRENT_DATE
            """;
        List<Map<String, Object>> tasks = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("taskId", rs.getString("task_id"));
                row.put("title", rs.getString("title"));
                row.put("dueDate", rs.getTimestamp("due_date"));
                tasks.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 3. Find tasks blocked by dependencies
    public List<UUID> getBlockedTasks() {
        String query = """
            SELECT td.task_id
            FROM task_dependency td
            JOIN tasks dep ON td.depends_on = dep.task_id
            WHERE dep.completed = false
            """;
        List<UUID> taskIds = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                taskIds.add(UUID.fromString(rs.getString("task_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskIds;
    }

    // 4. Show comment history for a task
    public List<Map<String, Object>> getCommentHistory(UUID taskId) {
        String query = """
            SELECT tc.timestamp, e.employee_name, tc.comment
            FROM task_comments tc
            JOIN employees e ON tc.employee_id = e.employee_id
            WHERE tc.task_id = ?
            ORDER BY tc.timestamp
            """;
        List<Map<String, Object>> comments = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("timestamp", rs.getTimestamp("timestamp"));
                row.put("employeeName", rs.getString("employee_name"));
                row.put("comment", rs.getString("comment"));
                comments.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    // 5. Generate project progress summary
    public List<Map<String, Object>> getProjectProgressSummary() {
        String query = """
            SELECT project_id, progress_percent FROM projects;
            """;
        List<Map<String, Object>> summary = new ArrayList<>();

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("projectId", UUID.fromString(rs.getString("project_id")));
                row.put("progressPercent", rs.getBigDecimal("progress_percent"));
                summary.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summary;
    }
}
