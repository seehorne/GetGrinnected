-- phpMyAdmin SQL Dump
-- version 5.2.1-1.el9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 14, 2025 at 06:47 PM
-- Server version: 9.2.0
-- PHP Version: 8.0.30
--
-- goofed upon to make it workable for CI by almond

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `GetGrinnected`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `accountid` int NOT NULL,
  `account_name` text,
  `email` text,
  `password` text,
  `profile_picture` longblob,
  `favorited_events` json DEFAULT NULL,
  `favorited_orgs` json DEFAULT NULL,
  `drafted_events` json DEFAULT NULL,
  `favorited_tags` json DEFAULT NULL,
  `account_description` text,
  `account_role` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`accountid`, `account_name`, `email`, `password`, `profile_picture`, `favorited_events`, `favorited_orgs`, `drafted_events`, `favorited_tags`, `account_description`, `account_role`) VALUES
(1, 'test_account', 'email@example.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE `events` (
  `eventid` int NOT NULL,
  `event_name` text,
  `event_description` text,
  `event_location` text,
  `organizations` json DEFAULT NULL,
  `rsvp` int DEFAULT NULL,
  `event_date` text,
  `event_time` text,
  `event_all_day` int DEFAULT NULL,
  `event_start_time` datetime DEFAULT NULL,
  `event_end_time` datetime DEFAULT NULL,
  `tags` json DEFAULT NULL,
  `event_private` int DEFAULT NULL,
  `repeats` int DEFAULT NULL,
  `event_image` longblob,
  `is_draft` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`eventid`, `event_name`, `event_description`, `event_location`, `organizations`, `rsvp`, `event_date`, `event_time`, `event_all_day`, `event_start_time`, `event_end_time`, `tags`, `event_private`, `repeats`, `event_image`, `is_draft`) VALUES
(1, 'one', 'uno yksi jedna um vienas une', 'nowhere', '[\"One Person\"]', 0, 'May 30', '1 p.m. - 5 p.m.', 0, '2025-05-30 18:00:00', '2025-05-30 22:00:00', '[\"one\", \"event\"]', 0, 0, NULL, 0),
(2, 'two', 'dos du dy tveir dois dau', 'everywhere', '[\"Two\", \"People\"]', 0, 'May 31', '3 p.m. - 4 p.m.', 0, '2025-05-31 20:00:00', '2025-05-31 21:00:00', '[\"two\", \"event\"]', 0, 0, NULL, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`accountid`);

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`eventid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `accountid` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `events`
--
ALTER TABLE `events`
  MODIFY `eventid` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30951;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
