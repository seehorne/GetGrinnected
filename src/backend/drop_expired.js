const scrape = require('./scrape.js');
const db = require('./db.js');
const fs = require("fs");

db.dropExpiredEvents(scrape.dropPastEvents(scrape.TRUEPATH));
