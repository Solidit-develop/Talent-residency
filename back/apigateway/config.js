const dotenv = require('dotenv');

// Cargar las variables de entorno desde el archivo .env
dotenv.config();

module.exports = {
  port: process.env.PORT || 4000,
  host: process.env.HOST || localhost
};
