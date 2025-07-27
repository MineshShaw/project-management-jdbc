package com.projectmanager;

import com.projectmanager.dao.*;
import com.projectmanager.model.*;
import com.projectmanager.model.enums.*;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.Connection;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter database password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBConnectionUtil.getConnection(password)) {

            System.out.println("✅ Connected to database!");

            ProjectDAO projectDAO = new ProjectDAO(conn);
            TaskDAO taskDAO = new TaskDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            TaskCommentDAO taskCommentDAO = new TaskCommentDAO(conn);
            TaskDependencyDAO taskDependencyDAO = new TaskDependencyDAO(conn);
            TeamDAO teamDAO = new TeamDAO(conn);
            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO(conn);
            TeamAssignmentDAO teamAssignmentDAO = new TeamAssignmentDAO(conn);

            while (true) {
                System.out.println("\n📋 Main Menu:");
                System.out.println("1. Projects");
                System.out.println("2. Tasks");
                System.out.println("3. Employees");
                System.out.println("4. Teams");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                int section = scanner.nextInt();
                scanner.nextLine();

                switch (section) {
                    case 1 -> {
                        System.out.println("\n🔧 Project Menu:");
                        System.out.println("1. Create Project");
                        System.out.println("2. View All Projects");
                        System.out.println("3. Update Project");
                        System.out.println("4. Delete Project");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter choice: ");
                        int projectChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (projectChoice) {
                            case 1 -> {
                                UUID pid = UUID.randomUUID();
                                System.out.print("Enter project name: ");
                                String name = scanner.nextLine();

                                System.out.print("Description: ");
                                String description = scanner.nextLine();

                                System.out.print("Enter team ID (or leave empty): ");
                                String teamIdStr = scanner.nextLine();

                                System.out.print("Enter start date (yyyy-MM-dd or yyyy-MM-dd HH:mm or leave blank): ");
                                String input = scanner.nextLine().trim();

                                LocalDateTime dateTime = null;

                                if (!input.isEmpty()) {
                                    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                    try {
                                        dateTime = LocalDateTime.parse(input, fullFormatter);
                                    } catch (Exception e) {
                                        try {
                                            dateTime = LocalDate.parse(input, dateOnlyFormatter).atStartOfDay();
                                        } catch (Exception ex) {
                                            System.out.println("❌ Invalid date format.");
                                        }
                                    }
                                } else {
                                    dateTime = null;
                                }

                                LocalDateTime startDate = dateTime;

                                System.out.print("Enter end date (yyyy-MM-dd or yyyy-MM-dd HH:mm or leave blank): ");
                                input = scanner.nextLine().trim();

                                if (!input.isEmpty()) {
                                    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                    try {
                                        dateTime = LocalDateTime.parse(input, fullFormatter);
                                    } catch (Exception e) {
                                        try {
                                            dateTime = LocalDate.parse(input, dateOnlyFormatter).atStartOfDay();
                                        } catch (Exception ex) {
                                            System.out.println("❌ Invalid date format.");
                                        }
                                    }
                                } else {
                                    dateTime = null;
                                }

                                LocalDateTime endDate = dateTime;

                                System.out.println("Creating project with ID: " + pid);
                                UUID teamId = teamIdStr.isEmpty() ? null : UUID.fromString(teamIdStr);
                                Project p = new Project(pid, name, description, startDate, endDate, teamId,
                                        ProjectStatus.NOT_STARTED, false, BigDecimal.ZERO);
                                projectDAO.insertProject(p);
                                System.out.println("✅ Project created");
                            }
                            case 2 -> {
                                System.out.println("📁 Projects:");
                                for (Project p : projectDAO.getAllProjects())
                                    System.out.println(
                                            p.getProjectId() + " - " + p.getProjectName() + " (" + p.getStatus() + ")");
                            }
                            case 3 -> {
                                System.out.print("Enter project ID to update: ");
                                UUID projectIdToUpdate = UUID.fromString(scanner.nextLine());

                                Project existing = projectDAO.getProjectById(projectIdToUpdate);
                                if (existing == null) {
                                    System.out.println("❌ No project found with that ID.");
                                    break;
                                }

                                System.out.println("Updating project: " + existing.getProjectName());

                                System.out.print("Project Name [" + existing.getProjectName() + "]: ");
                                String name = scanner.nextLine();
                                if (name.isEmpty())
                                    name = existing.getProjectName();

                                System.out.print("Description [" + existing.getDescription() + "]: ");
                                String desc = scanner.nextLine();
                                if (desc.isEmpty())
                                    desc = existing.getDescription();

                                System.out.print("Team ID ["
                                        + (existing.getTeamId() != null ? existing.getTeamId() : "null") + "]: ");
                                String teamStr = scanner.nextLine();
                                UUID teamId = teamStr.isEmpty() ? existing.getTeamId() : UUID.fromString(teamStr);

                                // ----- Start Date -----
                                System.out.print("Start Date ["
                                        + (existing.getStartDate() != null ? existing.getStartDate() : "null")
                                        + "] (yyyy-MM-dd or yyyy-MM-dd HH:mm): ");
                                String input = scanner.nextLine().trim();
                                LocalDateTime startDate = existing.getStartDate();
                                if (!input.isEmpty()) {
                                    try {
                                        DateTimeFormatter full = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        startDate = LocalDateTime.parse(input, full);
                                    } catch (Exception e1) {
                                        try {
                                            DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            startDate = LocalDate.parse(input, dateOnly).atStartOfDay();
                                        } catch (Exception e2) {
                                            System.out.println("❌ Invalid date. Keeping existing.");
                                        }
                                    }
                                }

                                // ----- End Date -----
                                System.out.print(
                                        "End Date [" + (existing.getEndDate() != null ? existing.getEndDate() : "null")
                                                + "] (yyyy-MM-dd or yyyy-MM-dd HH:mm): ");
                                input = scanner.nextLine().trim();
                                LocalDateTime endDate = existing.getEndDate();
                                if (!input.isEmpty()) {
                                    try {
                                        DateTimeFormatter full = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        endDate = LocalDateTime.parse(input, full);
                                    } catch (Exception e1) {
                                        try {
                                            DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            endDate = LocalDate.parse(input, dateOnly).atStartOfDay();
                                        } catch (Exception e2) {
                                            System.out.println("❌ Invalid date. Keeping existing.");
                                        }
                                    }
                                }

                                // ----- Status -----
                                System.out.print("Status [" + existing.getStatus().toDbValue()
                                        + "] (not_started/in_progress/completed/on_hold): ");
                                String statusStr = scanner.nextLine();
                                ProjectStatus status = statusStr.isEmpty() ? existing.getStatus()
                                        : ProjectStatus.fromString(statusStr);

                                // ----- Completed -----
                                System.out.print("Completed [" + existing.isCompleted() + "] (true/false): ");
                                String compStr = scanner.nextLine();
                                boolean completed = compStr.isEmpty() ? existing.isCompleted()
                                        : Boolean.parseBoolean(compStr);

                                // ----- Progress -----
                                System.out.print("Progress [" + existing.getProgressPercent() + "]: ");
                                String progStr = scanner.nextLine();
                                BigDecimal progress = progStr.isEmpty() ? existing.getProgressPercent()
                                        : new BigDecimal(progStr);

                                // Build updated project
                                Project updated = new Project(projectIdToUpdate, name, desc, startDate, endDate, teamId,
                                        status, completed, progress);

                                if (projectDAO.updateProject(updated)) {
                                    System.out.println("✅ Project updated successfully.");
                                } else {
                                    System.out.println("❌ Failed to update project.");
                                }

                            }
                            case 4 -> {
                                System.out.print("Enter project ID to delete: ");
                                UUID projectId = UUID.fromString(scanner.nextLine());
                                boolean deleted = projectDAO.deleteProject(projectId);
                                if (deleted) {
                                    System.out.println("✅ Project deleted");
                                } else {
                                    System.out.println("❌ Failed to delete project.");
                                }
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("\n📝 Task Menu:");
                        System.out.println("1. Create Task");
                        System.out.println("2. View Tasks by Project");
                        System.out.println("3. Add Comment to Task");
                        System.out.println("4. Add Task Dependency");
                        System.out.println("5. Assign Task to Employee");
                        System.out.println("6. Update Task");
                        System.out.println("7. View All Tasks");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter choice: ");
                        int taskChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (taskChoice) {
                            case 1 -> {
                                System.out.print("Enter project ID: ");
                                UUID projId = UUID.fromString(scanner.nextLine());
                                if (projId == null) {
                                    System.out.println("❌ Invalid project ID.");
                                    continue;
                                }
                                System.out.print("Enter task title: ");
                                String title = scanner.nextLine();
                                System.out.print("Enter task description: ");
                                String desc = scanner.nextLine();

                                System.out.print("Enter end date (yyyy-MM-dd or yyyy-MM-dd HH:mm or leave blank): ");
                                String input = scanner.nextLine().trim();

                                LocalDateTime dateTime = null;

                                if (!input.isEmpty()) {
                                    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                    try {
                                        dateTime = LocalDateTime.parse(input, fullFormatter);
                                    } catch (Exception e) {
                                        try {
                                            dateTime = LocalDate.parse(input, dateOnlyFormatter).atStartOfDay();
                                        } catch (Exception ex) {
                                            System.out.println("❌ Invalid date format.");
                                        }
                                    }
                                } else {
                                    dateTime = null;
                                }

                                LocalDateTime dueDate = dateTime;

                                UUID tid = UUID.randomUUID();
                                Task t = new Task(tid, projId, title, desc,
                                        dueDate, TaskStatus.TODO, false);
                                taskDAO.insertTask(t);
                                System.out.println("✅ Task created");
                            }
                            case 2 -> {
                                System.out.print("Enter project ID: ");
                                UUID pid = UUID.fromString(scanner.nextLine());
                                System.out.println("📄 Tasks:");
                                for (Task t : taskDAO.getTasksByProjectId(pid))
                                    System.out.println(" - " + t.getTitle() + " (Due: " + t.getDueDate() + ")"
                                            + " [Status: " + t.getStatus().toDbValue() + "]");
                            }
                            case 3 -> {
                                System.out.print("Enter task ID: ");
                                UUID tid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter comment: ");
                                String msg = scanner.nextLine();
                                TaskComment c = new TaskComment(tid, eid, LocalDateTime.now(), msg);
                                taskCommentDAO.insertComment(c);
                                System.out.println("✅ Comment added");
                            }
                            case 4 -> {
                                System.out.print("Enter task ID: ");
                                UUID tid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter dependency task ID: ");
                                UUID dep = UUID.fromString(scanner.nextLine());
                                TaskDependency d = new TaskDependency(tid, dep);
                                taskDependencyDAO.insertDependency(d);
                                System.out.println("✅ Dependency added");
                            }
                            case 5 -> {
                                System.out.print("Enter task ID: ");
                                UUID tid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter role: ");
                                String role = scanner.nextLine();
                                TaskAssignment ta = new TaskAssignment(tid, eid, role);
                                taskAssignmentDAO.insertAssignment(ta);
                                System.out.println("✅ Task assigned");
                            }
                            case 6 -> {
                                System.out.print("Enter task ID to update: ");
                                UUID taskIdToUpdate = UUID.fromString(scanner.nextLine());

                                Task existingTask = taskDAO.getTaskById(taskIdToUpdate);
                                if (existingTask == null) {
                                    System.out.println("❌ No task found with that ID.");
                                    break;
                                }

                                System.out.println("Updating task: " + existingTask.getTitle());

                                // Title
                                System.out.print("Title [" + existingTask.getTitle() + "]: ");
                                String title = scanner.nextLine();
                                if (title.isEmpty())
                                    title = existingTask.getTitle();

                                // Description
                                System.out.print("Description [" + existingTask.getDescription() + "]: ");
                                String desc = scanner.nextLine();
                                if (desc.isEmpty())
                                    desc = existingTask.getDescription();

                                // Due Date
                                System.out.print("Due Date [" + existingTask.getDueDate()
                                        + "] (yyyy-MM-dd or yyyy-MM-dd HH:mm): ");
                                String input = scanner.nextLine().trim();
                                LocalDateTime dueDate = existingTask.getDueDate();
                                if (!input.isEmpty()) {
                                    try {
                                        DateTimeFormatter full = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        dueDate = LocalDateTime.parse(input, full);
                                    } catch (Exception e1) {
                                        try {
                                            DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            dueDate = LocalDate.parse(input, dateOnly).atStartOfDay();
                                        } catch (Exception e2) {
                                            System.out.println("❌ Invalid date. Keeping existing.");
                                        }
                                    }
                                }

                                // Status
                                System.out.print("Status [" + existingTask.getStatus().toDbValue()
                                        + "] (todo/in_progress/done/blocked): ");
                                String statusStr = scanner.nextLine();
                                TaskStatus status = statusStr.isEmpty() ? existingTask.getStatus()
                                        : TaskStatus.fromString(statusStr);

                                // Completed
                                System.out.print("Completed [" + existingTask.isCompleted() + "] (true/false): ");
                                String completedStr = scanner.nextLine();
                                boolean completed = completedStr.isEmpty() ? existingTask.isCompleted()
                                        : Boolean.parseBoolean(completedStr);

                                // Rebuild updated task
                                Task updatedTask = new Task(
                                        taskIdToUpdate,
                                        existingTask.getProjectId(),
                                        title,
                                        desc,
                                        dueDate,
                                        status,
                                        completed);

                                if (taskDAO.updateTask(updatedTask)) {
                                    System.out.println("✅ Task updated successfully.");
                                } else {
                                    System.out.println("❌ Failed to update task.");
                                }

                            }
                            case 7 -> {
                                System.out.println("All Tasks:");
                                for (Task task : taskDAO.getAllTasks()) {
                                    System.out.println(task);
                                }
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("\n👤 Employee Menu:");
                        System.out.println("1. New Employee");
                        System.out.println("2. View All Employees");
                        System.out.println("3. Update Employee");
                        System.out.println("4. Delete Employee");
                        System.out.println("5. Assign Employee to Team");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter choice: ");
                        int empChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (empChoice) {
                            case 1 -> {
                                UUID eid = UUID.randomUUID();
                                System.out.print("Enter employee name: ");
                                String name = scanner.nextLine();
                                System.out.print("Enter email: ");
                                String email = scanner.nextLine();
                                System.out.print("Enter contact: ");
                                String contact = scanner.nextLine();
                                Employee emp = new Employee(eid, name, email, contact);
                                employeeDAO.insertEmployee(emp);
                                System.out.println("✅ Employee added");
                            }
                            case 2 -> {
                                System.out.println("👥 Employees:");
                                for (Employee emp : employeeDAO.getAllEmployees())
                                    System.out.println(emp.getEmployeeId() + " - " + emp.getEmployeeName() + " ("
                                            + emp.getEmployeeEmail() + ")");
                            }
                            case 3 -> {
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter new name: ");
                                String name = scanner.nextLine();
                                System.out.print("Enter new email: ");
                                String email = scanner.nextLine();
                                System.out.print("Enter new contact: ");
                                String contact = scanner.nextLine();
                                Employee emp = new Employee(eid, name, email, contact);
                                employeeDAO.updateEmployee(emp);
                                System.out.println("✅ Employee updated");
                            }
                            case 4 -> {
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                employeeDAO.deleteEmployee(eid);
                                System.out.println("✅ Employee deleted");
                            }
                            case 5 -> {
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter team ID: ");
                                UUID teamId = UUID.fromString(scanner.nextLine());
                                TeamAssignment ta = new TeamAssignment(teamId, eid);
                                teamAssignmentDAO.insertAssignment(ta);
                                System.out.println("✅ Employee assigned to team");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("\n👥 Team Menu:");
                        System.out.println("1. Create Team");
                        System.out.println("2. Assign Project to Team");
                        System.out.println("3. Assign Employee to Team");
                        System.out.println("4. View All Teams");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter choice: ");
                        int teamChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (teamChoice) {
                            case 1 -> {
                                UUID tid = UUID.randomUUID();
                                System.out.print("Enter team name: ");
                                String name = scanner.nextLine();
                                Team team = new Team(tid, name, null, true);
                                teamDAO.insertTeam(team);
                                System.out.println("✅ Team created");
                            }
                            case 2 -> {
                                System.out.print("Enter team ID: ");
                                UUID teamId = UUID.fromString(scanner.nextLine());
                                System.out.print("Enter employee ID: ");
                                UUID eid = UUID.fromString(scanner.nextLine());
                                TeamAssignment ta = new TeamAssignment(teamId, eid);
                                teamAssignmentDAO.insertAssignment(ta);
                                System.out.println("✅ Employee assigned to team");
                            }
                        }
                    }
                    case 0 -> {
                        System.out.println("👋 Exiting...");
                        return;
                    }
                    default -> System.out.println("❌ Invalid option!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
