-- phpMyAdmin SQL Dump
-- version 5.2.1-1.el9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 14, 2025 at 06:47 PM
-- Server version: 9.2.0
-- PHP Version: 8.0.30
--
-- modified for CI purposes by almond

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

-- almond addition: create the DB itself.
CREATE DATABASE IF NOT EXISTS GetGrinnectedCI;
USE GetGrinnectedCI;

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
  `account_role` int DEFAULT NULL,
  `notified_events` json DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `accounts`
--

-- create two fake accounts, both under grinnell emails that do not exist
INSERT INTO `accounts` (`accountid`, `account_name`, `email`, `password`, `profile_picture`, `favorited_events`, `favorited_orgs`, `drafted_events`, `favorited_tags`, `account_description`, `account_role`, `notified_events`) VALUES
(1, 'test_account', 'example1234567890@grinnell.edu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'demo_account', 'getgrinnected.demo@grinnell.edu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

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

-- Fake event data. we make events "one", "two" and "three"
INSERT INTO `events` (`eventid`, `event_name`, `event_description`, `event_location`, `organizations`, `rsvp`, `event_date`, `event_time`, `event_all_day`, `event_start_time`, `event_end_time`, `tags`, `event_private`, `repeats`, `event_image`, `is_draft`) VALUES
(1, 'one', 'fake description for event 1', 'nowhere', '[\"One Person\"]', 0, 'May 30', '1 p.m. - 5 p.m.', 0, '2025-05-30 18:00:00', '2025-05-30 22:00:00', '[\"one\", \"odd\"]', 0, 0, NULL, 0),
(2, 'two', 'fake description for event 2', 'everywhere', '[\"Two\", \"People\"]', 0, 'May 31', '3 p.m. - 4 p.m.', 0, '2025-05-31 20:00:00', '2025-05-31 21:00:00', '[\"two\", \"even\"]', 0, 0, NULL, 0),
(3, 'three', 'fake description for event 3', 'in between', '[\"Nobody\"]', 0, 'May 31', '2 p.m. - 3 p.m.', 0, '2025-05-31 19:00:00', '2025-05-31 20:00:00', '[\"three\", \"odd\"]', 0, 0, NULL, 0);


-- these were here in the original dump, idk why it's not part of the
-- main table declaration but it seems to work okay.
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`accountid`);
ALTER TABLE `events`
  ADD PRIMARY KEY (`eventid`);
ALTER TABLE `accounts`
  MODIFY `accountid` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
ALTER TABLE `events`
  MODIFY `eventid` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30951;

-- this makes sure we save all the changes that were made.
COMMIT;

-- not sure if these are comments or not, I figure it's best not to touch them.
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;