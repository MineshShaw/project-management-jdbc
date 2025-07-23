package com.projectmanager.dao;

import com.projectmanager.model.Employee;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeDAO {
    private Connection conn;
    public EmployeeDAO(Connection conn) {
        this.conn = conn;
    }
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(extractEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public Employee getEmployeeById(UUID employeeId) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractEmployee(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertEmployee(Employee employee) {
        String query = "INSERT INTO employees (employee_id, employee_name, employee_email, contact) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (employee.getEmployeeId() != null)
                stmt.setString(1, employee.getEmployeeId().toString());
            else
                throw new IllegalArgumentException("Employee ID cannot be null");
            if (employee.getEmployeeName() != null)
                stmt.setString(2, employee.getEmployeeName());
            else
                stmt.setNull(2, Types.VARCHAR);

            if (employee.getEmployeeEmail() != null)
                stmt.setString(3, employee.getEmployeeEmail());
            else
                stmt.setNull(3, Types.VARCHAR);

            if (employee.getContact() != null)
                stmt.setString(4, employee.getContact());
            else
                stmt.setNull(4, Types.VARCHAR);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees SET employee_name = ?, employee_email = ?, contact = ? WHERE employee_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            if (employee.getEmployeeName() != null)
                stmt.setString(1, employee.getEmployeeName());
            else
                stmt.setNull(1, Types.VARCHAR);

            if (employee.getEmployeeEmail() != null)
                stmt.setString(2, employee.getEmployeeEmail());
            else
                stmt.setNull(2, Types.VARCHAR);

            if (employee.getContact() != null)
                stmt.setString(3, employee.getContact());
            else
                stmt.setNull(3, Types.VARCHAR);

            stmt.setString(4, employee.getEmployeeId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteEmployee(UUID employeeId) {
        String query = "DELETE FROM employees WHERE employee_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeId.toString());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeId(UUID.fromString(rs.getString("employee_id")));
        emp.setEmployeeName(rs.getString("employee_name"));
        emp.setEmployeeEmail(rs.getString("employee_email"));
        emp.setContact(rs.getString("contact"));
        return emp;
    }
}
