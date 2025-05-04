const jwt = require('jsonwebtoken');
const db = require('../../db_connect.js');
const util = require('../utils.cjs');
const DBPATH = './src/backend/Database/localOTP.db'

/**
 * Check if a user exists by username. This route is protected, and
 * you must prove you are that user in order to use it.
 * 
 * @param {*} req    Express request containing parameters
 * @param {*} res    Express response to send output to
 * @param {*} _next  Error handler function for async (unused)
 */
async function routeGetUserData(req, res, _next) {
  // Take the email from the request, which will be loaded by the middleware.
  const email = req.email;

  // Query the DB for the account corresponding to that email.
  // Since the middleware checks if the account exists, we can just return the data.
  const account = await db.getAccountByEmail(email);
  res.json(account);
}

/**
 * Resend an OTP code to a user.
 * 
 * @param {*} req  Express request. Body should contain email.
 * @param {*} res  Express response. Will be sent status of the send.
 * @param {*} _next  Express error handler for async, unused.
 */
async function routeSendOTP(req, res, _next) {
  // Get email from the body, we know it is there thanks to the middleware
  const email = req.body.email;

  // Make sure the user already exists. If it does not, return a HTTP 404 error.
  // That signifies "resource not found", which is appropriate here.
  if (!await db.getAccountByEmail(email)) {
    res.status(404).json({
      'error': 'No such user',
      'message': 'No user account exists with that email.'
    });
    return;
  }

  // Send that user an OTP code, then respond we did it successfully!
  // TODO: we may want sendOTP to return error and deal with that.
  await util.sendOTP(email);
  res.status(200).json({
    'message': 'OTP successfully sent.'
  });
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
async function routeSignUpNewUser(req, res, _next) {
  // Get username and email from the request, which we can assume exists thanks to the middleware
  const username = req.body.username;
  const email = req.body.email;

  // Make sure the email is a grinnell email. If it does not, respond with
  // an appropriate error. 400 for "bad request"
  if (!email.endsWith('@grinnell.edu')) {
    res.status(400).json({
      'error': 'Invalid email',
      'message': 'Email must end with @grinnell.edu.'
    });
    return;
  }

  // Make sure the username provided doesn't break any format rules.
  // If not, return HTTP 400 again.
  const valid = util.validateUsername(username);
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

  // Also make sure no user with that email exists. 
  // We don't want one person to have two accounts.
  if (await db.getAccountByEmail(email)) {
    res.status(400).json({
      'error': 'Email exists',
      'message': 'A user with that email already exists.'
    });
    return;
  }

  // Having passed all the checks, we can now create that user in the database
  await db.createAccount(username, email);

  // With the account created, send them an email.
  // TODO: ERROR HANDLING WOULD APPLY HERE TOO IF USED.
  await util.sendOTP(email);

  // Respond with success-- account created!
  res.status(201).json({
    'message': 'Account created, and OTP successfully sent.'
  });
}

/**
 * Generate new user tokens for a specific email address.
 * 
 * @param {string} email  Email to generate the tokens for.
 * @returns  An object with two keys:
 * - refresh for the user's refresh token
 * - access for the user's access token
 */
function generateUserTokens(email) {
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

  return { 'refresh': refreshToken, 'access': accessToken };
}

/**
 * Verify an OTP code.
 * 
 * @param {*} req  Express request. Body should contain email logging into,
 * as well as otp code being verified.
 * @param {*} res  Express response, will be sent success or failure.
 * @param {*} _next  Express error handler, not used.
 */
async function routeVerifyOTP(req, res, _next) {
  // Get the email and OTP sent from the body,
  // we can assume they exist thanks to the middleware.
  const email = req.body.email;
  const code = req.body.code;

  // Check the OTP code the user entered against the codes we have stored.
  // If it does not match (wrong code or expired), return the same error with
  // HTTP status 400.
  validCode = await util.otpFileCheck(DBPATH, email, code)
  if (!validCode) {
    res.status(400).json({
      'error': 'Bad code',
      'message': 'Could not verify OTP.'
    });
    return;
  } else {
    // Generate tokens for that user
    const tokens = generateUserTokens(email);

    // Send those tokens back to the user in a successful response.
    res.json({
      'message': 'Successfully authenticated',
      'refresh_token': tokens.refresh,
      'access_token': tokens.access
    });
    return;
  }
}

/**
 * Get the favorited events of the user you are logged in as.
 * 
 * @param {*} req  Express request.
 * @param {*} res  Express response.
 * @param {*} _next  Next callback function, unused.
 */
async function routeGetFavorited(req, res, _next) {
  // Query the database for that user. We can assume they exist thanks to the middleware.
  const email = req.email;
  const account = await db.getAccountByEmail(email);

  // Return their favorited events array
  res.json({
    'favorited_events': account.favorited_events
  });
}

/**
 * Write updated favorite events for the user you are logged in as.
 * 
 * @param {*} req  Express request
 * @param {*} res  Express response
 * @param {*} next  Express callback function
 */
async function routePutFavorited(req, res, next) {
  // Set the favorited_events array of that user, and let that function do the response.
  // This assumes the body has `favorited_events`, and the middleware makes sure that is true
  await util.userDataSetArray('favorited_events', req, res, next);
}

/**
 * Get the notified events of the user you are logged in as.
 * 
 * @param {*} req  Express request.
 * @param {*} res  Express response.
 * @param {*} _next  Next callback function, unused.
 */
async function routeGetNotified(req, res, _next) {
  // Query the database for that user. We can assume they exist thanks to the middleware.
  const email = req.email;
  const account = await db.getAccountByEmail(email);

  // Return their notified events array
  res.json({
    'notified_events': account.notified_events
  });
}

/**
 * Write updated notified events for the user you are logged in as.
 * 
 * @param {*} req  Express request
 * @param {*} res  Express response
 * @param {*} next  Express callback function
 */

async function routePutNotified(req, res, next) {
  // Set the notified_events array of that user, and let that function do the response.
  // This requires the body contain `notified_events`, which we get thanks to the middleware
  await util.userDataSetArray('notified_events', req, res, next);
}

/**
 * Get the username events of the user you are logged in as.
 * 
 * @param {*} req  Express request.
 * @param {*} res  Express response.
 * @param {*} _next  Next callback function, unused.
 */
async function routeGetUsername(req, res, _next) {
  // Query the database for that user. We can assume they exist thanks to the middleware.
  const email = req.email;
  const account = await db.getAccountByEmail(email);

  // Return their username array
  res.json({
    'username': account.account_name
  });
}

/**
 * Write an updated username for the user you are logged in as.
 * 
 * @param {*} req  Express request
 * @param {*} res  Express response
 * @param {*} _next  Express callback function, unused
 */
async function routePutUsername(req, res, _next) {
  // Get new username from the request body, we can assume it exists thanks to the middleware.
  const newUsername = req.body.username;

  // Get the username from the request body, and make sure it's a string.
  if (typeof newUsername !== 'string') {
    res.status(400).json({
      'error': 'Invalid body',
      'message': 'Request body must be a string.'
    });
    return;
  }

  // Make sure the new username is valid, if not also return an error.
  const valid = util.validateUsername(newUsername);
  if (!valid.result) {
    // reuse the reason returned by validateUsername if the check fails,
    // so we can have a more descriptive error
    res.status(400).json({
      'error': 'Invalid username',
      'message': valid.reason
    })
    return;
  }

  // With the new username validated, set it
  const email = req.email;
  await db.modifyAccountField(email, 'account_name', newUsername);
  res.json({
    'message': 'Username successfully updated.'
  });
}

/**
 * Refresh the authorization and refresh token of a user. This route is
 * protected, so it can only be accessed by users who hold a valid refresh token.
 * 
 * @param {*} _req  Express request, unused.
 * @param {*} res  Express response, where we will send our results.
 * @param {*} _next  Express error handler callback for async, unused.
 */
async function routeRefreshTokens(req, res, _next) {
  // Get the email of the authenticated user from the request after it was
  // stored there by the JWT authenticator.
  const email = req.email;

  // Generate and send the new tokens
  const tokens = generateUserTokens(email);
  res.json({
    'message': 'Successfully refreshed',
    'refresh_token': tokens.refresh,
    'access_token': tokens.access
  });
}

if (require.main !== module) {
  module.exports = {
    routeGetFavorited,
    routeGetNotified,
    routeGetUserData,
    routeGetUsername,
    routePutFavorited,
    routePutNotified,
    routePutUsername,
    routeRefreshTokens,
    routeSendOTP,
    routeSignUpNewUser,
    routeVerifyOTP,
  };
}