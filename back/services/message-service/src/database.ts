import { Providers } from "./entitis/provedores";
// import { Type } from "typescript";
import { users } from "./entitis/users";

import config from "./config";
import 'dotenv/config'
import { DataSource } from "typeorm";
import { userTypes } from "./entitis/typesUsers";
import { Messages } from "./entitis/messages";
import { Conversation } from "./entitis/conversation";
import { State } from "./entitis/state";
import { Town } from "./entitis/town";
import { Address } from "./entitis/adrdess";
import { skills } from "./entitis/skill";
import { review } from "./entitis/review";
import { appointment } from "./entitis/appointment";
import { statusAppointment } from "./entitis/statusAppointment";



const host = config.host
const port = config.db_port
const user = config.user
const password= config.password
const database= config.database;

console.log(host,port,user,password,database);

export const AppDataSource = new DataSource({
    type:'postgres',
    host:host,
    port:parseInt(port!),
    password:password,
    username:user,
    database:database,
    entities:[Messages,Conversation,State,Town,Address,users,userTypes,Providers,skills,review,appointment,statusAppointment],
    logging:true,
    synchronize:true    

})