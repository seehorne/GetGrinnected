import { createRequire } from 'module';
const require = createRequire(import.meta.url);
//https://stackoverflow.com/questions/69099763/referenceerror-require-is-not-defined-in-es-module-scope-you-can-use-import-in
const scrape = require('./scrape.js');
const db = require('./db_connect.js');

let dropTags = scrape.dropPastEvents(scrape.TRUEPATH);
await db.dropExpiredEvents(dropTags);
