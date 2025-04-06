const fs = require("fs");
const URL = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'
const CIPATH = './src/backend/ci_events.json'
const TRUEPATH = './src/backend/event_data.json'
const DROPPATH = './src/backend/drop_ids.json'

/**
 * processExisting
 * 
 * Processes already scraped events to create a set of their IDs
 * @returns Set of IDs associated with pre-existing events
 */
function processExisting(path){
  try{
    events = fs.readFileSync(path, 'utf-8');
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
async function scrapeData(url, path) {
  existingIDs = await processExisting(path);
  const appendPromises = [];
  fetch(url)
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(async events => {
    existingEvents = fs.readFileSync(path, 'utf-8');
    lines = existingEvents.split('\n');
    console.log(lines.length);
    anyExistingEvents = lines.length > 4;
    zeroEventsNotCorrupt = lines.length === 4;
    updatedLines = lines.slice(0, -2); //remove last two lines
    fs.writeFileSync(path, updatedLines.join('\n'));
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
        appendPromises.push(fs.appendFileSync(path, 
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
    fs.appendFile(path, CLOSEFILE, function(err){
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
function dropPastEvents(path){
  expiredIDs = new Set();
  expiredIndices = new Set();
  now = new Date(new Date().toLocaleString("en-US", { timeZone: "America/Chicago" }));
  console.log(now)
  events = fs.readFileSync(path, 'utf-8');
  storedEvents = JSON.parse(events);
  lines = events.split('\n');
  console.log(lines.length);
  fs.writeFileSync(DROPPATH, "{\n\"data\" : [\n");
  if (lines.length <= 4){
    return; //this implies there are no events even there
  }
  expiredEvents = 0;
  //how many with start and end times to remove
  now_midnight = new Date(now).setHours(0, 0, 0, 0);
  idString = "";
  for (let i = 0; i < storedEvents.data.length; i++) {
    console.log(i);
    let event = storedEvents.data[i];
    dayDiff = new Date(event.StartTimeISO).setHours(0, 0, 0, 0) - now_midnight;
    console.log(dayDiff);
    eventInfo = {};
    if (dayDiff < 0) {//event is on day that has passed
        expiredEvents++;
        expiredIDs.add(event.ID);
        expiredIndices.add(i);
        console.log("DROPPING"+ event.Title);
        idString = idString+event.ID+'\n';
        eventInfo["ID"] = event.ID;
        eventStr = "";
            if (expiredEvents!==1){
              eventStr = ",\n"
            }
            eventStr = eventStr + JSON.stringify(eventInfo);
        fs.appendFileSync(DROPPATH, eventStr, function(err){
          if(err) throw err;
          console.log('WRITING TO JSON')
        });
    } else if (!event.allDay && dayDiff === 0) {//event is today, may or may not be over
        let diff = new Date(event.EndTimeISO).getTime() - now.getTime();
        console.log(diff);
        console.log(event.Title);
        if (diff < 0) {
          console.log("DROPPING"+ event.Title);
            expiredEvents++;
            expiredIDs.add(event.ID);
            expiredIndices.add(i);
            idString = idString+event.ID+'\n';
            eventInfo["ID"] = event.ID;
            eventStr = "";
            if (expiredEvents!==1){
              eventStr = ",\n"
            }
            eventStr = eventStr + JSON.stringify(eventInfo);
            fs.appendFileSync(DROPPATH, eventStr, function(err){
              if(err) throw err;
              console.log('WRITING TO JSON')
            });
        }
    } else if(dayDiff > 0){//event is after today
      break;//since they're time sorted, no need to look further once on tomorrow
    }
  }
  // minus 2 so we don't remove the brackets at the top but rather actual events
  updatedLines = lines.filter((_, i) => !expiredIndices.has(i-2));
  fs.writeFileSync(path, updatedLines.join('\n'), 'utf-8');
  //fs.writeFileSync(DROPPATH, idString, 'utf8');//write it to a file
  const CLOSEFILE = '\n]\n}'
  fs.appendFileSync(DROPPATH, CLOSEFILE, function(err){
    if(err) throw err;
    console.log('WRITING TO JSON')
    });
  return expiredIDs;
}

module.exports = { processExisting, scrapeData, URL, dropPastEvents, CIPATH, TRUEPATH, DROPPATH};
//scrapeData(url)