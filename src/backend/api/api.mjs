import * as dotenv from 'dotenv'
import express from 'express';
import * as http from 'http';
import * as https from 'https';

// Import routes from their files
import * as events from './routes/events.mjs';

/* global vars to store the servers we are running,
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
  dotenv.config()

  // Create the app that we will serve
  const app = express();
  app.use(express.json());

  // Create a regular HTTP server.
  // Default to 8080 so non-root users can run it
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
  app.get('/events', events.getEvents);

  // Getting events between, set the URL parameter names with :start and :end.
  // Those params will get passed into the function as part of `req.params` dictionary
  app.get('/events/between/:start/:end', events.getEventsBetween);
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
 * @param _req  Express request object (unused)
 * @param res   Express response object - where we send our result.
 */
function getAPIOnline(_req, res) {
  res.send('API online!');
}

// run the server when we are run.
// under MJS, we do not need to specify different behavior on import
run();