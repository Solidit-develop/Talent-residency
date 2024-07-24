import "reflect-metadata"
import app from "./app"
import { AppDataSource } from "./database"
require ("dotenv").config({path:"./src/.env"})

async function  main (){
    try{
        await AppDataSource.initialize()
        console.log("Database conected")
        const portserver =process.env.portserver ||4001
        app.listen(portserver)
        console.log("Servidor escuchando en el purto ", portserver)       

    }
    catch(error){
        console.log(error)
    }

}

main()
