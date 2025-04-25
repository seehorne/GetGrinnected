const express = require('express');
const http = require('http');
const https = require('https');

// Import routes from their files
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

  // Default route just shows the API is online
  app.get('/', getAPIOnline);

  // Getting all events takes no parameters
  app.get('/events', events.routeGetEvents);

  // Getting events between, set the URL parameter names with :start and :end.
  // Those params will get passed into the function as part of `req.params` dictionary
  app.get('/events/between/:start/:end', events.routeGetEventsBetween);

  // Check if a user exists by trying to GET them by username.
  // :username gets passed as a parameter to the function, in `req.params`
  app.get('/user/:username', user.routeCheckUsernameExists);

  // Login and signups will be done through POST requests, which is because you
  // have to send information and there's the metaphor of creating something new.
  app.post('/user/login', user.routeSendOTP);
  app.post('/user/signup', user.routeSignUpNewUser);
  
  // Resend an OTP code by POSTing the email you need it sent to.
  app.post('/user/resend-otp', user.routeSendOTP);

  // OTP code verification also through a POST request. If successful, it will
  // send back the needed authentication tokens.
  app.post('/user/verify', user.routeVerifyOTP);
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
function getAPIOnline(_req, res) {
  res.send('API online!');
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
