package com.projectmanager.dao;

import com.projectmanager.model.TaskDependency;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDependencyDAO {
    private Connection conn;
    public TaskDependencyDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TaskDependency> getAllDependencies() {
        List<TaskDependency> dependencies = new ArrayList<>();
        String query = "SELECT * FROM task_dependency";

        try (
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TaskDependency dep = new TaskDependency();
                dep.setTaskId(UUID.fromString(rs.getString("task_id")));
                dep.setDependsOn(UUID.fromString(rs.getString("depends_on")));
                dependencies.add(dep);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dependencies;
    }

    public boolean insertDependency(TaskDependency dep) {
        String query = "INSERT INTO task_dependency (task_id, depends_on) VALUES (?, ?)";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (dep.getTaskId() == null || dep.getDependsOn() == null) {
                throw new IllegalArgumentException("Task ID and Depends On cannot be null");
            }
            stmt.setString(1, dep.getTaskId().toString());
            stmt.setString(2, dep.getDependsOn().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteDependency(UUID taskId, UUID dependsOnId) {
        String query = "DELETE FROM task_dependency WHERE task_id = ? AND depends_on = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            stmt.setString(2, dependsOnId.toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<UUID> getDependenciesForTask(UUID taskId) {
        List<UUID> dependencies = new ArrayList<>();
        String query = "SELECT depends_on FROM task_dependency WHERE task_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dependencies.add(UUID.fromString(rs.getString("depends_on")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dependencies;
    }
}
