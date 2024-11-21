
import { users } from "./entitis/users";

import config from "./config";
import 'dotenv/config'
import { DataSource } from "typeorm";
import { appointment } from "./entitis/appointment";
import { Providers } from "./entitis/provedores";
import { review } from "./entitis/review";
import { interaccion } from "./entitis/interaccion";



const host = config.host
const port = config.db_port
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
    entities:[review,users,appointment,Providers,interaccion],
    logging: ["error", "schema"],
    // logging:true,

    synchronize:false    

})