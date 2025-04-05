const scrape = require('./scrape.js');
const db = require('./db_connect.js');

let dropTags = scrape.dropPastEvents(scrape.TRUEPATH);
db.dropExpiredEvents(dropTags);
