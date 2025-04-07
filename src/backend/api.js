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
  // Create the app that we will serve
  const app = express();
  app.use(express.json());

  // Create a regular HTTP server too. Backup.
  const http_port = process.env.HTTP_PORT || 80;
  http_server = http.createServer(app).listen(http_port, () => {
    console.log(`http server listening on port  ${http_port}`);
  });

  // Read SSL credentials from their system files
  require('dotenv').config()
  const credentials = {
    key: process.env.SSL_KEY,
    ca: process.env.SSL_CA,
    cert: process.env.SSL_CERT,
  };

  // Create an HTTPS server serving the application.
  const https_port = process.env.HTTPS_PORT || 443;
  https_server = https.createServer(credentials, app).listen(https_port, () => {
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
  if (server == null) {
    return;
  }

  server.close((err) => {
    if (err) {
      console.log(`server closed with status ${err}`)
    } else {
      console.log('server closed with no error')
    }
  });
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
  // If they don't request tags, return all known events
  const tags = parseQueryTags(req.query.tag);
  if (tags == null) {
    const events = await db.getEvents();
    res.json(events);
    return;
  }

  // If they do request tags, query the DB for them
  const events = await db.getEventsWithTags(tags);
  if (!events) {
    res.status(404).json({ 
      'message': 'No events found with tags',
      'tags': tags
    });
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
  const allFeaturesRegex = new RegExp('^' + validDate + validTime + validTimeZone + '$');
  if (allFeaturesRegex.test(paramDate)) {
    return Date.parse(paramDate);
  }

  // If no timezone is specified, assume they mean Grinnell time (UTC-5)
  const dateTimeRegex = new RegExp('^' + validDate + validTime + '$');
  if (dateTimeRegex.test(paramDate)) {
    paramDate = paramDate.concat('-0500');
    return Date.parse(paramDate);
  }

  // If no timezone OR time is specified, assume they mean midnight at grinnell time
  const dateOnlyRegex = new RegExp('^' + validDate + '$');
  if (dateOnlyRegex.test(paramDate)) {
    paramDate = paramDate.concat('T00:00-0500');
    return Date.parse(paramDate);
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
 */
function parseQueryTags(queryTags) {
  // Don't try parse zero-like tags
  if (!queryTags) {
    return null;
  }

  // If there is only one tag, it will not get put in a list. fix that.
  if (!Array.isArray(queryTags)) {
    queryTags = [queryTags];
  }

  // Split any comma-separated items like a,b,c
  const tags = queryTags.map((x) => x.split(',')).flat();
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
