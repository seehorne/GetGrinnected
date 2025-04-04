const scrape = require('./scrape.js');
const fs = require("fs");

await scrape.scrapeData(scrape.URL, scrape.TRUEPATH);