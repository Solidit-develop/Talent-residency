import { Request } from "express";
import { Response } from "express";
const controllerAppointment ={

    prueba:async(req:Request,res:Response):Promise<void>=>{
        try{
            res.send("Pong")
            console.log("Pong")
        }catch(error){
            console.log(error)
        }
    }

}

export default controllerAppointment;