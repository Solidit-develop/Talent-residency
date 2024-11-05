import "reflect-metadata"
import app from "./app"
import { AppDataSource } from "./database"
// require ("dotenv").config({path:"./src/.env"})
import 'dotenv/config'

async function  main (){
    try{
        console.log("Inicio de la ejecución");
        await AppDataSource.initialize()
        console.log("Database conected")
        const portserver =process.env.PORT ||4001
        app.listen(portserver)
        console.log("Servidor escuchando en el purto ", portserver)       

    }
    catch(error){
        console.log(error)
    }

}

main()
