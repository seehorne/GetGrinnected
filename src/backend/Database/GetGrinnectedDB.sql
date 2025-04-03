CREATE DATABASE IF NOT EXISTS GetGrinnected;

CREATE TABLE IF NOT EXISTS events (
    eventid INT AUTO_INCREMENT PRIMARY KEY,
    event_name TEXT,
    event_description TEXT,
    event_location TEXT,
    organizations JSON,
    rsvp INT,
    event_date TIME,
    event_time TEXT,
    event_all_day INT,
    event_start_time DATETIME,
    event_end_time DATETIME,
    tags JSON,
    event_private INT,
    repeats INT,
    event_image LONGBLOB,
    is_draft INT
);

CREATE TABLE IF NOT EXISTS accounts(
    accountid INT AUTO_INCREMENT PRIMARY KEY,
    account_name TEXT,
    email TEXT,
    password TEXT,
    salt TEXT,
    profile_picture LONGBLOB,
    favorited_events JSON,
    favorited_orgs JSON,
    drafted_events JSON,
    favorited_tags JSON,
    account_description TEXT,
    account_role INT
);
