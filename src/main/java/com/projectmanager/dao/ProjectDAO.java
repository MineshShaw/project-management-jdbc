package com.projectmanager.dao;

import com.projectmanager.model.Project;
import com.projectmanager.model.enums.ProjectStatus;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectDAO {

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Project project = extractProjectFromResultSet(rs);
                projects.add(project);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving projects.");
            e.printStackTrace();
        }

        return projects;
    }

    public Project getProjectById(UUID id) {
        String query = "SELECT * FROM projects WHERE project_id = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractProjectFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving project with ID: " + id);
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertProject(Project project) {
        String query = "INSERT INTO projects (project_id, project_name, description, start_date, end_date, team_id, status, completed, progress_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, project.getProjectId().toString());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(project.getStartDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(project.getEndDate()));
            stmt.setString(6, project.getTeamId().toString());
            stmt.setString(7, project.getStatus().name());
            stmt.setBoolean(8, project.isCompleted());
            stmt.setBigDecimal(9, project.getProgressPercent());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting project: " + project.getProjectName());
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProject(Project project) {
        String query = "UPDATE projects SET project_name = ?, description = ?, start_date = ?, end_date = ?, team_id = ?, status = ?, completed = ?, progress_percent = ? WHERE project_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(project.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(project.getEndDate()));
            stmt.setString(5, project.getTeamId().toString());
            stmt.setString(6, project.getStatus().name());
            stmt.setBoolean(7, project.isCompleted());
            stmt.setBigDecimal(8, project.getProgressPercent());
            stmt.setString(9, project.getProjectId().toString());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating project: " + project.getProjectName());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProject(UUID projectId) {
        String query = "DELETE FROM projects WHERE project_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId.toString());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting project with ID: " + projectId);
            e.printStackTrace();
        }

        return false;
    }

    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(UUID.fromString(rs.getString("project_id")));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        project.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        project.setTeamId(UUID.fromString(rs.getString("team_id")));
        project.setStatus(ProjectStatus.valueOf(rs.getString("status")));
        project.setCompleted(rs.getBoolean("completed"));
        project.setProgressPercent(rs.getBigDecimal("progress_percent"));
        return project;
    }
}
