import 'reflect-metadata';
import express from 'express';
import { json } from 'body-parser';
import { userRouter } from './src/api/controller/userController';
import { connectDatabase } from './src/persitence/database';
import config from './config';

const app = express();

app.use(json());
app.use('/users', userRouter);

const PORT = config.PORT;

connectDatabase().then(() => {
    app.listen(PORT, () => {
        console.log(`Server is running on port ${PORT}`);
    });
}).catch(err => {
    console.error('Failed to connect to the database', err);
});
