import path from "path";
import { DataSource } from "typeorm";
import { agrements } from "./entitis/agrements";
import { agrements_service } from "./entitis/agrements-service";
import { appointment } from "./entitis/appointment";
import { Providers } from "./entitis/provedores";
import { serviceStatus } from "./entitis/servicesStatus";


import 'dotenv/config'
import config from "./config";

const DB_TYPE= config.db_type
const host= config.host
const port = config.db_port
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
    entities:[agrements,agrements_service,appointment,Providers,serviceStatus],
    logging:true,
    synchronize:false 
})
