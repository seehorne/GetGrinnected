const scrape = require('./scrape.js');
const db = require('./db_connect.js');
const fs = require("fs");

let dropTags = await scrape.dropPastEvents(scrape.TRUEPATH);
db.dropExpiredEvents(dropTags);
