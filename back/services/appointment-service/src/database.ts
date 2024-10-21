import { DataSource } from "typeorm";
import  config  from "./config";
import { appointment } from "./entitis/appointment";
import { Providers } from "./entitis/provedores";
import { users } from "./entitis/users";

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
 entities:[appointment,Providers,users],
 logging:true,
 synchronize:false
})