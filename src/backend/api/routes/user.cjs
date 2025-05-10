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
  checkResult = await util.otpFileCheck(DBPATH, email, code)
  if (!checkResult.status) {
    res.status(400).json({
      'error': 'Bad code',
      'message': 'Could not verify OTP.'
    });
    return;
  } else {
    // If the user is changing their email get the database to handle that.
    if (checkResult.oldEmail !== undefined) {
      // The email that the user includes in their verify request is the new one.
      // Use that, along with the old email, to change their account.
      const newEmail = email;
      await db.changeUserEmail(checkResult.oldEmail, newEmail);
      console.log('finished with db changed email');
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
  await util.setUserEventArray('favorited_events', req, res, next);
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
  await util.setUserEventArray('notified_events', req, res, next);
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
  const tokens = util.generateUserTokens(email);
  res.json({
    'message': 'Successfully refreshed',
    'refresh_token': tokens.refresh,
    'access_token': tokens.access
  });
}

/**
 * Delete the account of the currently logged-in user.
 * 
 * @param {*} req Express request
 * @param {*} res Express response
 * @param {*} _next Express error handler or next function, unused.
 */
async function routeDeleteAccount(req, res, _next) {
  // Get the email to delete, we know it is included thanks to the middleware.
  const email = req.email;

  // Ask the database to delete that account
  try {
    await db.deleteAccount(email);

    // Return a successful delete
    res.json({
      'message': 'Account successfully deleted.'
    });
  } catch (error) {
    // If an error happens, respond saying there was an error instead
    res.status(404).json({
      'error': 'Unknown error',
      'message': error.toString()
    });
  }
}

/**
 * Handle a user request to change their email address. This route sends an email
 * to their new address asking if they want to change it. The email will only be changed
 * if they actually send that verification code.
 * 
 * @param {*} req Express request
 * @param {*} res Express response. Will be given HTTP 202 "Accepted" on success, or errors.
 * @param {*} _next Express error handler/next callback. Unused.
 */
async function routeChangeEmail(req, res, _next) {
  const newEmail = req.body.new_email;
  const oldEmail = req.email;

  // Reject the response if the email isn't good.
  const valid = util.validateEmail(newEmail);
  if (!valid.result) {
    res.status(400).json({
      'error': 'Invalid email',
      'message': valid.reason,
    });
    return;
  }

  // OVERRIDE: Also reject the response if they are trying to change the email
  // of the demo account. We just don't allow that.
  if (
    newEmail.trim().toLowerCase() === 'getgrinnected.demo@grinnell.edu' ||
    oldEmail.trim().toLowerCase() === 'getgrinnected.demo@grinnell.edu'
  ) {
    res.status(400).json({
      'error': 'Cannot change email',
      'message': 'Cannot change to or from the email associated with the demo account.'
    });
    return;
  }

  // Send them a one-time code to that new email, keeping the old email in the DB too.
  console.log(`sending otp to new ${newEmail} old ${oldEmail}`);
  await util.sendOTP(newEmail, oldEmail);
  res.status(202).json({
    'message': 'Verification code sent to new email.'
  });
}

if (require.main !== module) {
  module.exports = {
    routeChangeEmail,
    routeDeleteAccount,
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