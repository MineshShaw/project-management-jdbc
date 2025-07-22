package com.projectmanager.dao;

import com.projectmanager.model.EmployeeUtilization;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeUtilizationDAO {

    public List<EmployeeUtilization> getAllUtilizations() {
        List<EmployeeUtilization> list = new ArrayList<>();
        String query = "SELECT * FROM employee_utilization";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmployeeUtilization eu = new EmployeeUtilization();
                eu.setEmployeeId(UUID.fromString(rs.getString("employee_id")));
                eu.setTaskCount(rs.getInt("task_count"));
                list.add(eu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertUtilization(EmployeeUtilization eu) {
        String query = "INSERT INTO employee_utilization (employee_id, task_id, hours_worked) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eu.getEmployeeId().toString());
            stmt.setInt(2, eu.getTaskCount());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateUtilization(EmployeeUtilization eu) {
        String query = "UPDATE employee_utilization SET hours_worked = ? WHERE employee_id = ? AND task_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eu.getTaskCount());
            stmt.setString(2, eu.getEmployeeId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteUtilization(UUID employeeId, UUID taskId) {
        String query = "DELETE FROM employee_utilization WHERE employee_id = ? AND task_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId.toString());
            stmt.setString(2, taskId.toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
