package com.projectmanager.dao;

import com.projectmanager.model.TaskAssignment;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskAssignmentDAO {

    public List<TaskAssignment> getAllAssignments() {
        List<TaskAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM task_assignment";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                assignments.add(extractAssignment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    public List<TaskAssignment> getAssignmentsByTaskId(UUID taskId) {
        List<TaskAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM task_assignment WHERE task_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                assignments.add(extractAssignment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    public boolean insertAssignment(TaskAssignment assignment) {
        String query = "INSERT INTO task_assignment (task_id, employee_id, assigned_role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, assignment.getTaskId().toString());
            stmt.setString(2, assignment.getEmployeeId().toString());
            stmt.setString(3, assignment.getRole());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAssignment(UUID taskId, UUID employeeId) {
        String query = "DELETE FROM task_assignment WHERE task_id = ? AND employee_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskId.toString());
            stmt.setString(2, employeeId.toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private TaskAssignment extractAssignment(ResultSet rs) throws SQLException {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId(UUID.fromString(rs.getString("task_id")));
        assignment.setEmployeeId(UUID.fromString(rs.getString("employee_id")));
        assignment.setRole(rs.getString("assigned_role"));
        return assignment;
    }
}
