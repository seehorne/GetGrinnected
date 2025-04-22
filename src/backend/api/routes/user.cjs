const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const sqlite3 = require('sqlite3');

const db = require('../../db_connect.js');
const { sendCode } = require('../../one_time_code.cjs')

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
 * Resend an OTP code to a user.
 * 
 * @param {*} req  Express request. Body should contain email.
 * @param {*} res  Express response. Will be sent status of the send.
 * @param {*} _next  Express error handler for async, unused.
 */
async function resendOTP(req, res, _next) {
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
  await sendOTP(email);
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
 * Request to log in an existing user account.
 * 
 * @param {*} req    Express request. Body should contain email to log into.
 * @param {*} res    Express response. Will be send success or failure.
 * @param {*} _next  Express error handler.
 */
async function logInUser(req, res, next) {
  // Okay, don't tell anyone but logging in a user is secretly the same
  // process as resending an otp -- literally has the same pre and postconditions.
  resendOTP(req, res, next);
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

async function sendOTP(email) {
    // Send the user a one-time code by email.
    const code = sendCode(email);

    // Calculate the expiration time of the OTP from right now.
    const expiration_mins = 15;
    const expiration_time = new Date(
        new Date().getTime() +   // the current time (in ms)
        expiration_mins * 60000  // add ms to get `expiration_mins` minutes forward
    );

    // Return the data from the OTP so it can be stored and verified later
    const otpData = {
        'email': email,
        'code': code,
        'expire': expiration_time.toISOString()
    };

    // TODO: HASH AND SAVE THE OTP DATA TO A FILE. THIS FUNCTION MAY AS WELL DO THAT, IT IS ALWAYS A THING.
    return otpData;
}

/**
 * Save OTP code to a file for reading later.
 * 
 * @param {string} filename  Filename of database to save to, e.g. `otp.sqlite`
 * @param {string} email     Email associated with OTP
 * @param {string} code      Hashed code associated with OTP
 * @param {string} expire    Expiration date of OTP code
 */
async function otpFileSave(filename, email, code, expire) {
    // Salt and hash the code before we store it
    const salt = await bcrypt.genSalt(SALT_ROUNDS);
    const hashedCode = await bcrypt.hash(code, salt);

    // Open db connection
    const db = new sqlite3.Database(filename);

    // Run all these commands in sequence.
    db.serialize(() => {
        // Create the table if this is our first time opening the db.
        // If it's not the first time, `IF NOT EXISTS` makes it do nothing
        //
        // email is the `PRIMARY KEY`, so each email can only appear once.
        db.run(`
            CREATE TABLE IF NOT EXISTS data(
                email TEXT NOT NULL PRIMARY KEY,
                hashed_code TEXT NOT NULL,
                expire TEXT NOT NULL
            ) STRICT
        `);

        // Attempt to insert the provided email, code, and expiration date.
        //
        // If we try to add one email twice it will cause a conflict because
        // email is the primary key, and we handle that conflict in the 
        // `ON CONFLICT` part by updating the existing entry instead.
        db.run(
            `INSERT INTO data (email, hashed_code, expire)
                VALUES        (?, ?, ?)
                ON CONFLICT (email)
                DO UPDATE SET hashed_code=excluded.hashed_code, expire=excluded.expire`,
            [email, hashedCode, expire]
        );
    });

    // Close DB connection
    db.close();
}

/**
 * Check if a code matches the OTP database.
 * 
 * @param {string} filename  Filename of database to check within, e.g. `otp.sqlite`
 * @param {string} email     Email associated with OTP
 * @param {string} code      Hashed code associated with OTP
 * 
 * @returns true if the OTP exists in the file and is not expired, false if
 * expired or nonexistent.
 */
async function otpFileCheck(filename, email, code) {
    // Define a function that gets the row matching an email.
    // It uses a promise to structure itself because the DB runs async.
    // If you `await getEmail`, you will successfully get an email eventually
    function getEmail(db, email) {
        return new Promise((resolve, reject) => {
            const sql = `SELECT * FROM data WHERE email = ?`;

            db.get(sql, email, (err, row) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(row);
                }
            });
        });
    }

    // Open db connection
    const db = new sqlite3.Database(filename);

    // Evaluate whether the OTP code is good or not
    var return_value = false;
    try {
        // Check the db for that email. If it does not exist, just return false
        const entry = await getEmail(db, email);
        if (entry === undefined) {
            return_value = false;
        } else {
            // Calculate the current date and the expiry date
            const now = new Date();
            const expiry = new Date(entry.expire);

            // OTP code is valid if
            // - the code entered matches properly
            // - it is before the expiration date
            return_value = (
                bcrypt.compare(code, entry.hashed_code) &&
                now <= expiry
            );
        }

        // If the return value is true, delete that row from the database -- they
        // have used their OTP code.
        if (return_value) {
            db.run(`DELETE FROM data WHERE email = ?`, email);
        }
    } catch (err) {
        console.log('Error:' + err);
        return_value = false;
    } finally {
        // Always close the db! No matter what
        db.close();
    }


    // Return after we have completed our try/catch/finally, to be sure the
    // db connection has been closed.
    return return_value;
}

/**
 * Clean up any expired entries in the OTP database.
 * 
 * @param {*} filename  Filename of database to clean, e.g. `otp.sqlite`.
 */
async function otpFileClean(filename) {
    // Define a function that deletes the row matching an email.
    // It uses a promise to structure itself because the DB runs async.
    // To use it, `await deleteEmail`.
    function deleteEmail(db, email) {
        return new Promise((resolve, reject) => {
            const sql = `DELETE FROM data WHERE email = ?`;

            db.run(sql, email, (err, _row) => {
                if (err) {
                    reject(err);
                } else {
                    resolve();
                }
            });
        });
    }
    const now = new Date();

    const db = new sqlite3.Database(filename);

    var to_delete = [];
    db.serialize(() => {
        // Find all rows that should be deleted
        const sql = `SELECT * FROM data`
        db.each(sql, async (err, row) => {
            if (err) {
                console.log('Error:' + err);
            } else {
                if (new Date(row.expire) < now) {
                    to_delete.push(row.email);
                }
            }
        });

        // Do the actual deleting
        const statement = db.prepare(`DELETE FROM data WHERE email = ?`);
        for (const email of to_delete) {
            console.log(`delete ${email}`);
            statement.run(email);
        }

    });
    // TODO: WRITE ME
    // Get the current time

    // Open connection to the database

    // Delete all db entries that have already expired

    // Close the database connection
}

if (require.mail !== module) {
    module.exports = {
        verifyOTP,
        logInUser,
        signUpNewUser,
        resendOTP,
        checkUsernameExists,
        validateUsername,
        otpFileCheck,
        otpFileClean,
        otpFileSave,
    };
}