package com.projectmanager;

import com.projectmanager.dao.*;
import com.projectmanager.model.*;
import com.projectmanager.model.enums.*;
import com.projectmanager.util.DBConnectionUtil;

import java.sql.Connection;
import java.time.LocalDateTime;
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
                        System.out.print("Enter choice: ");
                        int projectChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (projectChoice) {
                            case 1 -> {
                                UUID pid = UUID.randomUUID();
                                System.out.print("Enter project name: ");
                                String name = scanner.nextLine();
                                Project p = new Project(pid, name, "Demo", LocalDateTime.now(),
                                        LocalDateTime.now().plusDays(7), null,
                                        ProjectStatus.NOT_STARTED, false, BigDecimal.ZERO);
                                projectDAO.insertProject(p);
                                System.out.println("✅ Project created");
                            }
                            case 2 -> {
                                System.out.println("📁 Projects:");
                                for (Project p : projectDAO.getAllProjects())
                                    System.out.println(p.getProjectId() + " - " + p.getProjectName() + " (" + p.getStatus() + ")");
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
                        System.out.print("Enter choice: ");
                        int taskChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (taskChoice) {
                            case 1 -> {
                                System.out.print("Enter project ID: ");
                                UUID projId = UUID.fromString(scanner.nextLine());
                                UUID tid = UUID.randomUUID();
                                System.out.print("Enter task title: ");
                                String title = scanner.nextLine();
                                Task t = new Task(tid, projId, title, "Test task",
                                        LocalDateTime.now().plusDays(3), TaskStatus.TODO, false);
                                taskDAO.insertTask(t);
                                System.out.println("✅ Task created");
                            }
                            case 2 -> {
                                System.out.print("Enter project ID: ");
                                UUID pid = UUID.fromString(scanner.nextLine());
                                System.out.println("📄 Tasks:");
                                for (Task t : taskDAO.getTasksByProjectId(pid))
                                    System.out.println(" - " + t.getTitle());
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
                        }
                    }
                    case 3 -> {
                        System.out.println("\n👤 Employee Menu:");
                        System.out.println("1. Create Employee");
                        System.out.print("Enter choice: ");
                        int empChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (empChoice == 1) {
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
                    }
                    case 4 -> {
                        System.out.println("\n👥 Team Menu:");
                        System.out.println("1. Create Team");
                        System.out.println("2. Assign Employee to Team");
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
