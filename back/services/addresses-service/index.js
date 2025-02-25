// const pool = require("./database");
const app = require("./app.js");
require("dotenv").config({ path: "./.env" });

// console.log(process.env)
const puerto = process.env.PORT|| 3002;

app.listen(puerto,'0.0.0.0', () => {
  console.log("servidor arriba en el puerto " + puerto);
});
