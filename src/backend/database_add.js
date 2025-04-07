const db = require('./db_connect.js');
const fs = require("fs");

db.insertEventsFromScrape().finally(process.exit(0))