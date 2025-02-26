import path from "path";

import 'dotenv/config'
import config from "./config";
import { DataSource } from "typeorm";

import { Estado } from "./entitis/estados";
import { Municipio } from "./entitis/municipios";
import { Localidad } from "./entitis/localidades";
// improt {Localidad}

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
    entities:[Estado,Localidad,Municipio],
    logging:true,
    // logging: ["error", "schema"],
    synchronize:true 
    
})
