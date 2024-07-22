import { createConnection } from 'typeorm';
import config from '../../config';
import { User } from '../shared/models/UserModel';

export const connectDatabase = async () => {
    await createConnection({
        type: 'postgres',
        host: config.DB_HOST,
        port: parseInt(config.DB_PORT!),
        username: config.DB_USERNAME,
        password: config.DB_PASSWORD,
        database: config.DB_DATABASE,
        entities: [User],
        synchronize: true,
    });
};
