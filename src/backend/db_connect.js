const mysql = require('mysql2');
const dotenv = require('dotenv');
const fs = require ('fs');

/* Important note a .env file needs to exist on any machine hoping to run this with the necessary 
*  information in its contents (this isn't hard but will need to be added on the server side)
*/

dotenv.config();

const pool = mysql.createPool({
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE
}).promise()

async function insertEventsFromScrape(){

    try{
        const file_data =  fs.readFileSync('event_data.json')
        const parsing = JSON.parse(file_data);
        const events = parsing.data;

        const sql = `
        INSERT INTO events (
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

                // This is to parse multiple orgs out of an org field
                const orgs = event.Org.split(",");

                // Executes the prepared statement with the event values and fills in with defaul values
                // where the scraper didn't get information.
                await pool.query(sql, [event.ID, event.Title, event.Description, event.Location,
                    JSON.stringify(orgs), 0, event.Date, event.Time, isAllDay, event.StartTimeISO,
                    event.EndTimeISO, JSON.stringify(tags), 0, 0, null, 0 ]);

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
    const tags = Array.isArray(event.tags) ? event.tags : []; 
    const Audience = Array.isArray(event.Audience) ? event.Audience : [];
    const combined_tags = tags.concat(Audience);
    return combined_tags
}

insertEventsFromScrape();
