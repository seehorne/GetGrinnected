const scrape = require('./scrape.js');
const db = require('./db_connect.js');
const fs = require("fs");

let dropTags = scrape.dropPastEvents(scrape.TRUEPATH);
await db.dropExpiredEvents(dropTags);
