const db = require('../../db_connect.js');
const util = require('../utils.cjs');
const DBPATH = './src/backend/Database/localOTP.db'

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
  var valid = util.validateEmail(email);
  if (!valid.result) {
    res.status(400).json({
      'error': 'Invalid email',
      'message': valid.reason,
    });
    return;
  }

  // Make sure the username provided doesn't break any format rules.
  // If not, return HTTP 400 again.
  valid = util.validateUsername(username);
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
  // TODO: can account creation be put off until they verify? that would be nice,
  // but is enough extra work I'm not sure it would be feasible right now.
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
  const tokens = util.generateUserTokens(email);
  res.json({
    'message': 'Successfully refreshed',
    'refresh_token': tokens.refresh,
    'access_token': tokens.access
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
  // we can assume they exist thanks to the middleware.
  const email = req.body.email;
  const code = req.body.code;
  bypass = false;

  if (email.toLowerCase() == 'getgrinnected.demo@grinnell.edu') {
    //give permission to verify regardless of the input
    bypass = true;
  }

  // Check the OTP code the user entered against the codes we have stored.
  // If it does not match (wrong code or expired), return the same error with
  // HTTP status 400.
  checkResult = await util.otpFileCheck(DBPATH, email, code)
  if (!checkResult.status && !bypass) {//the code is wrong AND we don't have permission to bypass
    res.status(400).json({
      'error': 'Bad code',
      'message': 'Could not verify OTP.'
    });
    return;
  } else {//the code is wrong AND we don't have permission to bypass
    // If the user is changing their email get the database to handle that.
    if (checkResult.oldEmail !== undefined) {
      // The email that the user includes in their verify request is the new one.
      // Use that, along with the old email, to change their account.
      const newEmail = email;

      // Try to change the email, but if it has changed since previously verified
      // we need to make sure to catch that error.
      try {
        await db.changeUserEmail(checkResult.oldEmail, newEmail);
      } catch (e) {
        // Handl error if account already exists
        if (e.message === 'Account already exists with new email') {
          res.status(400).json({
            'error': 'Account exists',
            'message': 'Account already exists with new email.'
          });
          return;
        } else {
          throw(e);
        }
      }
    }

    // Generate tokens for that user
    const tokens = util.generateUserTokens(email);

    // Send those tokens back to the user in a successful response.
    res.json({
      'message': 'Successfully authenticated',
      'refresh_token': tokens.refresh,
      'access_token': tokens.access
    });
    return;
  }
}

if (require.main !== module) {
  module.exports = {
    routeSendOTP,
    routeSignUpNewUser,
    routeVerifyOTP,
    routeRefreshTokens,
  };
}