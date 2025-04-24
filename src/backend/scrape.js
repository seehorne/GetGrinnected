const fs = require("fs");
const { setMaxParserCache } = require("mysql2");
const URL = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'
const CIPATH = './src/backend/ci_events.json'
const TRUEPATH = './src/backend/event_data.json'
const DROPPATH = './src/backend/drop_ids.json'
const CLOSEFILE = '\n]\n}'
const OPENFILE = '{\n\"data\" : [\n'

function removeHTMLRelics(input, isArray){
  if (isArray){//go through the array if input is an array
    num_elements = input.length;
    for (let i = 0; i <  num_elements; i++) {
      currentString = input[i].replace(/&amp;/g, "&");//replace HTML ampersand with ampersand
      currentString = currentString.replace(/&#160/g, " ");//replace HTML nonbreaking space with space
      //capitalize first letter
      finalString = String(currentString).charAt(0).toUpperCase() + String(currentString).slice(1)
      input[i] = finalString
    }
    return input;
  }
  else{//otherwise its just a string so treat it like one
    input = input.replace(/&amp;/g, "&"); //replace HTML ampersand with ampersand
    input = input.replace(/&#160/g, " ");//replace HTML nonbreaking space with space
    //capitalize first letter
    inputCaps = String(input).charAt(0).toUpperCase() + String(input).slice(1);
    return inputCaps;
  }
}

/**
 * processExisting
 * 
 * Processes already scraped events to create a set of their IDs
 * @returns Set of IDs associated with pre-existing events
 */
function processExisting(path) {
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

/**
 * scrapeData
 * 
 * Scrapes JSON data of calendar events from specified URL
 * Repackages relevant data to JSON structure stored in event_data.json
 * Rewrites all events, to ensure catching updates
 * 
 * @param {*} url -> url to data to be scraped, assumes JSON data
 * @param {*} path -> filepath to write to
 */
async function scrapeData(url, path) {
  //existingIDs = await processExisting(path);
  existingIDs = new Set()
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
  const events = await response.json();
  fs.writeFileSync(path, OPENFILE);
  maxPage = events.meta.total_pages; //how many pages
  url = url + '?page=' //add ending to look at different pages
  for(let i = 1; i <= maxPage; i++){
    pageURL = url+i.toString();
    console.log(pageURL)
    if (i===1){
      //true means adding first event
      //this technically is the first page, but thats where event #1 will be
      //and we update the value in the scrapePage function after the first one is added
      //we want to know this for formatting reasons
      //ie so we  don't add a comma and newline before the first event
      existingIDs = await scrapePage(pageURL, path, existingIDs, true);
    }
    else{
      //false: not the very first page, so not the very first event
      existingIDs = await scrapePage(pageURL, path, existingIDs, false);
    }
  }
    fs.appendFileSync(path, CLOSEFILE);
}



/**
 * scrapePage
 * 
 * scrapes the content of one page of calendar.
 * 
 * @param {*} url -> url to page of data to be scraped
 * @param {*} path -> file to write to
 * @param {*} existingIDs -> set of IDs already reflected in JSON
 * @param {*} firstEvent -> boolean reflecting if we're adding the very first event
 * @returns Updated set of IDs associated with pre-existing events (post-hoc existingIDs)
 */
async function scrapePage(url, path, existingIDs, firstEvent) {
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    const events = await response.json();
    if (events.data && Array.isArray(events.data)) {
      counter = 0;
      events.data.forEach(event => {
        if (!existingIDs.has(event.id)){ //event not already listed
        existingIDs.add(event.id); //add id to list
        eventInfo = {}; //make dictionary to JSONify event
        eventInfo["Title"] = event.title;
        if (eventInfo.Title != null){
          eventInfo["Title"] = removeHTMLRelics(eventInfo.Title, false);
        }
        eventInfo["Date"] = event.date;
        eventInfo["Time"] = event.date_time;
        eventInfo["StartTimeISO"]= event.date_iso;
        eventInfo["EndTimeISO"]= event.date2_iso;
        allDayEnd = new Date(event.date2_iso) - new Date('1970-01-01T00:00:00.000Z') === 0;
        if (eventInfo.EndTimeISO===null || allDayEnd){
          eventInfo["AllDay?"] = true;
          eventInfo["EndTimeISO"] = new Date(new Date(event.date2_iso).setHours(23, 59, 0, 0))
        }
        else{
          eventInfo["AllDay?"] = false;
        }
        eventInfo["Location"] = event.location;
        if (eventInfo.Location != null){
          eventInfo["Location"] = removeHTMLRelics(eventInfo.Location, false);
        }
        eventInfo["Description"] = event.description ? event.description.replace(/<[^>]+>/g, '') : 'No description available';
        if (eventInfo.Description != null){
          eventInfo["Description"] = removeHTMLRelics(eventInfo.Description, false);
        }
        eventInfo["Audience"] = event.event_types_audience;
        if (eventInfo.Audience != null){
          eventInfo["Audience"] = removeHTMLRelics(eventInfo.Audience, true);
        }
        eventInfo["Org"] = event.custom_organization;
        if (eventInfo.Org != null){
          eventInfo["Org"] = removeHTMLRelics(eventInfo.Org, false);
        }
        eventInfo["Tags"]= event.tags;
        if (eventInfo.Tags != null){
          eventInfo["Tags"] = removeHTMLRelics(eventInfo.Tags, true);
        }
        eventInfo["ID"]= event.id;
        stringifyEvent = JSON.stringify(eventInfo);
        if (!firstEvent){ //not first event to be added ever
          stringifyEvent = ',\n'+stringifyEvent;
        }
        else{
          firstEvent = false; //no other event should be the first event
        }
        fs.appendFileSync(path, stringifyEvent);
        console.log("Adding "+ event.title + " " + event.id + " " + event.date)
        }
        counter++;
      }
    );
    }
    return existingIDs;
}

/**
 * dropPastEvents
 * 
 * removes elapsed events from JSON of events to be in app 
 * and writes their IDs to drop_ids.json, clearing all previous
 * contents.
 * 
 * @param {*} path -> filepath to update
 * @param {*} time_based -> boolean true if time based drop, false otherwise
 */
async function dropPastEvents(path,time_based){
  events = fs.readFileSync(path, 'utf-8');
  storedEvents = JSON.parse(events);
  lines = events.split('\n');
  console.log(lines.length);
  fs.writeFileSync(DROPPATH, OPENFILE); //write opening to JSON
  removableIndices = new Set(); //keep track of expired/irrelevant indices
  if (time_based){
    //record current time for comparison
    now = new Date(new Date().toLocaleString("en-US", { timeZone: "America/Chicago" }));
    console.log(now)
    if (lines.length <= 4){
      //we can close the file and end immediately if there is nothing in the events json
      //to drop, which would happen if there is only the JSON array formatting, no JSON 
      //entries
      fs.appendFileSync(DROPPATH, CLOSEFILE, function(err){
        if(err) throw err;
        console.log('WRITING TO JSON')
        });//write the end on
      return; //this implies there are no events listed 
      //bc all 4 lines would be the format ones. so no need to drop anything.
    }
    expiredEvents = 0; //count amount of expired events
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
          removableIndices.add(i);
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
              removableIndices.add(i);
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
    }}
  }
  else{//not time based, looking for events we have that just aren't on the site anymore
    currentIDs = processExisting(path);
    const response = await fetch(URL);
    if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    const events = await response.json();
    numPages = events.meta.total_pages;
    for (let i = 1; i <= numPages; i++){
      //go through, read, drop IDs from set, add to JSON
      localURL = URL+'?page=';
      localURL = localURL+i.toString();
      pageResponse = await fetch(localURL)
      if (!pageResponse.ok) throw new Error(`HTTP error! Status: ${response.status}`);
      const pageEvents = await pageResponse.json();
      pageEvents.data.forEach(event => {
        console.log(currentIDs.size)
        currentIDs.delete(event.id)
        console.log("removed "+event.id+ " from set")
        console.log(currentIDs.size)
      })
    }
    recordedCancelledEvents = currentIDs.size
    firstAdd = true;
    if (recordedCancelledEvents){//triggers if set is not empty
      //write them to the JSON of things to remove
      currentIDs.forEach(ID => {
        eventInfo = {}; //set up JSON entry
        eventInfo["ID"] = ID;
        eventStr = "";
        if (!firstAdd){
          eventStr = ",\n"
        }
        else{ 
          firstAdd = false;
        }
        eventStr = eventStr + JSON.stringify(eventInfo);
        fs.appendFileSync(DROPPATH,eventStr);
        //record index of line to drop from big JSON
        removableIndices.add((lines.findIndex(findID(ID)))-2);//to account for this being on actual lines
        //^the -2 is to account that this runs on text lines vs the actual JSON data
      }
      )
    }
  }
  // remove the lines associated with the expired events
  // minus 2 so we don't remove the brackets at the top but rather actual events
  updatedLines = lines.filter((_, i) => !removableIndices.has(i-2));
  fs.writeFileSync(path, updatedLines.join('\n'), 'utf-8');
  fs.appendFileSync(DROPPATH, CLOSEFILE);
  return;
}

/**
 * findID
 * 
 *  helper for dropping to find the line with correct index
 * 
 * @param {*} idNum -> idNum we are searching for
 * @returns function on a specific array index, to see if it has relevant ID field
 */
function findID(idNum){
  return function(line){
    try {
      const jsonLine = JSON.parse(line.trim().replace(/,$/, '')); // remove trailing comma if any
      return jsonLine.ID === idNum;
    } catch (error) {
      return false;
    }
  }
}

module.exports = { processExisting, scrapeData, URL, dropPastEvents, CIPATH, TRUEPATH, DROPPATH};
//scrapeData(url)