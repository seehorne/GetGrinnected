const fs = require("fs");
const url = 'https://events.grinnell.edu/live/json/events/response_fields/all/paginate'

async function scrapeData(url) {
  fetch(url)
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(events => {
    //console.log(events.data);
    event_info = {};
    counter = 0;
    if (events.data && Array.isArray(events.data)) {
      events.data.forEach(event => {
        event_info = {};
        event_info["Title"] = event.title;
        event_info["Date"] = event.date;
        event_info["Time"] = event.date_time;
        event_info["Location"] = event.location;
        event_info["Description"] = event.description ? event.description.replace(/<[^>]+>/g, '') : 'No description available';
        event_info["Audience"] = event.event_types_audience;
        event_info["Org"] = event.custom_organization;
        event_info["Tags"]= event.tags;
        event_info["ID"]= event.id;
        stringify_event = JSON.stringify(event_info);
        if (counter != 0){
            stringify_event = ',\n'+stringify_event;
        }
        counter++;
        fs.appendFile('event_data.json', stringify_event, function(err){
            if(err) throw err;
            console.log('WRITING TO JSON')
            });
      });
    }
  })
  .catch(error => {
    console.error('Error fetching the JSON data:', error);
  });
}

scrapeData(url)
  