const db = import('./db_connect.js');
const fs = require("fs");

await db.insertEventsFromScrape();