const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const sqlite3 = require('sqlite3');

const { sendCode } = require('../one_time_code.cjs')
const database = require('../db_connect.js');
const SALT_ROUNDS = 10;
const DBPATH = './src/backend/Database/localOTP.db'

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
 * Determine whether a username is valid.
 * 
 * @param {string} username  The username to check.
 * @returns  An object with these keys:
 * - result: true if username is valid, false if not
 * - reason: if result is false, the reason why.
 */
function validateUsername(username) {
    // Make sure the username is within the right length (8-20 chars)
    const minLength = 1;
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
    if (letters.length < 1) {
        return {
            result: false,
            reason: `Username must contain at least one letter.`
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
        reason: 'Valid username.'
    };
}

async function sendOTP(email) {
    sendTo = email
    if (email.toLowerCase() == 'getgrinnected.demo@grinnell.edu'){
        sendTo = 'getgrinnected.demo@gmail.com'
    }
    // Send the user a one-time code by email.
    const code = sendCode(sendTo);

    // Calculate the expiration time of the OTP from right now.
    const expiration_mins = 15;
    const expiration_time = new Date(
        new Date().getTime() +   // the current time (in ms)
        expiration_mins * 60000  // add ms to get `expiration_mins` minutes forward
    );

    // Combine the data from the OTP so it can be stored and verified later
    const otpData = {
        'email': email,
        'code': code,
        'expire': expiration_time.toISOString()
    };

    // Save the data to the local OTP database before returning
    await otpFileSave(DBPATH, otpData.email, otpData.code, otpData.expire);
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
    // if the file does not exist it will be created
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
                hashedCode TEXT NOT NULL,
                expire TEXT NOT NULL
            ) STRICT
        `);

        // Attempt to insert the provided email, code, and expiration date.
        //
        // If we try to add one email twice it will cause a conflict because
        // email is the primary key, and we handle that conflict in the 
        // `ON CONFLICT` part by updating the existing entry instead.
        db.run(
            `INSERT INTO data (email, hashedCode, expire)
                VALUES        (?, ?, ?)
                ON CONFLICT (email)
                DO UPDATE SET hashedCode=excluded.hashedCode, expire=excluded.expire`,
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
            const code_match = await bcrypt.compare(code, entry.hashedCode);
            const not_expired = now <= expiry;
            return_value = code_match && not_expired;
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
    //alphabetically sort? some sort of db call to that effect

    // TODO: WRITE ME
    // Get the current time

    // Open connection to the database

    // Delete all db entries that have already expired

    // Close the database connection
}

/**
 * Parse from a time URL parameter to a Unix timestamp.
 *
 * @param timeParam String representing a date.
 * @returns Date object representing the date. It may be an invalid date, in
 *          which case calling .valueOf() on it will return NaN.
 */
function parseParamDate(paramDate) {
    // these are the three features we care about in our date format.
    // "\\d" goes to the string \d, which means any digit in regex.
    const validDate = "\\d\\d\\d\\d-\\d\\d-\\d\\d"  // YYYY-MM-DD
    const validTime = "T\\d\\d:\\d\\d"              // THH:MM, no seconds
    const validTimeZone = "[+-]\\d\\d\\d\\d"        // Timezone, eg -0500 or +1230

    // MATCHES: YYYY-MM-DDTHH:MM-ZZZZ or YYYY-MM-DDTHH:MM+ZZZZ
    // CHANGES: nothing
    const allFeaturesRegex = new RegExp(
        '^' +                                    // matches start of string
        validDate + validTime + validTimeZone +  // main body of regex
        '$'                                      // matches end of string
    );
    if (allFeaturesRegex.test(paramDate)) {
        return new Date(paramDate);
    }

    // MATCHES: YYYY-MM-DDTHH:MM
    // CHANGES: adds default timezone of UTC-5 (grinnell)
    const dateTimeRegex = new RegExp('^' + validDate + validTime + '$');
    if (dateTimeRegex.test(paramDate)) {
        paramDate = paramDate.concat('-0500');
        return new Date(paramDate);
    }

    // MATCHES: YYYY-MM-DD
    // CHANGES: adds default time of midnight
    //          adds default timezone of UTC-5 (grinnell)
    const dateOnlyRegex = new RegExp('^' + validDate + '$');
    if (dateOnlyRegex.test(paramDate)) {
        paramDate = paramDate.concat('T00:00-0500');
        return new Date(paramDate);
    }

    // If none of those attempts to figure out the format worked,
    // return a date that we know is definitely invalid to match the return type.
    return new Date('purposefully invalid date string');
}

/**
 * Parse from a query tag object to a well-formed list of individual tags. Tags
 * with commas will be split to support queries like `?tag=a,b,c,d`.
 * 
 * These are the possible values for queryTags to take when coming from Express:
 *   not specified      -> undefined
 *   single instance    -> a string
 *   multiple instances -> an array of strings
 *
 * @param queryTags a query tag object, which can be
 *                  either a list of strings or a string.
 * @returns an array containing each tag, or null if no tags were found.
 *                   each tag in the array will be quoted.
 */
function parseQueryTags(queryTags) {
    // Don't try and parse undefined tags (which means not provided in URL)
    if (!queryTags) {
        return [];
    }

    // If there is only one element,
    // we need to put it in an array for the next step to work
    if (!Array.isArray(queryTags)) {
        queryTags = [queryTags];
    }

    // Split any comma-separated items to make the array flat. See example.
    queryTags = queryTags          // ["a", "b,c"]
        .map((x) => x.split(','))  // -> [["a"], ["b", "c"]]
        .flat();                   // -> ["a", "b", "c"]

    // For the DB query to find tags, we must put quotation marks on each tag.
    // This is because of the way the tags are stored as JSON in the database.
    const tags = queryTags.map((x) => '"' + x + '"');
    return tags;
}

/**
 * Filter an array by an async predicate.
 * @param {*} array  Array of items to filter
 * @param {*} predicate  Predicate to check, should be an async function.
 */
async function asyncFilter(array, predicate) {
    // await evaluation of the predicate on all array items
    const results = await Promise.all(array.map(predicate));

    // Now that we've evaluated all the predicates, filter the array by that.
    return array.filter((_v, index) => results[index]);
}

/**
 * Given an object, determine whether it is an array containing valid event IDs.
 * 
 * @param {*} object  Object to check.
 * @returns `true` if these conditions are true.
 * - `object` is an array
 * - all elements of `object` correspond to a valid event ID
 */
async function eventsExist(object) {
    // Make sure the object is actually an array.
    const isArray = Array.isArray(object);
    if (!isArray) {
        return {
            'result': false,
            'message': 'Object is not an array.'
        };
    }
    const array = object;

    // Filter the array by which items do NOT exist.
    const badIDs = await asyncFilter(array, async (item) => {
        const event = await database.getEventByID(item);
        return event === undefined;
    });

    // If there are any items that do not match with an event, return that.
    if (badIDs.length !== 0) {
        return {
            'result': false,
            'message': `Items ${JSON.stringify(badIDs)} did not correspond to actual event IDs.`
        };
    }

    // Otherwise we're good, and we can return true with no message.
    return {
        'result': true,
        'message': 'Success'
    };
}

/**
 * Handle a request to set an array in user data for a single user, with a parameter
 * that lets you set which array gets modified.
 * 
 * @param {*} array_name  Which array name to set. Should match up with the account table.
 * @param {*} req  Express request.
 * @param {*} res  Express response.
 * @param {*} _next  Next function to call from express, unused.
 */
async function userDataSetArray(array_name, req, res, _next) {
    // Get the email from the request, assuming it was set by the middleware.
    const email = req.email;

    // The new array will be in the request body under the proper array name.
    const newArray = req.body[array_name];

    // Make sure the array is all integers, and that all elements are real events
    const exist = await eventsExist(newArray);
    if (!exist.result) {
        res.status(400).json({
            'error': 'Invalid request',
            'message': exist.message
        });
        return;
    }

    // Update that user's favorited events in the database. Turn the array into a
    // string so it will be handled properly by the DB query.
    await database.modifyAccountField(email, array_name, JSON.stringify(newArray));
    res.json({
        'message': `Successfully updated ${array_name}`
    });
}

if (require.main !== module) {
    module.exports = {
        generateUserTokens,
        validateUsername,
        otpFileCheck,
        otpFileClean,
        otpFileSave,
        sendOTP,
        parseParamDate,
        parseQueryTags,
        eventsExist,
        userDataSetArray,
    };
}