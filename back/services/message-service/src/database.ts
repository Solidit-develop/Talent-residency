import { Providers } from "./entitis/provedores";
// import { Type } from "typescript";
import { users } from "./entitis/users";

import config from "./config";
import 'dotenv/config'
import { DataSource } from "typeorm";
import { userTypes } from "./entitis/typesUsers";
import { Messages } from "./entitis/messages";
import { Conversation } from "./entitis/conversation";

const host = config.host
const port = config.port
const user = config.user
const password= config.password
const database= config.database;

export const AppDataSource = new DataSource({
    type:'postgres',
    host:host,
    port:parseInt(port!),
    password:password,
    username:user,
    database:database,
    entities:[Providers,userTypes,users,Messages,Conversation],
    logging:true,
    synchronize:true    

})