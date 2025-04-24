const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const sqlite3 = require('sqlite3');

const db = require('../../db_connect.js');
const util = require('../utils.cjs');
const { sendCode } = require('../../one_time_code.cjs')
const DBPATH = './src/backend/Database/localOTP.db'

/**
 * Check if a user exists by username,
 * 
 * @param {*} req    Express request containing parameters
 * @param {*} res    Express response to send output to
 * @param {*} _next  Error handler function for async (unused)
 */
async function routeCheckUsernameExists(req, res, _next) {
  // Query the database, then send the result.
  const result = await db.getAccount(req.params.username);
  res.json({ result: result !== undefined });
}

/**
 * Resend an OTP code to a user.
 * 
 * @param {*} req  Express request. Body should contain email.
 * @param {*} res  Express response. Will be sent status of the send.
 * @param {*} _next  Express error handler for async, unused.
 */
async function routeSendOTP(req, res, _next) {
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
  await sendOTP(email);

  // Respond with success-- account created!
  res.status(201).json({
    'message': 'Account created, and OTP successfully sent.'
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
async function routeVerifyOTP(req, res, _next) {
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
  
  //boolean that checks if the code is right
  validCode = await util.otpFileCheck(DBPATH, email, code) 

  if (validCode){
    res.status(400).json({
      'error': 'Bad code',
      'message': 'Could not verify OTP.'
    });
    return;
  }
  else{
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
    return
  }
}



if (require.mail !== module) {
    module.exports = {
        routeVerifyOTP,
        routeSignUpNewUser,
        routeSendOTP,
        routeCheckUsernameExists,
    };
}