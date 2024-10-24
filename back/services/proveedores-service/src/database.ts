import path from "path";
import { DataSource } from "typeorm";
import { Town } from "./entitis/town";
import { State } from "./entitis/state";
import { Address } from "./entitis/adrdess";
import { users } from "./entitis/users";
import { userTypes } from "./entitis/typesUsers";
import { Providers } from "./entitis/provedores";
import 'dotenv/config'
import { skills } from "./entitis/skill";
import config from "./config";
import { review } from "./entitis/review";
import { appointment } from "./entitis/appointment";
// import { statusAppointment } from "./entitis/statusAppointment";
import { Conversation } from "./entitis/conversation";
import { Messages } from "./entitis/messages";
import { agrements } from "./entitis/agrements";
import { agrements_service } from "./entitis/agrements-service";
import { serviceStatus } from "./entitis/servicesStatus";


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
    entities:[State,Town,Address,users,userTypes,Providers,skills,review,appointment,Conversation,Messages,agrements,agrements_service,serviceStatus],
    logging:true,
    synchronize:true 
})
