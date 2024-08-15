import path from "path";
import { DataSource } from "typeorm";
import { Town } from "./entitis/town";
import { State } from "./entitis/state";
import { Address } from "./entitis/adrdess";
import { users } from "./entitis/users";
import { userTypes } from "./entitis/typesUsers";
require ("dotenv").config({path:"./src/.env"})

const DB_TYPE= process.env.DB_TYPE|| 'postgres'
const host= process.env.DB_HOST
const port = 5432
const user= process.env.DB_USER
const password = process.env.DB_PASSWORD
const database= process.env.DB

export const AppDataSource = new DataSource({
    type: 'postgres',
    host: host,
    port: port,
    username: user,
    password: password,
    database: database,
    entities:[State,Town,Address,users,userTypes],
    logging:true,
    synchronize:true 
})
