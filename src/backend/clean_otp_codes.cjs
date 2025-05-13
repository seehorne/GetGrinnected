const sqlite3 = require('sqlite3');
const DBPATH = './src/backend/Database/localOTP.db'

async function otpFileClean(filename) {
    // Format the current time in the same format as the database
    //   example: 2025-05-13T18:51:51.899Z
    const now = new Date().toISOString()

    // Create a database connection
    const db = new sqlite3.Database(filename);

    // Delete all expired events.
    // We can do this in alphabet order, since ISO A < ISO B means that ISO A happened first
    const sql = `DELETE FROM data WHERE expire < ?`
    db.get(sql, now,
        // callback function for the db command. print the error if we hit one.
        (err, _) => {
            if (err) {
                console.error(err);
            }
        });
}

// when this file is run act like a script, but export our function otherwise
if (require.main === module) {
    otpFileClean(DBPATH);
} else {
    module.exports = { otpFileClean };
}