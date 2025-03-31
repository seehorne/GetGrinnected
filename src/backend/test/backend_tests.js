//const scrape = require('../scrape');
const { test, describe } = require('node:test');
const assert = require('node:assert/strict');
const fs = require("fs");
//import { register } from 'node:module'
const scrape = require('../scrape.js');

describe('Web Scrape Output Unit Tests', () => {
test('All events are unique by ID', { concurrency: true }, t => {
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    currentEvents = JSON.parse(events);
    IDSet = scrape.processExisting();
    numIDs = IDSet.size;
    numEvents = currentEvents.data.length;
    assert.strictEqual(numIDs,numEvents)
  });

  test('All events have start time before end time OR marked as all day', 
    { concurrency: false }, t => {
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
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
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
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
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    ogEvents = JSON.parse(events);
    beforeSize = ogEvents.data.length;
    await scrape.scrapeData(scrape.url);
    newEvents = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    nowEvents = JSON.parse(events);
    nowSize = nowEvents.data.length;
    assert(nowSize >= beforeSize)
  }
  )
});