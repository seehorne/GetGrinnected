const express = require('express');
const fs = require('fs');

// Listen on port 5844 by default
const app = express();
const port = process.env.PORT || 5844;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});

// TODO: this is a very temporary part, we need to talk to the database.
// it is also synchronous for now since we can't continue without it.
const datafile = process.env.DATAFILE || 'event_data.json';
const event_data = JSON.parse(fs.readFileSync(datafile, 'utf-8')).data;

// Middleware to parse JSON requests
app.use(express.json());

// GET all events
// TODO: we want to be able to filter events, likely using queries.
// TODO: for all these methods, we need to figure out some system of authorization or authentication. I forget which.
app.get('/events', (req, res) => {
  res.json(event_data);
});

app.get('/events/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const item = get_item_by_id(id);
  if (!item) {
    res.status(404).json({ error: 'Item not found' });
  } else {
    res.json(item);
  }
});

function get_item_by_id(id) {
  return event_data.find((item) => item.ID === id);
}
