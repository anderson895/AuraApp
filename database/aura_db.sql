-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2026 at 12:01 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aura_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admission_forms`
--

CREATE TABLE `admission_forms` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `first_name` varchar(80) NOT NULL,
  `middle_name` varchar(80) DEFAULT NULL,
  `birthdate` varchar(20) DEFAULT NULL,
  `birthplace` varchar(150) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `civil_status` varchar(30) DEFAULT 'Single',
  `nationality` varchar(50) DEFAULT 'Filipino',
  `religion` varchar(80) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `contact_number` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `program` varchar(150) DEFAULT NULL,
  `previous_school` varchar(150) DEFAULT NULL,
  `previous_strand` varchar(100) DEFAULT NULL,
  `school_year` varchar(20) DEFAULT NULL,
  `guardian_name` varchar(150) DEFAULT NULL,
  `guardian_contact` varchar(20) DEFAULT NULL,
  `status` enum('Pending','Under Review','Accepted','Rejected') DEFAULT 'Pending',
  `admin_remarks` text DEFAULT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admission_forms`
--

INSERT INTO `admission_forms` (`id`, `user_id`, `last_name`, `first_name`, `middle_name`, `birthdate`, `birthplace`, `gender`, `civil_status`, `nationality`, `religion`, `address`, `contact_number`, `email`, `program`, `previous_school`, `previous_strand`, `school_year`, `guardian_name`, `guardian_contact`, `status`, `admin_remarks`, `submitted_at`) VALUES
(1, 2, 'padilla', 'joshua', '', '2000-01-01', '', 'Male', 'Single', 'Filipino', 'christian', 'awdaw', 'sesfesf', 'joshua@gmail.com', 'Bachelor of Science in Information Technology', 'cawd', 'esffse', '2026-2027', 'juan dela cruz', '09454545454', 'Accepted', '', '2026-04-24 21:56:35');

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `school_year` varchar(20) DEFAULT NULL,
  `semester` varchar(20) DEFAULT NULL,
  `enrolled_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `enrollments`
--

INSERT INTO `enrollments` (`id`, `user_id`, `subject_id`, `school_year`, `semester`, `enrolled_at`) VALUES
(1, 2, 8, '', '1st Sem', '2026-04-24 14:02:16'),
(2, 2, 5, '', '1st Sem', '2026-04-24 14:02:20'),
(3, 2, 6, '', '1st Sem', '2026-04-24 21:54:17');

-- --------------------------------------------------------

--
-- Table structure for table `requirements`
--

CREATE TABLE `requirements` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `req_form138` tinyint(1) DEFAULT 0,
  `req_good_moral` tinyint(1) DEFAULT 0,
  `req_birth_cert` tinyint(1) DEFAULT 0,
  `req_id_photo` tinyint(1) DEFAULT 0,
  `req_medical_cert` tinyint(1) DEFAULT 0,
  `req_transcript` tinyint(1) DEFAULT 0,
  `req_honorable_dismissal` tinyint(1) DEFAULT 0,
  `file_form138` varchar(500) DEFAULT NULL,
  `file_good_moral` varchar(500) DEFAULT NULL,
  `file_birth_cert` varchar(500) DEFAULT NULL,
  `file_id_photo` varchar(500) DEFAULT NULL,
  `file_medical_cert` varchar(500) DEFAULT NULL,
  `file_transcript` varchar(500) DEFAULT NULL,
  `file_honorable_dismissal` varchar(500) DEFAULT NULL,
  `remarks` text DEFAULT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `requirements`
--

INSERT INTO `requirements` (`id`, `user_id`, `req_form138`, `req_good_moral`, `req_birth_cert`, `req_id_photo`, `req_medical_cert`, `req_transcript`, `req_honorable_dismissal`, `file_form138`, `file_good_moral`, `file_birth_cert`, `file_id_photo`, `file_medical_cert`, `file_transcript`, `file_honorable_dismissal`, `remarks`, `submitted_at`, `updated_at`) VALUES
(1, 2, 0, 1, 0, 1, 0, 0, 0, NULL, 'uploads\\2\\req_good_moral.pdf', NULL, 'uploads\\2\\req_id_photo.png', NULL, NULL, NULL, '', '2026-04-24 14:04:25', '2026-04-24 21:59:54');

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `title` varchar(150) NOT NULL,
  `units` int(11) DEFAULT 3,
  `program` varchar(150) DEFAULT NULL,
  `year_level` varchar(20) DEFAULT '1st Year',
  `semester` varchar(20) DEFAULT '1st Sem',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`id`, `code`, `title`, `units`, `program`, `year_level`, `semester`, `created_at`) VALUES
(1, 'IT101', 'Introduction to Computing', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(2, 'IT102', 'Computer Programming 1', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(3, 'GE101', 'Understanding the Self', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(4, 'GE102', 'Purposive Communication', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(5, 'MATH101', 'Mathematics in the Modern World', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(6, 'PE101', 'Physical Fitness', 2, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(7, 'NSTP101', 'NSTP 1', 3, 'Bachelor of Science in Information Technology', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(8, 'CS101', 'Programming Fundamentals', 3, 'Bachelor of Science in Computer Science', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(9, 'CS102', 'Discrete Mathematics', 3, 'Bachelor of Science in Computer Science', '1st Year', '1st Sem', '2026-04-24 13:20:24'),
(10, 'BA101', 'Principles of Management', 3, 'Bachelor of Science in Business Administration', '1st Year', '1st Sem', '2026-04-24 13:20:24');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(150) NOT NULL,
  `role` enum('student','admin') DEFAULT 'student',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `full_name`, `role`, `created_at`) VALUES
(1, 'admin', 'admin123', 'admin@tcu.edu.ph', 'System Administrator', 'admin', '2026-04-24 13:20:24'),
(2, 'joshua', 'joshua', 'joshua@gmail.com', 'joshua padilla', 'student', '2026-04-24 14:01:36');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admission_forms`
--
ALTER TABLE `admission_forms`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uniq_user_subject` (`user_id`,`subject_id`),
  ADD KEY `subject_id` (`subject_id`);

--
-- Indexes for table `requirements`
--
ALTER TABLE `requirements`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `code` (`code`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admission_forms`
--
ALTER TABLE `admission_forms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `requirements`
--
ALTER TABLE `requirements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `subjects`
--
ALTER TABLE `subjects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admission_forms`
--
ALTER TABLE `admission_forms`
  ADD CONSTRAINT `admission_forms_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `requirements`
--
ALTER TABLE `requirements`
  ADD CONSTRAINT `requirements_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
