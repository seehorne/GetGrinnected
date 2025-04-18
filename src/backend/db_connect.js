const mysql = require('mysql2');
const dotenv = require('dotenv');
const fs = require ('fs');
const bcrypt = require('bcrypt');
const scrape = require('./scrape.js');

/* Important note a .env file needs to exist on any machine hoping to run this with the necessary 
*  information in its contents (this isn't hard but will need to be added on the server side)
*/

const DROPPATH = './src/backend/drop_ids.txt'

dotenv.config();

const pool = mysql.createPool({
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE
}).promise()

/**
 * insertEventsFromScrape
 * 
 * uses scraped json file to fill events into database
 */
async function insertEventsFromScrape(){

    try{
        const file_data =  fs.readFileSync(scrape.TRUEPATH, 'utf-8')
        const parsing = JSON.parse(file_data);
        const events = parsing.data;

        const sql = `
        INSERT IGNORE INTO events (
            eventid, event_name, event_description, event_location, organizations, rsvp, 
            event_date, event_time, event_all_day, event_start_time, event_end_time, 
            tags, event_private, repeats, event_image, is_draft
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
             `;

        for (const event of events) {
            try {

                // This is so I can access Allday the method
                const isAllDay = event["AllDay?"] ? 1 : 0;
                const tags = combineTags(event);

                // This is to parse multiple orgs out of an org field (also account for events that don't have orgs)
                const orgs = event.Org ? event.Org.split(",") : [];

                const startTimeISO = new Date(event.StartTimeISO);
                const endTimeISO = new Date(event.EndTimeISO);

                // Executes the prepared statement with the event values and fills in with default values
                // where the scraper didn't get information.
                await pool.query(sql, [event.ID, event.Title, event.Description, event.Location,
                    JSON.stringify(orgs), 0, event.Date, event.Time, isAllDay, startTimeISO,
                    endTimeISO, JSON.stringify(tags), 0, 0, null, 0 ]).then(console.log(`I added event:  ${event.ID}`))

            } catch (eventError) {
                console.error(`Error inserting event: ${event.Title}`, eventError);
            }
        }

    } catch (error) {
        console.error('Error processing events:', error);
    } 
}

// This is to combine the tags and audience to use audience as an additional tag
function combineTags(event){
    // Used a ternary to avoid null since that is what they give instead of an empty array.
    // If it would look nicer or be more legible as an if statement please say the word.
    const tags = Array.isArray(event.Tags) ? event.Tags : []; 
    const Audience = Array.isArray(event.Audience) ? event.Audience : [];
    const combined_tags = tags.concat(Audience);
    return combined_tags;
}

async function getEvents(){
    const [events] = await pool.query("SELECT * FROM events");
    return events;
}

/**
 * Construct a part of a query that selects for all tags.
 *
 * \param tags  List of string tags
 * \returns     A string that serves as a SQL selector for all those tags
 */
function queryAllTags(tags) {
    // escape the tag so that we will get a string query containing '\"tagnamne\"', for valid search
    const pieces = tags.map((t) => `JSON_CONTAINS(tags, ${pool.escape(t)}, '$')`);
    const piecesAndTogether = pieces.join(' AND ');
    return '(' + piecesAndTogether + ')';
}

/**
 * Construct a part of a query that selects for events between a start and end time.
 *
 * \param start  Start time in Unix time
 * \param end    End time in Unix time
 * \returns      A string that serves as a SQL selector for those dates
 */
function queryBetweenDates(start, end) {
    // Construct a query for events that happen entirely between params
    //
    // "entirely between" means
    //   event_start_time is AFTER start param
    //     AND
    //   event_end_time is BEFORE end param
    const event_time_between = `(event_start_time >= ${pool.escape(start)} AND event_end_time <= ${pool.escape(end)})`
    return event_time_between;
}

/**
 * Query the database for all events that have every tag in a list.
 *
 * \param tags  List of strings.
 * \returns     A list of all events that contain every tag in `tags`.
 */
async function getEventsWithTags(tags) {
    const query = 'SELECT * FROM events WHERE ' + queryAllTags(tags);
    const [events] = await pool.query(query);
    return events;
}

/**
 * Query the database for events between two dates.
 *
 * \param start  Start date in unix time
 * \param end    End date in unix time
 * \returns      A list of all events between those dates
 */
async function getEventsBetween(start, end) {
    const query = 'SELECT * FROM events WHERE ' + queryBetweenDates(start, end);
    const [events] = await pool.query(query);
    return events;
}

/**
 * Query the database for events between two dates, that also have certain tags.
 *
 * \param start  Start date in unix time
 * \param end    End date in unix time
 * \param tags   List of tags that are required
 * \returns      A list of all events between those dates that also have every tag
 */
async function getEventsBetweenWithTags(start, end, tags) {
    const query = 'SELECT * FROM events WHERE ' + queryBetweenDates(start, end)
        + ' AND ' + queryAllTags(tags);
    const [events] = await pool.query(query);
    return events;
}

/**
 * Return whether a username has an account associated with it.
 * 
 * @param {*} username  The username to check
 * @returns  The JSON representation of that user if they exist, or `undefined`
 *           if they do not.
 */
async function getAccount(username){
    const [account] = await pool.query(`
       SELECT * 
       FROM accounts
       WHERE account_name = ?
        `, [username]);
    
    return account[0]; 
}

/**
 * Create a new account with a username and email.
 * 
 * @param {string} username  Username of account
 * @param {string} email     Grinnell email to create account for
 * @throws   Error if user already exists.
 * @returns  Result of the DB query once it is completed.
 */
async function createAccount(username, email){
    const existing_account = await getAccount(username);

    if(existing_account){
        throw new Error("User already exists.");
    }
    const result = await pool.query(`
        INSERT INTO
        accounts (account_name, email)
        VALUES (?, ?)`, [username, email]);
    
        return result;
}

async function dropExpiredEvents(){

    const file_data =  fs.readFileSync(scrape.DROPPATH, 'utf-8')

    const json = JSON.parse(file_data);

    const eventIds_array = json.data.map(item => item.ID).filter(id => id);

    if (eventIds_array.length === 0) {
        console.log("No events to delete.");
        return;
    }
    // Maps the number of event ids to corresponding ?s for prepared statement setup
    const placeholders = eventIds_array.map(() => "?").join(", ");
    const query = `DELETE FROM events WHERE eventid IN (${placeholders})`;

    const [result] = await pool.query(query, eventIds_array);
    console.log(`Output from result: ${result}`);
    return result;
}

async function verifyLogin(username, password){

    const existing_account = await getAccount(username);

    if(!existing_account){
        return false; // No user found case
    }

    const [db_password] = await pool.query(`
        SELECT password
        FROM accounts
        WHERE account_name = ?`, [username]);

    const isMatch = await bcrypt.compare(password, db_password[0].password);
    
    // These returns are subject to change depending on what would be most usefully returned

    if (isMatch) {
      return true; // Legal username and password 
    } else {
      return false; // Invalid username and password
    }
}

// Testing on Server

//const setEvents = new Set([25625, 30582, 27740]);
//dropExpiredEvents(setEvents);

//getEvents().then(console.log)

//createAccount("test1", "email@email.com", "password");

// TODO: these should become actual tests. I think that's doable!
// TODO: lol no it isn't! github has no FUCKING CLUE what the database is.
async function testLogins() {
    console.log("Legal Login");
    const log1 = await verifyLogin("test1", "password");
    console.log(log1);

    console.log("Illegal Login (password)");
    const log2 = await verifyLogin("test1", "passwrd");
    console.log(log2);

    console.log("Illegal Login (username)");
    const log3 = await verifyLogin("test", "password");
    console.log(log3);
}

if (require.main === module) {
    // File is being used as a script. Run it.
    insertEventsFromScrape();
    testLogins(); // TODO: move to different test? unknown if possible.
} else {
    // File is being used as a module. Export it.
    module.exports = {
        createAccount,
        dropExpiredEvents,
        getAccount,
        getEvents,
        getEventsBetween,
        getEventsBetweenWithTags,
        getEventsWithTags,
        insertEventsFromScrape,
    };
}