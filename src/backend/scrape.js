const fs = require("fs");
const { setMaxParserCache } = require("mysql2");
const URL = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'
const CIPATH = './src/backend/ci_events.json'
const TRUEPATH = './src/backend/event_data.json'
const DROPPATH = './src/backend/drop_ids.json'
const CLOSEFILE = '\n]\n}'
const OPENFILE = '{\n\"data\" : [\n'

/**
 * processExisting
 * 
 * Processes already scraped events to create a set of their IDs
 * @returns Set of IDs associated with pre-existing events
 */
function processExisting(path){
  try{
    events = fs.readFileSync(path, 'utf-8');
    currentEvents = JSON.parse(events) //parse existing events
      existingIDs = new Set();
      if (currentEvents.data && Array.isArray(currentEvents.data)){
        currentEvents.data.forEach(event =>{
          existingIDs.add(event.ID); //add each ID to a set
      });
      return existingIDs; //return the set
      }
  }
  catch(error){
    console.error('Error fetching the JSON data:', error);
  }
}

async function scrapeData(url ,path){
  existingIDs = await processExisting(path);
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
  // .then(response => {
  //   if (!response.ok) {
  //     throw new Error(`HTTP error! Status: ${response.status}`);
  //   }
  //   return response.json();
  // })
  const events = await response.json();
    existingEvents = fs.readFileSync(path, 'utf-8');
    lines = existingEvents.split('\n');
    console.log(lines.length);
    anyExistingEvents = lines.length > 4;
    zeroEventsNotCorrupt = lines.length === 4;
    updatedLines = lines.slice(0, -2); //remove last two lines
    fs.writeFileSync(path, updatedLines.join('\n'));
    maxPage = events.meta.total_pages;
    url = url + '?page='
    for(let i = 1; i <= maxPage; i++){
      pageURL = url+i.toString();
      console.log(pageURL)
      await scrapePage(pageURL, path, anyExistingEvents);
      if (!anyExistingEvents){
        didAdd = fs.readFileSync(path, 'utf-8').split('\n').length > 4;
        anyExistingEvents = didAdd
      }
    }
    fs.appendFileSync(path, CLOSEFILE);
  // })
  // .catch(error => {
  //   console.error('Error fetching the JSON data:', error);
  // });
}



/**
 * scrapeData
 * 
 * Scrapes JSON data of calendar events from specified URL
 * Repackages relevant data to JSON structure stored in event_data.json
 * @param {*} url -> url to data to be scraped, assumes JSON data
 */
async function scrapePage(url, path, anyExistingEvents) {
  //existingIDs = await processExisting(path);
  const appendPromises = [];
  console.log(url)
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
  // .then(response => {
  //   if (!response.ok) {
  //     throw new Error(`HTTP error! Status: ${response.status}`);
  //   }
  //   return response.json();
  // })
  // .then(async events => {
    const events = await response.json()
    // existingEvents = fs.readFileSync(path, 'utf-8');
    // lines = existingEvents.split('\n');
    // console.log(lines.length);
    // anyExistingEvents = lines.length > 4;
    // zeroEventsNotCorrupt = lines.length === 4;
    // updatedLines = lines.slice(0, -2); //remove last two lines
    // fs.writeFileSync(path, updatedLines.join('\n'));
    if (events.data && Array.isArray(events.data)) {
      counter = 0;
      events.data.forEach(event => {
        if (!existingIDs.has(event.id)){ //event not already listed
        existingIDs.add(event.id); //add id to list
        eventInfo = {}; //make dictionary to JSONify event
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
        fs.appendFileSync(path, stringifyEvent);
        }
        counter++;
      }
    );
    }
    //await Promise.all(appendPromises);
    // fs.appendFile(path, CLOSEFILE, function(err){
    //   if(err) throw err;
    //   console.log('WRITING TO JSON')
    //   });
  // .catch(error => {
  //   console.error('Error fetching the JSON data:', error);
  // });
}

/**
 * dropPastEvents
 * 
 * removes elapsed events from JSON of events to be in app 
 * and writes their IDs to drop_ids.json, clearing all previous
 * contents.
 * 
 */
function dropPastEvents(path){
  //record current time for comparison
  now = new Date(new Date().toLocaleString("en-US", { timeZone: "America/Chicago" }));
  console.log(now)
  events = fs.readFileSync(path, 'utf-8');
  storedEvents = JSON.parse(events);
  lines = events.split('\n');
  console.log(lines.length);
  fs.writeFileSync(DROPPATH, OPENFILE); //write opening to JSON
  if (lines.length <= 4){
    return; //this implies there are no events even there
  }
  expiredEvents = 0; //count amount of expired events
  expiredIndices = new Set(); //keep track of expired indices
  //how many with start and end times to remove
  now_midnight = new Date(now).setHours(0, 0, 0, 0); //record midnight today
  idString = "";
  for (let i = 0; i < storedEvents.data.length; i++) {
    console.log(i); //log event index
    let event = storedEvents.data[i]; 
    //time difference between today midnight and event day midnight
    //negative implies expired, 0 implies today, positive implies future
    dayDiff = new Date(event.StartTimeISO).setHours(0, 0, 0, 0) - now_midnight;
    console.log(dayDiff); //log difference from current time
    eventInfo = {}; //set up JSON entry
    if (dayDiff < 0) {//event is on day that has passed
        expiredEvents++;
        expiredIndices.add(i);
        console.log("DROPPING"+ event.Title);
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
        console.log(diff); //log time difference
        console.log(event.Title); //log event under consideration
        if (diff < 0) { //negative difference = expired event
          console.log("DROPPING"+ event.Title);
            expiredEvents++;
            expiredIndices.add(i);
            eventInfo["ID"] = event.ID;
            eventStr = "";
            if (expiredEvents!==1){
              eventStr = ",\n"
            }
            eventStr = eventStr + JSON.stringify(eventInfo);
            //write the JSONified id to file of ones to drop
            fs.appendFileSync(DROPPATH, eventStr, function(err){
              if(err) throw err;
              console.log('WRITING TO JSON')
            });
        }
    } 
    // else if(dayDiff > 0){//event is after today
    //   break;//since they're time sorted, no need to look further once on tomorrow
    // }
  }
  // remove the lines associated with the expired events
  // minus 2 so we don't remove the brackets at the top but rather actual events
  updatedLines = lines.filter((_, i) => !expiredIndices.has(i-2));
  fs.writeFileSync(path, updatedLines.join('\n'), 'utf-8');
  fs.appendFileSync(DROPPATH, CLOSEFILE, function(err){
    if(err) throw err;
    console.log('WRITING TO JSON')
    });
  return;
}

module.exports = { processExisting, scrapeData, URL, dropPastEvents, CIPATH, TRUEPATH, DROPPATH};
//scrapeData(url)