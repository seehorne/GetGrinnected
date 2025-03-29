const fs = require("fs");
const url = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'

function processExisting(){
  try{
    events = fs.readFileSync('event_data.json', 'utf-8');
    current_events = JSON.parse(events)
      existingIDs = new Set();
      if (current_events.data && Array.isArray(current_events.data)){
        current_events.data.forEach(event =>{
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
    existing_events = fs.readFileSync('event_data.json', 'utf-8');
    lines = existing_events.split('\n');
    updatedLines = lines.slice(0, -2); //remove last two lines
    fs.writeFileSync('event_data.json', updatedLines.join('\n'));
    counter = 0;
    if (events.data && Array.isArray(events.data)) {
      events.data.forEach(event => {
        if (!existingIDs.has(event.id)){
        existingIDs.add(event.id);
        event_info = {};
        event_info["Title"] = event.title;
        event_info["Date"] = event.date;
        event_info["Time"] = event.date_time;
        event_info["StartTimeISO"]= event.date_iso;
        event_info["EndTimeISO"]= event.date_iso;
        if (event_info.StartTimeISO===null){
          event_info["AllDay?"] = true;
        }
        else{
          event_info["AllDay?"] = false;
        }
        event_info["Location"] = event.location;
        event_info["Description"] = event.description ? event.description.replace(/<[^>]+>/g, '') : 'No description available';
        event_info["Audience"] = event.event_types_audience;
        event_info["Org"] = event.custom_organization;
        event_info["Tags"]= event.tags;
        event_info["ID"]= event.id;
        stringify_event = JSON.stringify(event_info);
        if (counter != 0 || lines.length >= 2){ //not first event to be added ever
            stringify_event = ',\n'+stringify_event;
        }
        counter++;
        appendPromises.push(fs.appendFile('event_data.json', stringify_event, function(err){
          if(err) throw err;
          console.log('WRITING TO JSON')
          }));
        }
      }
    );
    }
    await Promise.all(appendPromises);
    const close_file = '\n]\n}'
    fs.appendFile('event_data.json', close_file, function(err){
      if(err) throw err;
      console.log('WRITING TO JSON')
      });
  })
  .catch(error => {
    console.error('Error fetching the JSON data:', error);
  });
}

scrapeData(url)
  