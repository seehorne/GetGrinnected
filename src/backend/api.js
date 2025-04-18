const db = require('./db_connect.js');
const express = require('express');
const fs = require('fs');
const http = require('http');
const https = require('https');

/* global var to store the servers we are running,
 * so they can be shut down when needed */
var http_server = null;
var https_server = null;

/**
 * Run the API.
 * 
 * This function will not exit on its own, 
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

  // Read our SSL credentials from the environment (loaded from dotenv)
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

  // Default route just shows the API is online
  app.get('/', getAPIOnline);

  // Getting all events takes no parameters
  app.get('/events', getEvents);

  // Getting events between, set the URL parameter names with :start and :end.
  // Those params will get passed into the function as part of `req.params` dictionary
  app.get('/events/between/:start/:end', getEventsBetween);
}

/**
 * Stop running the API.
 * 
 * It will only do something if you called run() before.
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
 * @param req Express request object
 * @param res Express response object
 */
function getAPIOnline(req, res) {
  res.send('API online!');
}

/**
 * Query the database and respond with all known events in JSON.
 * Supports querying for tags with query parameter `tag`.
 *
 * @param req  Express request object
 * @param res  Express response object
 * @param next Error handler function
 */
async function getEvents(req, res, _next) {
  // Pass any query parameters named "tag" so we can parse them into an array.
  // Could be undefined, that function will handle it.
  const tags = parseQueryTags(req.query.tag);

  // If there are no tags query the DB normally,
  // if there are tags query for them
  if (tags.length === 0) {
    const events = await db.getEvents();
    res.json(events);
    return;
  } else {
    const events = await db.getEventsWithTags(tags);
    res.json(events);
    return;
  }
}

/**
 * Query the database and respond with all events between two dates.
 * Supports querying for tags with query parameter `tag`.
 *
 * @param req  Express request object
 * @param res  Express response object
 * @param next Error handler function
 */
async function getEventsBetween(req, res, _next) {
  // Parse the required start and end parameters held in the request
  var start = parseParamDate(req.params.start);
  var end = parseParamDate(req.params.end);

  // Return descriptive error on failure to parse stard and/or end date
  //
  //   .status(400)  creates a 400 "Invalid request" HTTP error
  //   .json(...)    lets us specify what JSON object we return with the error, 
  //     so we can have a consistent error code and more detailed error messages 
  if (isNaN(start.valueOf()) && isNaN(end.valueOf())) {
    res.status(400).json({
      'error': 'Invalid date',
      'message': 'Start and end date could not be read properly.'
    });
    return;
  } else if (isNaN(start)) {
    res.status(400).json({
      'error': 'Invalid date',
      'message': 'Start date could not be read properly.'
    });
    return;
  } else if (isNaN(end)) {
    res.status(400).json({
      'error': 'Invalid date',
      'message': 'End date could not be read properly.'
    });
    return;
  }

  // Return descriptive error if start is before end
  //   .status(400)  creates a 400 "Invalid request" HTTP error
  //   .json(...)    lets us specify what JSON object we return with the error, 
  //     so we can have a consistent error code and more detailed error message
  else if (start >= end) {
    res.status(400).json({
      'error': 'Bad date order',
      'message': 'Start date must occur before end date.'
    });
    return;
  }

  // We know the start and end dates are good, so transform them into strings
  // that can be handled by the SQL side.
  // These will look like 'YYYY-MM-DD HH:MM:00.000Z'
  start = start.toISOString();
  end = end.toISOString();
  
  // If there are no tags query the DB normally,
  // if there are tags query for them
  const tags = parseQueryTags(req.query.tag);
  if (tags.length === 0) {
    const events = await db.getEventsBetween(start, end);
    res.json(events);
    return;
  } else {
    const events = await db.getEventsBetweenWithTags(start, end, tags);
    res.json(events);
    return;
  }
}

/**
 * Parse from a time URL parameter to a Unix timestamp.
 *
 * @param timeParam String representing a date.
 * @returns Date object representing the date. It may be an invalid date, in
 *          which case calling .valueOf() on it will return NaN.
 */
function parseParamDate(paramDate) {
  // these are the three features we care about in our date format.
  // "\\d" goes to the string \d, which means any digit in regex.
  const validDate = "\\d\\d\\d\\d-\\d\\d-\\d\\d"  // YYYY-MM-DD
  const validTime = "T\\d\\d:\\d\\d"              // THH:MM, no seconds
  const validTimeZone = "[+-]\\d\\d\\d\\d"        // Timezone, eg -0500 or +1230

  // MATCHES: YYYY-MM-DDTHH:MM-ZZZZ or YYYY-MM-DDTHH:MM+ZZZZ
  // CHANGES: nothing
  const allFeaturesRegex = new RegExp(
    '^' +                                    // matches start of string
    validDate + validTime + validTimeZone +  // main body of regex
    '$'                                      // matches end of string
  );
  if (allFeaturesRegex.test(paramDate)) {
    return new Date(paramDate);
  }

  // MATCHES: YYYY-MM-DDTHH:MM
  // CHANGES: adds default timezone of UTC-5 (grinnell)
  const dateTimeRegex = new RegExp('^' + validDate + validTime + '$');
  if (dateTimeRegex.test(paramDate)) {
    paramDate = paramDate.concat('-0500');
    return new Date(paramDate);
  }

  // MATCHES: YYYY-MM-DD
  // CHANGES: adds default time of midnight
  //          adds default timezone of UTC-5 (grinnell)
  const dateOnlyRegex = new RegExp('^' + validDate + '$');
  if (dateOnlyRegex.test(paramDate)) {
    paramDate = paramDate.concat('T00:00-0500');
    return new Date(paramDate);
  }

  // If none of those attempts to figure out the format worked,
  // return a date that we know is definitely invalid to match the return type.
  return new Date('purposefully invalid date string');
}

/**
 * Parse from a query tag object to a well-formed list of individual tags. Tags
 * with commas will be split to support queries like `?tag=a,b,c,d`.
 * 
 * These are the possible values for queryTags to take when coming from Express:
 *   not specified      -> undefined
 *   single instance    -> a string
 *   multiple instances -> an array of strings
 *
 * @param queryTags a query tag object, which can be
 *                  either a list of strings or a string.
 * @returns an array containing each tag, or null if no tags were found.
 *                   each tag in the array will be quoted.
 */
function parseQueryTags(queryTags) {
  // Don't try and parse undefined tags (which means not provided in URL)
  if (!queryTags) {
    return [];
  }

  // If there is only one element,
  // we need to put it in an array for the next step to work
  if (!Array.isArray(queryTags)) {
    queryTags = [queryTags];
  }

  // Split any comma-separated items to make the array flat. See example.
  queryTags = queryTags        // ["a", "b,c"]
    .map((x) => x.split(','))  // -> [["a"], ["b", "c"]]
    .flat();                   // -> ["a", "b", "c"]

  // For the DB query to find tags, we must put quotation marks on each tag.
  // This is because of the way the tags are stored as JSON in the database.
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
