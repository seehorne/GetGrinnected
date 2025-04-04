const db = import('./db_connect.js');
const fs = import("fs");

await db.insertEventsFromScrape();