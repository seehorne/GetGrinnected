const fs = require("fs");
const url = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'

/**
 * processExisting
 * 
 * Processes already scraped events to create a set of their IDs
 * @returns Set of IDs associated with pre-existing events
 */
function processExisting(){
  try{
    events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    currentEvents = JSON.parse(events)
      existingIDs = new Set();
      if (currentEvents.data && Array.isArray(currentEvents.data)){
        currentEvents.data.forEach(event =>{
          existingIDs.add(event.ID);
      });
      return existingIDs;
      }
  }
  catch(error){
    console.error('Error fetching the JSON data:', error);
  }
}

/**
 * scrapeData
 * 
 * Scrapes JSON data of calendar events from specified URL
 * Repackages relevant data to JSON structure stored in event_data.json
 * @param {*} url -> url to data to be scraped, assumes JSON data
 */
async function scrapeData(url) {
  existingIDs = await processExisting();
  const appendPromises = [];
  fetch(url)
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(async events => {
    existingEvents = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
    lines = existingEvents.split('\n');
    console.log(lines.length);
    anyExistingEvents = lines.length > 4;
    zeroEventsNotCorrupt = lines.length === 4;
    updatedLines = lines.slice(0, -2); //remove last two lines
    fs.writeFileSync('./src/backend/event_data.json', updatedLines.join('\n'));
    if (events.data && Array.isArray(events.data)) {
      counter = 0;
      events.data.forEach(event => {
        if (!existingIDs.has(event.id)){
        existingIDs.add(event.id);
        eventInfo = {};
        eventInfo["Title"] = event.title;
        eventInfo["Date"] = event.date;
        eventInfo["Time"] = event.date_time;
        eventInfo["StartTimeISO"]= event.date_iso;
        eventInfo["EndTimeISO"]= event.date2_iso;
        if (eventInfo.StartTimeISO===null){
          eventInfo["AllDay?"] = true;
        }
        else{
          eventInfo["AllDay?"] = false;
        }
        eventInfo["Location"] = event.location;
        eventInfo["Description"] = event.description ? event.description.replace(/<[^>]+>/g, '') : 'No description available';
        eventInfo["Audience"] = event.event_types_audience;
        eventInfo["Org"] = event.custom_organization;
        eventInfo["Tags"]= event.tags;
        eventInfo["ID"]= event.id;
        stringifyEvent = JSON.stringify(eventInfo);
        if (anyExistingEvents||counter !=0){ //not first event to be added ever
          stringifyEvent = ',\n'+stringifyEvent;
        }
        else if (zeroEventsNotCorrupt){
          stringifyEvent = '\n'+stringifyEvent;
        }
        appendPromises.push(fs.appendFileSync('./src/backend/event_data.json', 
          stringifyEvent, function(err){
            if(err) throw err;
            console.log('WRITING TO JSON')
          }));
        }
        counter++;
      }
    );
    }
    await Promise.all(appendPromises);
    const CLOSEFILE = '\n]\n}'
    fs.appendFile('./src/backend/event_data.json', CLOSEFILE, function(err){
      if(err) throw err;
      console.log('WRITING TO JSON')
      });
  })
  .catch(error => {
    console.error('Error fetching the JSON data:', error);
  });
}

/**
 * dropPastEvents
 * 
 * removes elapsed events from JSON of events to be in app 
 * 
 * @returns Set() containing the IDs of all expired events
 */
function dropPastEvents(){
  expiredIDs = new Set();
  now = new Date(new Date().toLocaleString("en-US", { timeZone: "America/Chicago" }));
  console.log(now)
  events = fs.readFileSync('./src/backend/event_data.json', 'utf-8');
  storedEvents = JSON.parse(events);
  lines = events.split('\n');
  if (lines.length <= 4){
    return; //this implies there are no events even there
  }
  expiredEvents = 0;
  //how many with start and end times to remove
  for (let i = 0; i < storedEvents.data.length; i++) {
    let event = storedEvents.data[i];
    if (!event.allDay) {
        let diff = new Date(event.EndTimeISO).getTime() - now.getTime();
        console.log(diff);
        console.log(event.Title);
        if (diff < 0) {
            expiredEvents++;
            expiredEvents.add(event.ID);
        }
        if (diff > 0) {
            break; // Exits the loop early, they're in time sorted order
        }
        //if the event end time is exactly now, it can stay, its not that deep
    }
  }
  //all day events. still time sorted so it should fill in if it needs to
  for (let i = 0; i < storedEvents.data.length; i++) {
    let event = storedEvents.data[i];
    if (event.allDay) {
        diff = new Date(event.StartTimeISO).setHours(0, 0, 0, 0) - now.setHours(0, 0, 0, 0);
        if (diff < 0) {
            expiredEvents++;
            expiredEvents.add(event.ID);
        }
        if (diff > 0) {
            break; // Exits the loop early, they're in time sorted order
        }
        //if the event is exactly today, it can stay its not that deep
    }
  }
  firstExpiredEventLine = 3; //this is the line right past the openers
  lastExpiredEventLine = firstExpiredEventLine + expiredEvents;
  updatedLines = lines.filter((_, index) => 
    index < firstExpiredEventLine - 1 || index >= lastExpiredEventLine - 1);
  fs.writeFileSync('./src/backend/event_data.json', updatedLines.join('\n'), 'utf-8');
  return expiredIDs;
}

module.exports = { processExisting, scrapeData, url, dropPastEvents};
//scrapeData(url)
  