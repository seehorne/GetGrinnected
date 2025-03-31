const fs = require("fs");
const url = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'

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
    updatedLines = lines.slice(0, -2); //remove last two lines
    fs.writeFileSync('./src/backend/event_data.json', updatedLines.join('\n'));
    counter = 0;
    if (events.data && Array.isArray(events.data)) {
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
        if (counter != 0 || lines.length >= 2){ //not first event to be added ever
            stringifyEvent = ',\n'+stringifyEvent;
        }
        counter++;
        appendPromises.push(fs.appendFile('./src/backend/event_data.json', 
          stringifyEvent, function(err){
            if(err) throw err;
            console.log('WRITING TO JSON')
          }));
        }
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

module.exports = { processExisting, scrapeData, url};
//scrapeData(url)
  