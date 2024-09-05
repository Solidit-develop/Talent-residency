
import "reflect-metadata"
import app from "./app"
import { AppDataSource } from "./database"
// require ("dotenv").config({path:"./src/.env"})
import config from "./config"

async function  main (){
    try{
        await AppDataSource.initialize()
        console.log("Database conected")
        const portserver = config.port
        app.listen(portserver)
        console.log("Servidor escuchando en el purto ", portserver)       

    }
    catch(error){
        console.log(error)
    }

}

main()
