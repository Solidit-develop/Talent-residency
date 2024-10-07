import { images } from "./entitis/images";
import { imagesRelation } from "./entitis/imagesRelation";
import config from "./config";
import { DataSource } from "typeorm";

const DB_TYPE = config.db_type
const host = config.host
const port = config.db_port
const user = config.user
const password = config.password
const database = config.database

export const AppDataSource = new DataSource({
    type: 'postgres',
    host: host,
    port: parseInt(port!),
    username: user,
    password: password,
    database: database,
    entities: [images, imagesRelation],
    logging: true,
    synchronize: true
})
