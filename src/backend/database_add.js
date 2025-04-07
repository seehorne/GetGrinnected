const db = require('./db_connect.js');
const fs = require("fs");

db.insertEventsFromScrape().then(process.exit(0))