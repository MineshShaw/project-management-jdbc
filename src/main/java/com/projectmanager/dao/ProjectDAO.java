package com.projectmanager.dao;

import com.projectmanager.model.Project;
import com.projectmanager.model.enums.ProjectStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectDAO {
    private Connection conn;

    public ProjectDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";

        try (
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
        try (
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

    try (
         PreparedStatement stmt = conn.prepareStatement(query)) {

        if (project.getProjectId() != null)
            stmt.setString(1, project.getProjectId().toString());
        else
            throw new IllegalArgumentException("Project ID cannot be null");

        if (project.getProjectName() != null)
            stmt.setString(2, project.getProjectName());
        else
            stmt.setNull(2, Types.VARCHAR);

        if (project.getDescription() != null)
            stmt.setString(3, project.getDescription());
        else
            stmt.setNull(3, Types.VARCHAR);

        if (project.getStartDate() != null)
            stmt.setTimestamp(4, Timestamp.valueOf(project.getStartDate()));
        else
            stmt.setNull(4, Types.TIMESTAMP);

        if (project.getEndDate() != null)
            stmt.setTimestamp(5, Timestamp.valueOf(project.getEndDate()));
        else
            stmt.setNull(5, Types.TIMESTAMP);

        if (project.getTeamId() != null)
            stmt.setString(6, project.getTeamId().toString());
        else
            stmt.setNull(6, Types.VARCHAR);

        if (project.getStatus() != null)
            stmt.setString(7, project.getStatus().name());
        else
            stmt.setNull(7, Types.VARCHAR);

        stmt.setBoolean(8, project.isCompleted());

        if (project.getProgressPercent() != null)
            stmt.setBigDecimal(9, project.getProgressPercent());
        else
            stmt.setNull(9, Types.DECIMAL);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error inserting project: " + project.getProjectName());
        e.printStackTrace();
    }

    return false;
}


    public boolean updateProject(Project project) {
        String query = "UPDATE projects SET project_name = ?, description = ?, start_date = ?, end_date = ?, team_id = ?, status = ?, completed = ?, progress_percent = ? WHERE project_id = ?";

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (project.getProjectName() != null)
                stmt.setString(2, project.getProjectName());
            else
                stmt.setNull(2, Types.VARCHAR);

            if (project.getDescription() != null)
                stmt.setString(3, project.getDescription());
            else
                stmt.setNull(3, Types.VARCHAR);

            if (project.getStartDate() != null)
                stmt.setTimestamp(4, Timestamp.valueOf(project.getStartDate()));
            else
                stmt.setNull(4, Types.TIMESTAMP);

            if (project.getEndDate() != null)
                stmt.setTimestamp(5, Timestamp.valueOf(project.getEndDate()));
            else
                stmt.setNull(5, Types.TIMESTAMP);

            if (project.getTeamId() != null)
                stmt.setString(6, project.getTeamId().toString());
            else
                stmt.setNull(6, Types.VARCHAR);

            if (project.getStatus() != null)
                stmt.setString(7, project.getStatus().name());
            else
                stmt.setNull(7, Types.VARCHAR);

            stmt.setBoolean(8, project.isCompleted());

            if (project.getProgressPercent() != null)
                stmt.setBigDecimal(9, project.getProgressPercent());
            else
                stmt.setNull(9, Types.DECIMAL);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating project: " + project.getProjectName());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProject(UUID projectId) {
        String query = "DELETE FROM projects WHERE project_id = ?";

        try (
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
        UUID projectId = UUID.fromString(rs.getString("project_id"));

        String name = rs.getString("project_name");
        String desc = rs.getString("description");

        LocalDateTime startDate = Optional.ofNullable(rs.getTimestamp("start_date"))
                                          .map(Timestamp::toLocalDateTime)
                                          .orElse(null);

        LocalDateTime endDate = Optional.ofNullable(rs.getTimestamp("end_date"))
                                        .map(Timestamp::toLocalDateTime)
                                        .orElse(null);

        UUID teamId = Optional.ofNullable(rs.getString("team_id"))
                              .map(UUID::fromString)
                              .orElse(null);

        ProjectStatus status = Optional.ofNullable(rs.getString("status"))
                                       .map(ProjectStatus::fromString)
                                       .orElse(null);

        Boolean completed = rs.getBoolean("completed");
        if (rs.wasNull()) completed = null;

        BigDecimal progress = rs.getBigDecimal("progress_percent");

        return new Project(projectId, name, desc, startDate, endDate, teamId, status, completed != null && completed, progress);
    }

}
