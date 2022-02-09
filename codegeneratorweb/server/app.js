var express = require('express');
var path = require('path');

var app = express();

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});
app.get('/favicon.ico', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'favicon.ico'));
});
app.get('/logo192.png', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'logo192.png'));
});
app.get('/logo512.png', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'logo512.png'));
});

app.use(express.static(path.join(__dirname, 'public')));

module.exports = app;
