import { images } from "./entitis/images";
import { imgenRelation } from "./entitis/imagesRelation";
import config from "./config";
import { DataSource } from "typeorm";

const DB_TYPE= config.db_type
const host= config.host
const port = config.port
const user= config.user
const password = config.password
const database= config.database

export const AppDataSource = new DataSource({
    type: 'postgres',
    host: host,
    port: parseInt(port!),
    username: user,
    password: password,
    database: database,
    entities:[images,imgenRelation],
    logging:true,
    synchronize:true 
})
