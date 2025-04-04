const scrape = require('./scrape.js');
const db = import('./db_connect.js');
const fs = require("fs");

let dropTags = scrape.dropPastEvents(scrape.TRUEPATH);
await db.dropExpiredEvents(dropTags);
