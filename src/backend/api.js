const express = require('express');
const fs = require('fs');
const db = require('./db_connect.js');

// Listen on port 5844 by default
const app = express();
const port = process.env.PORT || 5844;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});

// Middleware to parse JSON requests
app.use(express.json());

// GET all events
// TODO: we want to be able to filter events, likely using queries.
// TODO: for all these methods, we need to figure out some system of authorization or authentication. I forget which.
app.get('/events', (req, res) => {
  const events = db.getEvents();
  res.json(events);
});

// app.get('/events/:id', (req, res) => {
//   const id = parseInt(req.params.id);
//   const item = get_item_by_id(id);
//   if (!item) {
//     res.status(404).json({ error: 'Item not found' });
//   } else {
//     res.json(item);
//   }
// });
//
// function getItemByID(id) {
//   return event_data.find((item) => item.ID === id);
// }
