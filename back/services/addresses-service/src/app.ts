import express from 'express';
import morgan from 'morgan';
import formData from 'express-form-data'; // Solo necesario si manejas multipart/form-data
// No es necesario body-parser si usas express en versión 4.16.0 o superior
// import bodyParser from 'body-parser'; 

import addressesRoutes from './routes/adresses.routes'; // Importa las rutas correctamente

const app = express();

// Middleware
app.use(morgan('dev'));
app.use(formData.parse());  // Para manejar multipart/form-data
// app.use(bodyParser.json());  // No es necesario si estás usando express (para JSON)

// Usa las rutas para el servidor
app.use(addressesRoutes);  // Es una buena práctica prefixar las rutas

export default app;
