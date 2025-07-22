-- schema.sql
CREATE DATABASE IF NOT EXISTS project_manager;
USE project_manager;

-- ===========================
-- Phase 1: Create Tables
-- ===========================

CREATE TABLE projects (
    project_id CHAR(36) PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATETIME,
    end_date DATETIME,
    team_id CHAR(36),
    status ENUM('not_started', 'in_progress', 'completed', 'on_hold') DEFAULT 'not_started',
    completed BOOLEAN DEFAULT FALSE,
    progress_percent DECIMAL(5,2) DEFAULT 0.00
);

CREATE TABLE tasks (
    task_id CHAR(36) PRIMARY KEY,
    project_id CHAR(36),
    title VARCHAR(255),
    description TEXT,
    due_date DATETIME,
    status ENUM('todo', 'in_progress', 'done', 'blocked') DEFAULT 'todo',
    completed BOOLEAN DEFAULT FALSE,

    INDEX idx_tasks_project_id (project_id),
    INDEX idx_tasks_due_completed (completed, due_date)
);

CREATE TABLE task_dependency (
    task_id CHAR(36),
    depends_on CHAR(36)
);

CREATE TABLE employees (
    employee_id CHAR(36) PRIMARY KEY,
    employee_name VARCHAR(255),
    employee_email VARCHAR(255) UNIQUE,
    contact VARCHAR(20)
);

CREATE TABLE employee_utilization (
    employee_id CHAR(36) PRIMARY KEY,
    task_count INT DEFAULT 0
);

CREATE TABLE task_assignment (
    employee_id CHAR(36),
    task_id CHAR(36),
    role VARCHAR(100),

    INDEX idx_task_assignment_emp (employee_id, task_id)
);

CREATE TABLE task_comments (
    task_id CHAR(36),
    employee_id CHAR(36),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment TEXT,

    INDEX idx_comments_task_time (task_id, timestamp)
);

CREATE TABLE teams (
    team_id CHAR(36) PRIMARY KEY,
    team_name VARCHAR(255),
    project_id CHAR(36),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE team_assignment (
    team_id CHAR(36),
    employee_id CHAR(36)
);

-- ===========================
-- Phase 2: Add Foreign Keys
-- ===========================

ALTER TABLE projects
ADD CONSTRAINT fk_projects_team
FOREIGN KEY (team_id) REFERENCES teams(team_id);

ALTER TABLE tasks
ADD CONSTRAINT fk_tasks_project
FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE task_dependency
ADD CONSTRAINT fk_dependency_task
FOREIGN KEY (task_id) REFERENCES tasks(task_id),
ADD CONSTRAINT fk_dependency_depends_on
FOREIGN KEY (depends_on) REFERENCES tasks(task_id);

ALTER TABLE employee_utilization
ADD CONSTRAINT fk_utilization_employee
FOREIGN KEY (employee_id) REFERENCES employees(employee_id);

ALTER TABLE task_assignment
ADD CONSTRAINT fk_task_assignment_employee
FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
ADD CONSTRAINT fk_task_assignment_task
FOREIGN KEY (task_id) REFERENCES tasks(task_id);

ALTER TABLE task_comments
ADD CONSTRAINT fk_comment_task
FOREIGN KEY (task_id) REFERENCES tasks(task_id),
ADD CONSTRAINT fk_comment_employee
FOREIGN KEY (employee_id) REFERENCES employees(employee_id);

ALTER TABLE teams
ADD CONSTRAINT fk_teams_project
FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE team_assignment
ADD CONSTRAINT fk_team_assignment_team
FOREIGN KEY (team_id) REFERENCES teams(team_id),
ADD CONSTRAINT fk_team_assignment_employee
FOREIGN KEY (employee_id) REFERENCES employees(employee_id);

-- ===========================
-- Phase 3: Add Triggers
-- ===========================

DELIMITER $$

CREATE TRIGGER trg_utilization_delete
AFTER DELETE ON task_assignment
FOR EACH ROW
BEGIN
  UPDATE employee_utilization
  SET task_count = task_count - 1
  WHERE employee_id = OLD.employee_id;
END$$

CREATE TRIGGER trg_utilization_insert
AFTER INSERT ON task_assignment
FOR EACH ROW
BEGIN
  INSERT INTO employee_utilization (employee_id, task_count)
  VALUES (NEW.employee_id, 1)
  ON DUPLICATE KEY UPDATE task_count = task_count + 1;
END$$

CREATE TRIGGER trg_decrement_utilization_on_completion
AFTER UPDATE ON tasks
FOR EACH ROW
BEGIN
  IF NEW.completed = TRUE AND OLD.completed = FALSE THEN
    UPDATE employee_utilization eu
    JOIN task_assignment ta ON ta.employee_id = eu.employee_id
    SET eu.task_count = eu.task_count - 1
    WHERE ta.task_id = NEW.task_id;
  END IF;
END$$

CREATE TRIGGER trg_update_progress_after_insert
AFTER INSERT ON tasks
FOR EACH ROW
BEGIN
  DECLARE total_tasks INT DEFAULT 0;
  DECLARE completed_tasks INT DEFAULT 0;
  DECLARE progress DECIMAL(5,2) DEFAULT 0;

  SELECT COUNT(*) INTO total_tasks FROM tasks WHERE project_id = NEW.project_id;
  SELECT COUNT(*) INTO completed_tasks FROM tasks WHERE project_id = NEW.project_id AND completed = TRUE;

  IF total_tasks > 0 THEN
    SET progress = (completed_tasks * 100.0) / total_tasks;
  END IF;

  UPDATE projects
  SET progress_percent = progress
  WHERE project_id = NEW.project_id;
END$$

CREATE TRIGGER trg_update_progress_after_delete
AFTER DELETE ON tasks
FOR EACH ROW
BEGIN
  DECLARE total_tasks INT DEFAULT 0;
  DECLARE completed_tasks INT DEFAULT 0;
  DECLARE progress DECIMAL(5,2) DEFAULT 0;

  SELECT COUNT(*) INTO total_tasks FROM tasks WHERE project_id = OLD.project_id;
  SELECT COUNT(*) INTO completed_tasks FROM tasks WHERE project_id = OLD.project_id AND completed = TRUE;

  IF total_tasks > 0 THEN
    SET progress = (completed_tasks * 100.0) / total_tasks;
  END IF;

  UPDATE projects
  SET progress_percent = progress
  WHERE project_id = OLD.project_id;
END$$

CREATE TRIGGER trg_update_progress_after_update
AFTER UPDATE ON tasks
FOR EACH ROW
BEGIN
  DECLARE total_tasks INT DEFAULT 0;
  DECLARE completed_tasks INT DEFAULT 0;
  DECLARE progress DECIMAL(5,2) DEFAULT 0;

  -- Check if completion status or project_id changed
  IF NEW.completed != OLD.completed OR NEW.project_id != OLD.project_id THEN

    -- Recalculate for NEW.project_id
    SELECT COUNT(*) INTO total_tasks FROM tasks WHERE project_id = NEW.project_id;
    SELECT COUNT(*) INTO completed_tasks FROM tasks WHERE project_id = NEW.project_id AND completed = TRUE;

    IF total_tasks > 0 THEN
      SET progress = (completed_tasks * 100.0) / total_tasks;
    END IF;

    UPDATE projects
    SET progress_percent = progress
    WHERE project_id = NEW.project_id;

    -- Also recalculate for OLD.project_id (in case task moved projects)
    IF OLD.project_id != NEW.project_id THEN
      SELECT COUNT(*) INTO total_tasks FROM tasks WHERE project_id = OLD.project_id;
      SELECT COUNT(*) INTO completed_tasks FROM tasks WHERE project_id = OLD.project_id AND completed = TRUE;

      IF total_tasks > 0 THEN
        SET progress = (completed_tasks * 100.0) / total_tasks;
      END IF;

      UPDATE projects
      SET progress_percent = progress
      WHERE project_id = OLD.project_id;
    END IF;

  END IF;
END$$

DELIMITER ;
