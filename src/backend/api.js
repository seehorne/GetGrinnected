const db = require('./db_connect');
const otp = require('./one_time_code')
const bcrypt = require('bcrypt');
const express = require('express');
const http = require('http');
const https = require('https');
const jwt = require('jsonwebtoken');

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

  // Check if a user exists by trying to GET them by username.
  // :username gets passed as a parameter to the function, in `req.params`
  app.get('/user/:username', checkUsernameExists);

  // Login and signups will be done through POST requests, which is because you
  // have to send information and there's the metaphor of creating something new.
  app.post('/user/login', logInUser);
  app.post('/user/signup', signUpNewUser);

  // OTP code verification also through a POST request. If successful, it will
  // send back the needed authentication tokens.
  app.post('/user/verify', verifyOTP);
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
 * @param _req  Express request object
 * @param res   Express response object
 */
function getAPIOnline(_req, res) {
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
 * Check if a user exists by username,
 * 
 * @param {*} req    Express request containing parameters
 * @param {*} res    Express response to send output to
 * @param {*} _next  Error handler function for async (unused)
 */
async function checkUsernameExists(req, res, _next) {
  // Query the database, then send the result.
  const result = await db.getAccount(req.params.username);
  res.json({ result: result !== undefined });
}

/**
 * Request to sign up a new user account.
 * 
 * @param {*} req  Express request. Body should contain:
 * - username to be created
 * - email to associate with that username
 * @param {*} res  Express response. Will be sent either a message
 * that the request succeeded (and a passcode has been sent) or that there
 * was an error.
 * @param {*} _next  Error handler function for async (unused)
 */
async function signUpNewUser(req, res, _next) {
  // Make sure the request provided a username. If it does not,
  // return an HTTP 400 which indicates a badly-formed request.
  const username = req.body.username;
  if (username === undefined) {
    res.status(400).json({
      'error': 'No username',
      'message': 'A username must be provided in the body of the request.'
    })
    return;
  }

  // Do the same check to make sure an email is included in the request body.
  const email = req.body.email;
  if (email === undefined) {
    res.status(400).json({
      'error': 'No email',
      'message': 'An email must be provided in the body of the request.'
    });
    return;
  }

  // Make sure the email is a grinnell email. If it does not, respond with
  // an appropriate error. 400 again.
  if (!email.endsWith('@grinnell.edu')) {
    res.status(400).json({
      'error': 'Invalid email',
      'message': 'Email must end with @grinnell.edu.'
    })
  }

  // Make sure the username provided doesn't break any format rules.
  // If not, return HTTP 400 again.
  const valid = validateUsername(username);
  if (!valid.result) {
    // reuse the reason returned by validateUsername if the check fails,
    // so we can have a more descriptive error
    res.status(400).json({
      'error': 'Invalid username',
      'message': valid.reason
    })
    return;
  }

  // Make sure that user doesn't already exist.
  // If they do, we can't sign them up--they need to login.
  if (await db.getAccount(username)) {
    res.status(400).json({
      'error': 'Username exists',
      'message': `A user with username ${username} already exists.`
    });
    return;
  }

  // Having passed all the checks, we can now create that user in the database
  await db.createAccount(username, email);

  // With the account created, send them an email.
  await sendOTP(email);

  // Respond with success-- account created!
  res.status(201).json({
    'message': 'Account created, and OTP successfully sent.'
  });
}

async function sendOTP(email) {
  // Send the user a one-time code by email.
  const code = otp.sendCode(email);

  // Salt and hash the code before we store it
  const saltRounds = 10;  // generally-accepted number of rounds
  const salt = await bcrypt.genSalt(saltRounds);
  const hashedCode = await bcrypt.hash(code, salt);

  // TODO: STORE (email, hashed code, current time) TO CHECK LATER
}

/**
 * Request to log in an existing user account.
 * 
 * @param {*} req    Express request. Body should contain email to log into.
 * @param {*} res    Express response. Will be send success or failure.
 * @param {*} _next  Express error handler, not used.
 */
async function logInUser(req, res, _next) {
  // Make sure email is included in the body
  const email = req.body.email;
  if (email === undefined) {
    res.status(400).json({
      'error': 'No email',
      'message': 'An email must be provided in the body of the request.'
    })
    return;
  }
  
  // Make sure the user already exists. If it does not, return a HTTP 404 error.
  // That signifies "resource not found", which is appropriate here.
  if (!await db.getAccountByEmail(email)) {
    res.status(404).json({
      'error': 'No such user',
      'message': 'No user account exists with that email.'
    });
    return;
  }

  // Respond with success-- OTP sent!
  res.status(200).json({
    'message': 'OTP successfully sent.'
  });
}

/**
 * Verify an OTP code.
 * 
 * @param {*} req  Express request. Body should contain email logging into,
 * as well as otp code being verified.
 * @param {*} res  Express response, will be sent success or failure.
 * @param {*} _next  Express error handler, not used.
 */
async function verifyOTP(req, res, _next) {
  // Get the email and OTP sent from the body,
  // and make sure they were actually sent.
  const email = req.body.email;
  const code = req.body.code;
  if (email === undefined) {
    res.status(400).json({
      'error': 'No email',
      'message': 'Request body must contain email.'
    });
    return;
  }
  if (code === undefined) {
    res.status(400).json({
      'error': 'No code',
      'message': 'Request body must contain code.'
    });
    return;
  }

  console.log(`got OTP verify request from ${email} with code ${code}`);

  // // TODO: CHECK CODE AGAINST LOCAL STORAGE, RETURN BAD OTP IF NOT
  // res.status(400).json({
  //   'error': 'Bad code',
  //   'message': 'Could not verify OTP.'
  // });

  // Use JSON Web Tokens to create two tokens for the user,
  // a long-lived refresh token and a short-lived access token.
  const refreshToken = jwt.sign(
    { email },
    process.env.REFRESH_TOKEN_SECRET,
    { expiresIn: '30d' }
  );
  const accessToken = jwt.sign(
    { email },
    process.env.ACCESS_TOKEN_SECRET,
    { expiresIn: '15m' }
  );

  // Send those tokens back to the user in a successful response.
  res.json({
    'message': 'Successfully authenticated',
    'refresh_token': refreshToken,
    'access_token': accessToken
  });
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

/**
 * Determine whether a username is valid.
 * 
 * @param {string} username  The username to check.
 * @returns  An object with these keys:
 * - result: true if username is valid, false if not
 * - reason: if result is false, the reason why.
 */
function validateUsername(username) {
  // Make sure the username is within the right length (8-20 chars)
  const minLength = 8;
  const maxLength = 20;
  if (username.length < minLength || username.length > maxLength) {
    return {
      result: false,
      reason: `Username must be ${minLength}-${maxLength} characters long.`
    };
  }

  // Count the letters in the username.
  //   the `|| []` is because a failed match returns null, but
  //   we need the result object to have a length of 0.
  const letters = username.match(/[A-Za-z]/g) || [];

  // If there are too few letters, the username is invalid.
  const minLetters = 5;
  if (letters.length < minLetters) {
    return {
      result: false,
      reason: `Username must contain at least ${minLetters} letters.`
    };
  }

  // Construct a regular expression that checks if every char is allowed.
  const allowedChars = "A-Za-z0-9._"
  const allowedCharsRegex = new RegExp(
    "^[" +          // must start at start of string
    allowedChars +  // only allow characters in our allowed list
    "]+$"           // must go all the way to end of string
  );

  // If that regular expression does not match, return the username is invalid
  if (!allowedCharsRegex.test(username)) {
    return {
      result: false,
      reason: `Username may only contain characters in the set [${allowedChars}].`
    };
  }

  // Make sure username doesn't start or end with _ or .
  if (username.startsWith('.') || username.startsWith('_')
    || username.endsWith('.') || username.endsWith('_')) {
    return {
      result: false,
      reason: 'Username must not start or end with . or _'
    };
  }

  // Make sure username doesn't contain doubles of any separators
  if (username.includes('..') || username.includes('__')
    || username.includes('._') || username.includes('_.')) {
    return {
      result: false,
      reason: 'Username must not contain .., __, ._, or _.'
    };
  }

  // After all these checks, we can be confident the username is valid
  return {
    result: true,
    reason: ''
  };
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
