const db = import('./db_connect.js');
const fs = require("fs");

db.insertEventsFromScrape();