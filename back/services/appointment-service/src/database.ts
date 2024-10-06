import { DataSource } from "typeorm";
import  config  from "./config";
import { Address } from "./entitis/adrdess";
import { State } from "./entitis/state";
import { Town } from "./entitis/town";
const DB_TYPE= config.db_type;
const host = config.host;
const port = config.port;
const user = config.user;
const password = config.password;
const database = config.database;


export const AppDataSource = new DataSource({
 type:'postgres',
 host:host,
 port:parseInt(port!),
 username:user,
 password:password,
 database:database,
//  entities:[State,Town,Address],
 logging:true,
 synchronize:true
})