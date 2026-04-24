-- ============================================================
--  AURA – Automated Enrollment & Admission Portal
--  Taguig City University
--  Database Setup Script
--
--  HOW TO USE:
--  1. Open XAMPP -> Start Apache + MySQL
--  2. Open browser -> http://localhost/phpmyadmin
--  3. Click "SQL" tab
--  4. Paste this entire script and click "Go"
-- ============================================================

CREATE DATABASE IF NOT EXISTS aura_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE aura_db;

-- Users
CREATE TABLE IF NOT EXISTS users (
    id         INT          AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    full_name  VARCHAR(150) NOT NULL,
    role       ENUM('student','admin') DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admission Forms
CREATE TABLE IF NOT EXISTS admission_forms (
    id                   INT  AUTO_INCREMENT PRIMARY KEY,
    user_id              INT  NOT NULL,
    last_name            VARCHAR(80)  NOT NULL,
    first_name           VARCHAR(80)  NOT NULL,
    middle_name          VARCHAR(80),
    birthdate            VARCHAR(20),
    birthplace           VARCHAR(150),
    gender               VARCHAR(20),
    civil_status         VARCHAR(30)  DEFAULT 'Single',
    nationality          VARCHAR(50)  DEFAULT 'Filipino',
    religion             VARCHAR(80),
    address              TEXT,
    contact_number       VARCHAR(20),
    email                VARCHAR(100),
    program              VARCHAR(150),
    previous_school      VARCHAR(150),
    previous_strand      VARCHAR(100),
    school_year          VARCHAR(20),
    guardian_name        VARCHAR(150),
    guardian_contact     VARCHAR(20),
    status               ENUM('Pending','Under Review','Accepted','Rejected') DEFAULT 'Pending',
    admin_remarks        TEXT,
    submitted_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Requirements
CREATE TABLE IF NOT EXISTS requirements (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    user_id               INT NOT NULL,
    req_form138           TINYINT(1) DEFAULT 0,
    req_good_moral        TINYINT(1) DEFAULT 0,
    req_birth_cert        TINYINT(1) DEFAULT 0,
    req_id_photo          TINYINT(1) DEFAULT 0,
    req_medical_cert      TINYINT(1) DEFAULT 0,
    req_transcript        TINYINT(1) DEFAULT 0,
    req_honorable_dismissal TINYINT(1) DEFAULT 0,
    file_form138             VARCHAR(500),
    file_good_moral          VARCHAR(500),
    file_birth_cert          VARCHAR(500),
    file_id_photo            VARCHAR(500),
    file_medical_cert        VARCHAR(500),
    file_transcript          VARCHAR(500),
    file_honorable_dismissal VARCHAR(500),
    remarks               TEXT,
    submitted_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- For users who already created the table without file columns, run these:
-- ALTER TABLE requirements ADD COLUMN file_form138             VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_good_moral          VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_birth_cert          VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_id_photo            VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_medical_cert        VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_transcript          VARCHAR(500);
-- ALTER TABLE requirements ADD COLUMN file_honorable_dismissal VARCHAR(500);

-- Subjects (course catalog)
CREATE TABLE IF NOT EXISTS subjects (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE,
    title       VARCHAR(150) NOT NULL,
    units       INT          DEFAULT 3,
    program     VARCHAR(150),
    year_level  VARCHAR(20)  DEFAULT '1st Year',
    semester    VARCHAR(20)  DEFAULT '1st Sem',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollments
CREATE TABLE IF NOT EXISTS enrollments (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    subject_id   INT NOT NULL,
    school_year  VARCHAR(20),
    semester     VARCHAR(20),
    enrolled_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uniq_user_subject (user_id, subject_id),
    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Default Admin Account (Username: admin  Password: admin123)
INSERT INTO users (username, password, email, full_name, role)
VALUES ('admin', 'admin123', 'admin@tcu.edu.ph', 'System Administrator', 'admin')
ON DUPLICATE KEY UPDATE id = id;

-- Seed Subjects (sample catalog)
INSERT INTO subjects (code, title, units, program, year_level, semester) VALUES
 ('IT101','Introduction to Computing',         3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('IT102','Computer Programming 1',            3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('GE101','Understanding the Self',            3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('GE102','Purposive Communication',           3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('MATH101','Mathematics in the Modern World', 3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('PE101','Physical Fitness',                  2,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('NSTP101','NSTP 1',                          3,'Bachelor of Science in Information Technology','1st Year','1st Sem'),
 ('CS101','Programming Fundamentals',          3,'Bachelor of Science in Computer Science',      '1st Year','1st Sem'),
 ('CS102','Discrete Mathematics',              3,'Bachelor of Science in Computer Science',      '1st Year','1st Sem'),
 ('BA101','Principles of Management',          3,'Bachelor of Science in Business Administration','1st Year','1st Sem')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
--  END OF SCRIPT
-- ============================================================
