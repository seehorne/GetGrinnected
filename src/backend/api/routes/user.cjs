const db = require('../../db_connect.js');
const util = require('../utils.cjs');

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
  await util.sendOTP(newEmail, oldEmail);
  res.status(202).json({
    'message': 'Verification code sent to new email.'
  });
}

/**
 * Route to get the email address a user is logged in with.
 * 
 * @param {*} req Express request
 * @param {*} res Express response. Will be given HTTP 200 "OK" on success, or errors.
 * @param {*} _next Express error handler/next callback. Unused.
 */
async function routeGetEmail(req, res, _next) {
  // The email is parsed as part of JWT checking, so we already know it exists
  res.json({
    'email': req.email
  });
}

if (require.main !== module) {
  module.exports = {
    routeChangeEmail,
    routeDeleteAccount,
    routeGetEmail,
    routeGetFavorited,
    routeGetNotified,
    routeGetUserData,
    routeGetUsername,
    routePutFavorited,
    routePutNotified,
    routePutUsername,
  };
}