package com.projectmanager.dao;

import com.projectmanager.model.Task;
import com.projectmanager.model.enums.TaskStatus;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDAO {
    private Connection conn;
    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";

        try (
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<Task> getTasksByProjectId(UUID projectId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE project_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public boolean insertTask(Task task) {
        String query = "INSERT INTO tasks (task_id, project_id, title, description, due_date, status, completed) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, task.getTaskId().toString());

            if (task.getProjectId() != null)
                stmt.setString(2, task.getProjectId().toString());
            else
                throw new IllegalArgumentException("Project ID cannot be null");
            if (task.getTitle() != null)
                stmt.setString(3, task.getTitle());
            else
                stmt.setNull(3, Types.VARCHAR);
            if (task.getDescription() != null)
                stmt.setString(4, task.getDescription());
            else
                stmt.setNull(4, Types.VARCHAR);
            if (task.getDueDate() != null)
                stmt.setTimestamp(5, Timestamp.valueOf(task.getDueDate()));
            else
                stmt.setNull(5, Types.TIMESTAMP);
            if (task.getStatus() != null)
                stmt.setString(6, task.getStatus().name());
            else
                stmt.setNull(6, Types.VARCHAR);
            if (task.isCompleted())
                stmt.setBoolean(7, task.isCompleted());
            else
                stmt.setNull(7, Types.BOOLEAN);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateTask(Task task) {
        String query = "UPDATE tasks SET project_id = ?, title = ?, description = ?, due_date = ?, status = ?, completed = ? WHERE task_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, task.getProjectId().toString());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(task.getDueDate()));
            stmt.setString(5, task.getStatus().name());
            stmt.setBoolean(6, task.isCompleted());
            stmt.setString(7, task.getTaskId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteTask(UUID taskId) {
        String query = "DELETE FROM tasks WHERE task_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(UUID.fromString(rs.getString("task_id")));
        task.setProjectId(UUID.fromString(rs.getString("project_id")));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));
        task.setCompleted(rs.getBoolean("completed"));
        return task;
    }
}
