import path from "path";
import { DataSource } from "typeorm";
import { Town } from "./entitis/town";
import { State } from "./entitis/state";
import { Address } from "./entitis/adrdess";
import { users } from "./entitis/users";
import { userTypes } from "./entitis/typesUsers";
import { Providers } from "./entitis/provedores";
// require ("dotenv").config({path:"./src/.env"})
import 'dotenv/config'
import { skills } from "./entitis/skill";
import config from "./config";

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
    entities:[State,Town,Address,users,userTypes,Providers,skills],
    logging:true,
    synchronize:true 
})
