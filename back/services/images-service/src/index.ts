import "reflect-metadata"
import app from "./app"
// require ("dotenv").config({path:"./src/.env"})
import 'dotenv/config'

import { AppDataSource } from "./database";

async function main() {
    try {
        console.log("Inicio de la ejecución");
        await AppDataSource.initialize();
        console.log("Database conected")
        const portserver = process.env.portserver || 4009
        app.listen(portserver)
        console.log("Servidor escuchando en el purto ", portserver)

    }
    catch (error) {
        console.log(error)
    }

}

main()
