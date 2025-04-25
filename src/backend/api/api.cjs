const express = require('express');
const http = require('http');
const https = require('https');
const jwt = require('jsonwebtoken');

// Import routes from their files
const db = require('../db_connect.js');
const events = require('./routes/events.cjs');
const user = require('./routes/user.cjs');

/* global vars to store the servers we are running,
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
  require('dotenv').config();

  // Create the app that we will serve
  const app = express();
  app.use(express.json());

  // Read ports for http and https servers. .env loads these values.
  // The default ports happen if not specified, and both are chosen because
  // non-sudo users can create them.
  const http_port = Number(process.env.HTTP_PORT) || 8080;
  const https_port = Number(process.env.HTTPS_PORT) || 4433;

  // Start up the HTTP server unless it is disabled by setting port to -1.
  // HTTP is often used when testing locally, but not for prod.
  if (http_port === -1) {
    console.log('not starting http server');
  } else {
    http_server = http.createServer(app).listen(http_port, () => {
      console.log(`http server listening on port ${http_port}`);
    });
  }

  // Start up the HTTPS server unless it is disabled by setting port to -1.
  // HTTPS is used for prod, but it's not worth doing locally because we won't
  // be able to get keys and certs for it.
  if (https_port === -1) {
    console.log('not starting https server');
  } else {
    // Read our SSL credentials from the environment (loaded by dotenv)
    const credentials = {
      key: process.env.SSL_KEY,
      ca: process.env.SSL_CA,
      cert: process.env.SSL_CERT,
    };

    // Create an HTTPS server serving the application.
    // Default to 4443 so non-root users can run it.
    https_server = https
      .createServer(credentials, app)
      .listen(https_port, () => {
        console.log(`https server listening on port ${https_port}`);
      });
  }

  // Default route just shows the API is online.
  app.get('/', routeShowOnline);

  /*
   * EVENTS ROUTES
   *
   * TODO: when frontends implement JWTs, these routes should become protected.
   */

  // Getting all events takes no parameters
  app.get('/events', events.routeGetEvents);

  // Getting events between, set the URL parameter names with :start and :end.
  // Those params will get passed into the function as part of `req.params` dictionary
  app.get('/events/between/:start/:end', events.routeGetEventsBetween);

  /*
   * SIGN-UP / LOGIN ROUTES
   *
   * These routes are not protected, since they are how you get authorization
   * in the first place.
   */

  // Login and signups will be done through POST requests, which is because you
  // have to send information and there's the metaphor of creating something new.
  app.post('/user/login', user.routeSendOTP);
  app.post('/user/signup', user.routeSignUpNewUser);
  
  // Resend an OTP code by POSTing the email you need it sent to.
  app.post('/user/resend-otp', user.routeSendOTP);

  // OTP code verification also through a POST request. If successful, it will
  // send back the needed authentication tokens.
  app.post('/user/verify', user.routeVerifyOTP);

  /*
   * USER DATA ROUTES
   *
   * These routes are protected, since they correspond to a particular user.
   */

  // Get your own data by requesting it with a GET request.
  app.get('/user/data', middlewareVerifyJWT, user.routeGetUserData);

  // TODO: Update parts of your data (favorites, followed, etc) with POST requests.


}

/**
 * Stop running the API.
 * 
 * It will only do something if you called run() before.
 */
function close() {
  // close http server
  if (http_server !== null) {
    http_server.close((err) => {
      if (err) {
        console.log(`http server closed with ${err}`);
      } else {
        console.log(`http server closed with no error`);
      }
    });
  }

  // close https server
  if (https_server !== null) {
    https_server.close((err) => {
      if (err) {
        console.log(`https server closed with ${err}`);
      } else {
        console.log(`https server closed with no error`);
      }
    });
  }
}

/**
 * Just respond that the API is online.
 *
 * @param _req  Express request object (unused)
 * @param res   Express response object - where we send our result.
 */
function routeShowOnline(_req, res) {
  res.send('API online!');
}

/**
 * Middleware to verify that a JWT is valid before doing a request.
 * Based on https://www.slingacademy.com/article/authentication-authorization-expressjs-jwt/
 * 
 * It looks at the Bearer header included with the request, and tries to
 * verify that value as a JWT authorization token.
 * 
 * TODO: document more and better
 */
function middlewareVerifyJWT(req, res, next) {
  // Get the contents of the Bearer header.
  const token = req.get('Bearer');

  // If the token IS empty, immediately send that response.
  // Use HTTP 401 since it specifically means "Unauthorized"
  if (!token) {
    res.status(401).json({
      'error': 'Token required',
      'message': 'An authorization token is required, but it was not provided.'
    });
    return;
  }

  // Verify the token.
  // Since we provide a callback, this will run async and call next() 
  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, payload) => {
    // For any error, send back a response that says the token is invalid.
    // HTTP 403 means "forbidden".
    if (err) {
      res.status(403).json({
        'error': 'Invalid or expired token',
        'message': `The authorization token provided was not valid, \
or has expired. Please request a new token.`
      });
      return;
    }

    // Ensure that an account exists with that email. This makes sure we're okay
    // if someone deletes their accounts but continues trying to make requests.
    const email = payload.email;
    const account = await db.getAccountByEmail(email);
    if (account === undefined) {
      res.status(404).json({
        'error': 'No such user',
        'message': `You are verified with email ${email}, but no user exists with \
that address.`
      });
      return;
    }

    // Store the email from the payload for access,
    // then call `next()` to make the route using this middleware run.
    req.email = email;
    next()
  });
}

// run the server when we are run, export otherwise.
if (require.main === module) {
  run();
} else {
  module.exports = {
    run,
    close
  };
}