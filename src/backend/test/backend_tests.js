//const scrape = require('../scrape');
const { test, describe } = require('node:test');
const assert = require('node:assert/strict');
const fs = require("fs");
//import { register } from 'node:module'
const { processExisting } = require('../scrape.js');

describe('API unit tests', () => {
test('All events are unique by ID', { concurrency: true }, t => {
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    current_events = JSON.parse(events);
    IDSet = processExisting();
    num_IDs = IDSet.size;
    num_events = current_events.data.length;
    assert.strictEqual(num_IDs,num_events)
  });

  test('All events have start time before end time OR marked as all day', { concurrency: false }, t => {
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    current_events = JSON.parse(events);
    num_non_compliant = 0;
    if (events.data && Array.isArray(events.data)) {
        events.data.forEach(event => {
            allDay = event.allDay;
            if(!allDay){
                const startDate = Date(event.StartTimeISO);
                const endDate = Date(event.EndTimeISO);
                diff = endDate.getTime()-startDate.getTime();
                if(diff <= 0){
                    num_non_compliant++;
                }
            }
        })
    }
    assert.strictEqual(0,num_non_compliant);
  });
});