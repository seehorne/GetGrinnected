const db = require('./db_connect.js');

db.dropExpiredEvents().then(process.exit(0))