//const scrape = require('../scrape');
const { test, describe } = require('node:test');
const assert = require('node:assert/strict');
const fs = require("fs");
//import { register } from 'node:module'
const scrape = require('../scrape.js');

describe('Web Scrape Output Unit Tests', () => {
test('All events are unique by ID', { concurrency: true }, t => {
    events = fs.readFileSync(scrape.CIPATH, 'utf-8');
    currentEvents = JSON.parse(events);
    IDSet = scrape.processExisting(scrape.CIPATH);
    numIDs = IDSet.size;
    numEvents = currentEvents.data.length;
    assert.strictEqual(numIDs,numEvents)
  });

  test('All events have start time before end time OR marked as all day', 
    { concurrency: false }, t => {
    events = fs.readFileSync(scrape.CIPATH, 'utf-8');
    currentEvents = JSON.parse(events);
    numNonCompliant = 0;
    if (events.data && Array.isArray(events.data)) {
        events.data.forEach(event => {
            allDay = event.allDay;
            if(!allDay){
                const startDate = Date(event.StartTimeISO);
                const endDate = Date(event.EndTimeISO);
                diff = endDate.getTime()-startDate.getTime();
                if(diff <= 0){
                    numNonCompliant++;
                }
            }
        })
    }
    assert.strictEqual(0,numNonCompliant);
  });

  test('All events have an associated organization', { concurrency: false }, 
    t => {
    events = fs.readFileSync(scrape.CIPATH, 'utf-8');
    currentEvents = JSON.parse(events);
    numNonCompliant = 0;
    if (events.data && Array.isArray(events.data)) {
        events.data.forEach(event => {
            org = event.Org;
            if (org === null){
                numNonCompliant++;
            }
        })
    }
    assert.strictEqual(0,numNonCompliant);
  });

  test('After scraping, there are as many or more events as before (no lost events)',
    { concurrency: true }, async t => {
    events = fs.readFileSync(scrape.CIPATH, 'utf-8');
    ogEvents = JSON.parse(events);
    beforeSize = ogEvents.data.length;
    await scrape.scrapeData(scrape.URL, scrape.CIPATH);
    newEvents = fs.readFileSync(scrape.CIPATH, 'utf-8');
    nowEvents = JSON.parse(events);
    nowSize = nowEvents.data.length;
    assert(nowSize >= beforeSize)
  }
  )

  test('When running drop twice, IDs to drop are distinct each run',
    { concurrency: true}, async t => {
      //run drop once
      await scrape.dropPastEvents(scrape.CIPATH, true);
      //read resulting file
      firstScrape = fs.readFileSync(scrape.DROPPATH, 'utf-8');
      //JSONify
      firstIDs = JSON.parse(firstScrape);
      //count number of IDs
      firstIDCount = firstIDs.data.length;
      //do it all again for a second version
      await scrape.dropPastEvents(scrape.CIPATH, true);
      secondScrape = fs.readFileSync(scrape.DROPPATH, 'utf-8');
      secondIDs = JSON.parse(secondScrape);
      secondIDCount = firstIDs.data.length;
      //if all IDs are distinct, this should be the total number we're looking at now
      totalNumberID = firstIDCount + secondIDCount;
      //make a set to store IDs in
      allIDs = new Set();
      //add IDs to set
      firstIDs.data.forEach(event => {
        allIDs.add(event.ID);
      });
      secondIDs.data.forEach(event => {
        allIDs.add(event.ID);
      });
      //if all IDs were distinct, the set size should be the number we added to
      assert(totalNumberID === allIDs.size)
    }
    )
    });
    
    test('After dropping events, there are at most the same number as before (no spurious additions)',
      { concurrency: false }, t => {
        events = fs.readFileSync(scrape.CIPATH, 'utf-8');
        ogEvents = JSON.parse(events);
        beforeSize = ogEvents.data.length;
        scrape.dropPastEvents(scrape.CIPATH, true);
        newEvents = fs.readFileSync(scrape.CIPATH, 'utf-8');
        nowEvents = JSON.parse(events);
        nowSize = nowEvents.data.length;
        assert(nowSize <= beforeSize)
      }
    )
    
    test('Number of events post-scrape cooresponds to number of scraped pages',
      { concurrency: true}, async t => {
        //run drop to get a fresh start
        await scrape.dropPastEvents(scrape.CIPATH, true);
        //read the URL for new scrape
        const response = await fetch(scrape.URL);
        const events = await response.json();
        //get how many pages there are
        numPages = events.meta.total_pages;
        //each page has 100 events
        //(except for last, which has up to 100 but may not be full)
        //so number of events should be >= 100 * (#pages-1) 
        //and <= 100 * (#pages)
        minEvents = 100 * (numPages-1)
        maxEvents = 100 * (numPages)
        //do the scrape
        await scrape.scrapeData(scrape.URL,scrape.CIPATH)
        //check how many events there are
        foundEvents = fs.readFileSync(scrape.CIPATH, 'utf-8');
        jsonifiedFoundEvents = JSON.parse(foundEvents);
        foundEventCount = jsonifiedFoundEvents.data.length;
        console.log(foundEventCount)
        assert(foundEventCount >= minEvents && foundEventCount <= maxEvents);
      }
    )
