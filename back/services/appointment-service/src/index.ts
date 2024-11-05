import "reflect-metadata"

import app from './app'
import {AppDataSource} from "./database"

import 'dotenv/config'

async function main() {
    try{
        console.log("Intentando conectarse")
        await AppDataSource.initialize();
        console.log("Database conected")
        const portserver = process.env.PORT ||4005
        app.listen(portserver)
        console.log("Servidor escuchando en el puerdo",portserver)
    }catch(error){
        console.log(error)
    }
}
main();