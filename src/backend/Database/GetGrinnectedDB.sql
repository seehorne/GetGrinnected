CREATE DATABASE IF NOT EXISTS getgrinnecteddb;

CREATE TABLE IF NOT EXISTS events(
    eventid INTEGER AUTO_INCREMENT PRIMARY KEY,
    event_name TEXT,
    event_description TEXT,
    event_location TEXT,
    organizations VARCHAR(MAX),
    rsvp INTEGER,
    event_date DATE,
    event_time TIME,
    tags VARCHAR(MAX),
    event_private INTEGER,
    repeats INTEGER,
    event_image IMAGE,
    is_draft INTEGER
);

CREATE TABLE IF NOT EXISTS accounts(
    accountid INTEGER AUTO_INCREMENT PRIMARY KEY,
    account_name TEXT,
    email TEXT,
    password TEXT,
    salt TEXT,
    profile_picture IMAGE,
    favorited_events VARCHAR(MAX),
    favorited_orgs VARCHAR(MAX),
    drafted_events VARCHAR(MAX),
    favorited_tags VARCHAR(MAX),
    account_description TEXT,
    account_role INTEGER
);
