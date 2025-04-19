import { sendCode } from '../one_time_code.mjs';
import * as bcrypt from 'bcrypt';

export async function sendOTP(email) {
    // Send the user a one-time code by email.
    const code = sendCode(email);

    // Salt and hash the code before we store it
    const saltRounds = 10;  // generally-accepted number of rounds
    const salt = await bcrypt.genSalt(saltRounds);
    const hashedCode = await bcrypt.hash(code, salt);

    // TODO: STORE (email, hashed code, current time) TO CHECK LATER
}

/**
 * Parse from a time URL parameter to a Unix timestamp.
 *
 * @param timeParam String representing a date.
 * @returns Date object representing the date. It may be an invalid date, in
 *          which case calling .valueOf() on it will return NaN.
 */
export function parseParamDate(paramDate) {
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
export function parseQueryTags(queryTags) {
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
    queryTags = queryTags        // ["a", "b,c"]
        .map((x) => x.split(','))  // -> [["a"], ["b", "c"]]
        .flat();                   // -> ["a", "b", "c"]

    // For the DB query to find tags, we must put quotation marks on each tag.
    // This is because of the way the tags are stored as JSON in the database.
    const tags = queryTags.map((x) => '"' + x + '"');
    return tags;
}

/**
 * Determine whether a username is valid.
 * 
 * @param {string} username  The username to check.
 * @returns  An object with these keys:
 * - result: true if username is valid, false if not
 * - reason: if result is false, the reason why.
 */
export function validateUsername(username) {
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