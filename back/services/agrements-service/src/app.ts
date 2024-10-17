import express from 'express'
import morgan from 'morgan'

const app = express();

app.use(morgan('dev'));
app.use(express.json());

app.use(require("./routes/agrements.routes"));

export default app;