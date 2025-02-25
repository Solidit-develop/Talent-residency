const express = require("express");
const morgan = require("morgan");
const path = require("path");
const app = express();
const cors= require('cors')
require('dotenv').config({ path: './.env' });

// console.log(process.env)

app.set("port", process.env.PORT || 3002);
app.use(cors());
app.use(express.json())
app.use(morgan('dev'))
app.use(express.urlencoded({extended:false}))

app.use("/estados",require("./routes/adresses.routes"))

module.exports = app;