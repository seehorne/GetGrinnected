const scrape = require('./scrape.js');
const db = require('./db_connect.js');
const fs = require("fs");

db.dropExpiredEvents(scrape.dropPastEvents(scrape.TRUEPATH));
