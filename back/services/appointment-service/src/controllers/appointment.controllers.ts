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
    },

    cita:async(req:Request, res:Response):Promise<void>=>{
        try{
            const {id_provider,id_pcustomer}=req.params
            const {appointmentLocation,appointmenDate}= req.body
            let creatioDate, statusAppointment;

            /* 
            no hay validacion para el acuerdo 
            los datos los mandan desde el front 
            esperando la confirmacion
            se crearan los datos por medio de una consukta donde se van a validad la relacion
            entre el usuario y el provedor 

            */

        }catch(errores){
            res.status(500).json({message:`errores internos ${errores}`})
            console.log(errores)
        }
       
    }

}

export default controllerAppointment;