-- ============================================================
--  AURA – Automated Enrollment & Admission Portal
--  Taguig City University
--  Database Setup Script
--
--  HOW TO USE:
--  1. Open XAMPP → Start Apache + MySQL
--  2. Open browser → http://localhost/phpmyadmin
--  3. Click "SQL" tab
--  4. Paste this entire script and click "Go"
-- ============================================================

CREATE DATABASE IF NOT EXISTS aura_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE aura_db;

-- ── Users ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         INT          AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    full_name  VARCHAR(150) NOT NULL,
    role       ENUM('student','admin') DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Admission Forms ──────────────────────────────────────────
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
    submitted_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── Requirements ─────────────────────────────────────────────
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
    remarks               TEXT,
    submitted_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── Default Admin Account ─────────────────────────────────────
-- Username: admin   Password: admin123
INSERT INTO users (username, password, email, full_name, role)
VALUES ('admin', 'admin123', 'admin@tcu.edu.ph', 'System Administrator', 'admin')
ON DUPLICATE KEY UPDATE id = id;

-- ============================================================
--  END OF SCRIPT
-- ============================================================
