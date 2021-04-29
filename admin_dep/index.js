'use strict';
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const config = require('./config');
const studentRoutes = require('./routes/stroutes');

const app = express();

app.use(express.json());
app.use(cors());
app.use(express.urlencoded({extended: true}))
app.use(express.static('public'))




app.use('/', studentRoutes.routes);


app.listen(config.port, () => console.log('App is listening on url http://localhost:' + config.port));
