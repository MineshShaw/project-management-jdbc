package com.projectmanager.dao;

import com.projectmanager.model.Team;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamDAO {

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                teams.add(extractTeam(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public Team getTeamById(UUID teamId) {
        String query = "SELECT * FROM teams WHERE team_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teamId.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractTeam(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertTeam(Team team) {
        String query = "INSERT INTO teams (team_id, team_name, project_id, active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, team.getTeamId().toString());
            stmt.setString(2, team.getTeamName());
            stmt.setString(3, team.getProjectId().toString());
            stmt.setBoolean(4, team.isActive());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateTeam(Team team) {
        String query = "UPDATE teams SET team_name = ?, project_id = ?, active = ? WHERE team_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, team.getTeamName());
            stmt.setString(2, team.getProjectId().toString());
            stmt.setBoolean(3, team.isActive());
            stmt.setString(4, team.getTeamId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteTeam(UUID teamId) {
        String query = "DELETE FROM teams WHERE team_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teamId.toString());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Team extractTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setTeamId(UUID.fromString(rs.getString("team_id")));
        team.setTeamName(rs.getString("team_name"));
        team.setProjectId(UUID.fromString(rs.getString("project_id")));
        team.setActive(rs.getBoolean("active"));
        return team;
    }
}