const db = require('./db_connect.js');
const express = require('express');
const fs = require('fs');
const http = require('http');
const https = require('https');

/* global var to store if we are running a server */
var http_server = null;
var https_server = null;

/**
 * Run the API.
 */
function run() {
  // Load environment vars from .env file
  require('dotenv').config()

  // Create the app that we will serve
  const app = express();
  app.use(express.json());

  // Create a regular HTTP server.
  // Default to 8080 so non-root users can run it.
  const http_port = Number(process.env.HTTP_PORT) || 8080;
  http_server = http.createServer(app).listen(http_port, () => {
    console.log(`http server listening on port  ${http_port}`);
  });

  // Read SSL credentials from their system files
  const credentials = {
    key: process.env.SSL_KEY,
    ca: process.env.SSL_CA,
    cert: process.env.SSL_CERT,
  };

  // Create an HTTPS server serving the application.
  // Default to 4443 so non-root users can run it.
  const https_port = Number(process.env.HTTPS_PORT) || 4443;
  https_server = https
    .createServer(credentials, app)
    .listen(https_port, () => {
      console.log(`https server listening on port ${https_port}`);
    });

  // Define routes
  app.get('/', getAPIOnline);
  app.get('/events', getEvents);
  app.get('/events/between/:start/:end', getEventsBetween);
}

/**
 * Stop running the API. Uses the global `server` var set by run()
 */
function close() {
  // close http server
  if (http_server != null) {
    http_server.close((err) => {
      if (err) {
        console.log(`http server closed with status ${err}`);
      } else {
        console.log(`http server closed with no error`);
      }
    });
  }

  // close https server
  if (https_server != null) {
    https_server.close((err) => {
      if (err) {
        console.log(`https server closed with status ${err}`);
      } else {
        console.log(`https server closed with no error`);
      }
    });
  }
}

/**
 * Just respond that the API is online.
 *
 * \param req Express request object
 * \param res Express response object
 */
function getAPIOnline(req, res) {
  res.send('API online!');
}

/**
 * Query the database and respond with all known events in JSON.
 * Supports querying for tags with query parameter `tag`.
 *
 * \param req  Express request object
 * \param res  Express response object
 * \param next Error handler function
 */
async function getEvents(req, res, next) {
  // Determine if the user wants to query specific tags
  const tags = parseQueryTags(req.query.tag);

  // Query the DB, including tags if those were provided
  if (tags.length === 0) { 
    const events = await db.getEvents();
    res.json(events);
    return;
  } else {
    const events = await db.getEventsWithTags(tags);
    res.json(events);
    return;
  }
  res.json(events);
}

/**
 * Query the database and respond with all events between two dates.
 * Supports querying for tags with query parameter `tag`.
 *
 * \param req  Express request object
 * \param res  Express response object
 * \param next Error handler function
 */
async function getEventsBetween(req, res, next) {
  // Try to parse the start and end times, fail if it does not work.
  const start = parseParamDate(req.params.start);
  const end = parseParamDate(req.params.end);
  if (isNaN(start)) {
    res.status(400).json({
      'message': 'Expected start time to match format YYYY-MM-DD(THH:MM(z))',
      'start': req.params.start,
    });
    return;
  } else if (isNaN(end)) {
    res.status(400).json({
      'message': 'Expected end time to match format YYYY-MM-DD(THH:MM(z))',
      'end': req.params.end,
    });
    return;
  } else if (start >= end) {
    res.status(400).json({
      'message': 'Start time must be before end time',
      'start': req.params.start,
      'end': req.params.end,
    });
    return;
  }

  // If tags are not requested, simply query db.
  const tags = parseQueryTags(req.query.tag);
  if (tags == null) { 
    const events = await db.getEventsBetween(start, end);
    res.json(events);
    return;
  }

  // If tags have been provided query the db for them.
  const events = await db.getEventsBetweenWithTags(start, end, tags);
  if (!events) {
    res.status(404).json({ 'message': 'No events found with tags', 'tags': tags });
    return;
  } 
  res.json(events);
}

/**
 * Parse from a time URL parameter to a Unix timestamp.
 *
 * \param timeParam String representing a date.
 * \returns Unix time equivalent if parseable, NaN if not.
 */
function parseParamDate(paramDate) {
  // these are the three features we care about in our date format
  const validDate = "\\d\\d\\d\\d-\\d\\d-\\d\\d"  // YYYY-MM-DD
  const validTime = "T\\d\\d:\\d\\d"              // THH:MM, no seconds
  const validTimeZone = "[+-]\\d\\d\\d\\d"        // Timezone, eg -0500 or +1230

  // If all features are specified, use that and respect the date and timezone provided
  // YYYY-MM-DDTHH:MM-0000
  const allFeaturesRegex = new RegExp('^' + validDate + validTime + validTimeZone + '$');
  if (allFeaturesRegex.test(paramDate)) {
    return Math.floor(Date.parse(paramDate) / 1000);
  }

  // If no timezone is specified, assume they mean Grinnell time (UTC-5)
  // YYYY-MM-DDTHH:MM
  const dateTimeRegex = new RegExp('^' + validDate + validTime + '$');
  if (dateTimeRegex.test(paramDate)) {
    paramDate = paramDate.concat('-0500');
    return Math.floor(Date.parse(paramDate) / 1000);
  }

  // If no timezone OR time is specified, assume they mean midnight at grinnell time
  // YYYY-MM-DD
  const dateOnlyRegex = new RegExp('^' + validDate + '$');
  if (dateOnlyRegex.test(paramDate)) {
    paramDate = paramDate.concat('T00:00-0500');
    return Math.floor(Date.parse(paramDate) / 1000);
  }

  // If none of those attempts to figure out the format worked, bypass Date.parse and return failure.
  return NaN;
}

/**
 * Parse from a query tag object to a well-formed list of individual tags. Tags
 * with commas will be split to support queries like `?tag=a,b,c,d`.
 *
 * \param queryTags a query tag object, which can be
 *                  either a list of strings or a string.
 * \returns an array containing each tag, or null if no tags were found.
 *                   each tag in the array will be quoted.
 */
function parseQueryTags(queryTags) {
  // Don't try parse zero-like tags
  if (!queryTags) {
    return [];
  }

  // If there is only one tag, it will not get put in a list. fix that.
  if (!Array.isArray(queryTags)) {
    queryTags = [queryTags];
  }

  // Split any comma-separated items like a,b,c to make them part of the array.
  queryTags = queryTags.map((x) => x.split(',')).flat();

  // The final tags must have quotation marks on either side, add those.
  const tags = queryTags.map((x) => '"' + x + '"');
  return tags;
}

/*
 * Depending on context, run or export
 */
if (require.main === module) {
  run();
} else {
  module.exports = {
    close,
    getEvents,
    getEventsBetween,
    parseParamDate,
    parseQueryTags,
    run
  };
}
