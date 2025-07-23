package com.projectmanager.dao;

import com.projectmanager.model.EmployeeUtilization;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeUtilizationDAO {
    private Connection conn;
    public EmployeeUtilizationDAO(Connection conn) {
        this.conn = conn;
    }

    public List<EmployeeUtilization> getAllUtilizations() {
        List<EmployeeUtilization> list = new ArrayList<>();
        String query = "SELECT * FROM employee_utilization";

        try (
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

    public boolean deleteUtilization(UUID employeeId) {
        String query = "DELETE FROM employee_utilization WHERE employee_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId.toString());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
