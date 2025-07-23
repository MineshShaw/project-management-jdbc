package com.projectmanager.dao;

import com.projectmanager.model.TeamAssignment;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamAssignmentDAO {
    private Connection conn;
    public TeamAssignmentDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertAssignment(TeamAssignment assignment) {
        String query = "INSERT INTO team_assignment (team_id, employee_id) VALUES (?, ?)";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, assignment.getTeamId().toString());
            stmt.setString(2, assignment.getEmployeeId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAssignment(UUID teamId, UUID employeeId) {
        String query = "DELETE FROM team_assignment WHERE team_id = ? AND employee_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teamId.toString());
            stmt.setString(2, employeeId.toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<TeamAssignment> getAllAssignments() {
        List<TeamAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM team_assignment";

        try (
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TeamAssignment ta = new TeamAssignment();
                ta.setTeamId(UUID.fromString(rs.getString("team_id")));
                ta.setEmployeeId(UUID.fromString(rs.getString("employee_id")));
                assignments.add(ta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }
}
