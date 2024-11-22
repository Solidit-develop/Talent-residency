import express from 'express';
import morgan from 'morgan';
import bodyParser from 'body-parser';
import formData from 'express-form-data'

const app = express();
app.use(morgan('dev'));
app.use(express.json());
app.use(formData.parse());
app.use(bodyParser.json());

app.use(require("./routes/appointment.routes"))

export default app;