package com.projectmanager.app;

import com.projectmanager.dao.*;
import com.projectmanager.model.*;
import com.projectmanager.model.enums.*;
import com.projectmanager.util.DBConnectionUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.math.*;

public class App {
    public static void main(String[] args) {
        try {
            // Test connection
            System.out.println("Testing DB Connection...");
            DBConnectionUtil.getConnection();
            System.out.println("✅ Connection successful!");

            // 1. Insert a new project
            ProjectDAO projectDAO = new ProjectDAO();
            UUID projectId = UUID.randomUUID();
            Project project = new Project(
                projectId,
                "Test Project",
                "Demo project for testing",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                null,
                ProjectStatus.NOT_STARTED,
                false,
                BigDecimal.ZERO
            );
            projectDAO.insertProject(project);
            System.out.println("✅ Project inserted");

            // 2. Fetch the project
            Project fetchedProject = projectDAO.getProjectById(projectId);
            System.out.println("📄 Retrieved project: " + fetchedProject.getProjectName());

            // 3. Insert a task under the project
            TaskDAO taskDAO = new TaskDAO();
            UUID taskId = UUID.randomUUID();
            Task task = new Task(
                taskId,
                projectId,
                "Sample Task",
                "This is a test task.",
                LocalDateTime.now().plusDays(3),
                TaskStatus.TODO,
                false
            );
            taskDAO.insertTask(task);
            System.out.println("✅ Task inserted");

            // 4. Query all tasks under project
            System.out.println("📋 All tasks:");
            var tasks = taskDAO.getTasksByProjectId(projectId);
            tasks.forEach(t -> System.out.println(" - " + t.getTitle()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
