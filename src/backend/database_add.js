const db = require('./db_connect.js');
const fs = require("fs");

await db.insertEventsFromScrape();