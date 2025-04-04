const scrape = import('./scrape.js');
const db = import('./db_connect.js');

let dropTags = scrape.dropPastEvents(scrape.TRUEPATH);
await db.dropExpiredEvents(dropTags);
