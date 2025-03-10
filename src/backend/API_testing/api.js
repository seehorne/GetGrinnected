const express = require('express');
const app = express();

const port = process.env.PORT || 3000; // Use the port provided by the host or default to 3000
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});

const jwt = require('jsonwebtoken');

// TODO: goofy ass secret key
const secretKey = 'your-secret-key';

// Define a route to handle incoming requests
app.get('/', (req, res) => {
  res.send('Hello, Express!');
});

// In-memory data store (replace with a database in production)
const data = [
  { id: 1, name: 'Item 1' },
  { id: 2, name: 'Item 2' },
  { id: 3, name: 'Item 3' },
];

// Middleware to parse JSON requests
app.use(express.json());

// Create (POST) a new item
app.post('/items', (req, res) => {
  const newItem = req.body;
  data.push(newItem);
  res.status(201).json(newItem);
});

// Read (GET) all items
app.get('/items', (req, res) => {
  res.json(data);
});

// Read (GET) a specific item by ID
app.get('/items/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const item = data.find((item) => item.id === id);
  if (!item) {
    res.status(404).json({ error: 'Item not found' });
  } else {
    res.json(item);
  }
});

// Update (PUT) an item by ID
app.put('/items/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const updatedItem = req.body;
  const index = data.findIndex((item) => item.id === id);
  if (index === -1) {
    res.status(404).json({ error: 'Item not found' });
  } else {
    data[index] = { ...data[index], ...updatedItem };
    res.json(data[index]);
  }
});

// Delete (DELETE) an item by ID
app.delete('/items/:id', (req, res) => {
      const id = parseInt(req.params.id);
      const index = data.findIndex((item) => item.id === id);
      if (index === -1) {
        res.status(404).json({ error: 'Item not found' });
      } else {
        const deletedItem = data.splice(index, 1);
        res.json(deletedItem[0]);
      }
});

// Sample user data (replace with a real database)
// TODO: ok but what if I store the passwords in plaintext for now because there is already NO WAY we do it like this, this is NOT secure.
const users = [
  { id: 1, username: 'user1', password: 'password1' },
  { id: 2, username: 'user2', password: 'password2' },
];

// Function to verify user credentials
function authenticateUser(username, password) {
  const user = users.find((user) => user.username === username);
  if (!user) {
     return null; // User not found
  }
  if (password === user.password) {
    return user; // Password is correct
  }
  return null; // Password is incorrect
}

// Route to let users log in
app.post('/auth/login', (req, res) => {
  const { username, password } = req.body;
  console.log(`login attempt from user "${username}"`);
  const user = authenticateUser(username, password);

  if (!user) {
    return res.status(401).json({ error: 'Authentication failed' });
    console.log(`login attempt failed`);
  }

  const token = jwt.sign({ userId: user.id, username: user.username }, secretKey, {
        expiresIn: '1h', // Token expiration time
  });

  console.log(`login attempt succeeded`);
  res.json({ token });
});

function authenticateToken(req, res, next) {
  const token = req.header('Authorization');

  if (!token) {
    return res.status(401).json({ error: 'Authentication token missing' });
  }

  jwt.verify(token, secretKey, (err, user) => {
    if (err) {
      return res.status(403).json({ error: 'Token is invalid'});
    }
    req.user = user;
    next(); // Continue to the protected route
  });
}

// Example usage:
app.get('/protected', authenticateToken, (req, res) => {
  res.json({ message: 'This is a protected route', user: req.user });
});
