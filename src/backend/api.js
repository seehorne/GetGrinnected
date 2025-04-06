const express = require('express');
const fs = require('fs');
const db = require('./db_connect.js');

/* global var to store if we are running a server */
var server = null;

/**
 * Run the API.
 */
function run() {
  // Listen on port 5844 by default
  const app = express();
  const port = process.env.PORT || 5844;
  server = app.listen(port);
  app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
  });

  // Middleware to parse JSON requests
  app.use(express.json());

  // Basic GET route just to show the API is up.
  app.get('/', getAPIOnline);

  // Event GETs
  app.get('/events', getEvents);
  app.get('/events/between/:start/:end', getEventsBetween);
}

/**
 * Stop running the API.
 */
function close() {
  server.close((err) => {
    console.log('server closed')
    if (err) {
      process.exit(err);
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
  console.log(`tags is ${JSON.stringify(tags)}`);
  if (tags == null) {
    console.log("we don't need no tags");
    const events = await db.getEvents();
    res.json(events);
  }

  // If they do request tags, query the DB for them
  // TODO: going here when not supposed to. fuckkkk
  const events = await db.getEventsWithTags(tags);
  if (!events) {
    res.status(404).json({ 
      'message': 'No events found with tags',
      'got': tags
    });
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
  const start = parseParamTime(req.params.start);
  const end = parseParamTime(req.params.end);
  if (isNaN(start)) {
    res.status(400).json({
      'message': 'Expected time to match format YYYY-MM-DD(THH:MM:SS(z))',
      'got': req.params.start
    });
  } else if (isNan(end)) {
    res.status(400).json({
      'message': 'Expected time to match format YYYY-MM-DD(THH:MM:SS(z))',
      'got': req.params.end
    });
  }

  // If tags are not requested, simply query db.
  const tags = parseQueryTags(req.query.tag);
  if (tags == null) { 
    const events = { 'name': 'fake event', 'desc': 'db get events between' }; //await db.getEventsBetween(start, end); // TODO: implement in DB
    res.json(events);
  }

  // TODO: this split is already gross, and adding more things we can filter on would make it worse.
  // we will want a better solution.
  const events = { 'name': 'fake event', 'desc': 'db get events between with tags' }; // await db.getEventsBetweenWithTags(start, end, tags); // TODO: implement in DB
  if (!events) {
    res.status(404).json({ 'message': 'No events found with tags', 'got': tags });
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
  // if time unspecified, assume Grinnell at midnight.
  const regexTimezoneAtEnd = /[+-]\d\d\d\d$/
  if (!paramDate.includes("T")) {
    paramDate = paramDate.concat('T00:00:00-0500');
  }

  // if time is specified but there is no timezone, assume Grinnell time.
  // timezones are either +#### or -####, thus the regex
  else if (!regexTimezoneAtEnd.test(paramDate)) {
    paramDate = paramDate.concat('-0500');
  }

  // let the date library parse the time now it is processed
  return Date.parse(paramDate);
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
    getAPIOnline,
    getEvents,
    getEventsBetween,
    parseParamDate,
    parseQueryTags,
    run
  };
}
