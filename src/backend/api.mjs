import express from 'express';
import * as http from 'http';
import * as https from 'https';
import * as dotenv from 'dotenv';

// Import routes from their respective files
import * as route_events from './api_components/routes/events.mjs';
import * as route_user from './api_components/routes/user.mjs';

/* global var to store the servers we are running,
 * so they can be shut down when needed */
var http_server = null;
var https_server = null;

/**
 * Run the API.
 * 
 * This function will not exit on its own, 
 */
export function run() {
  // Load environment vars from .env file
  dotenv.config();

  // Create the app that we will serve
  const app = express();
  app.use(express.json());

  // Create a regular HTTP server.
  // Default to 8080 so non-root users can run it.
  // If it is set to -1, do not run a HTTP server at all.
  const http_port = Number(process.env.HTTP_PORT) || 8080;
  if (http_port !== -1) {
    http_server = http.createServer(app).listen(http_port, () => {
      console.log(`http server listening on port ${http_port}`);
    });
  }

  // If HTTPS_PORT is defined (and not -1), run an HTTPS server on it. If it is not defined,
  // do not attempt to run a server.
  const https_port = Number(process.env.HTTPS_PORT) || -1;
  if (https_port !== -1) {
    // Read our SSL credentials from the environment (loaded from dotenv)
    const credentials = {
      key: process.env.SSL_KEY,
      ca: process.env.SSL_CA,
      cert: process.env.SSL_CERT,
    };

    https_server = https
      .createServer(credentials, app)
      .listen(https_port, () => {
        console.log(`https server listening on port ${https_port}`);
      });
  }

  /*
   * API routes (URLs that users can reach) get created below.
   *
   * See `api_components/routes/<NAME>.js` for routes that start with `/<NAME>`,
   * or in this file for `getAPIOnline`.
   */

  // Default route just shows the API is online
  app.get('/', getAPIOnline);

  // Getting all events takes no parameters
  app.get('/events', route_events.getEvents);

  // Getting events between, set the URL parameter names with :start and :end.
  // Those params will get passed into the function as part of `req.params` dictionary
  app.get('/events/between/:start/:end', route_events.getEventsBetween);

  // Check if a user exists by trying to GET them by username.
  // :username gets passed as a parameter to the function, in `req.params`
  app.get('/user/:username', route_user.checkUsernameExists);

  // Login and signups will be done through POST requests, which is because you
  // have to send information and there's the metaphor of creating something new.
  app.post('/user/login', route_user.logInUser);
  app.post('/user/signup', route_user.signUpNewUser);

  // OTP code verification also through a POST request. If successful, it will
  // send back the needed authentication tokens.
  app.post('/user/verify', route_user.verifyOTP);
}

/**
 * Stop running the API.
 * 
 * It will only do something if you called run() before.
 */
export function close() {
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

// start up the server when run as a script
run();
