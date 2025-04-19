import * as db from '../../db_connect.js';
import * as util from '../utils.mjs';

/**
 * Query the database and respond with all known events in JSON.
 * Supports querying for tags with query parameter `tag`.
 *
 * @param req  Express request object
 * @param res  Express response object
 * @param next Error handler function
 */
export async function getEvents(req, res, _next) {
  // Pass any query parameters named "tag" so we can parse them into an array.
  // Could be undefined, that function will handle it.
  const tags = util.parseQueryTags(req.query.tag);

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
export async function getEventsBetween(req, res, _next) {
  // Parse the required start and end parameters held in the request
  var start = util.parseParamDate(req.params.start);
  var end = util.parseParamDate(req.params.end);

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
  const tags = util.parseQueryTags(req.query.tag);
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