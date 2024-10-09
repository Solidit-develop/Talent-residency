import { Request,Response } from "express"
import { AppDataSource } from "../database"

import { agrements } from "../entitis/agrements"
import { agrements_service } from "../entitis/agrements-service"
import { appointment } from "../entitis/appointment"
import { Providers } from "../entitis/provedores"
import { serviceStatus } from "../entitis/servicesStatus"


const agrementsRepository = AppDataSource.getRepository(agrements);
const agrements_serviceRepository = AppDataSource.getRepository(agrements_service)
const appointmentRepository = AppDataSource.getRepository(appointment)
const providersRepository = AppDataSource.getRepository(Providers);
const serviceRepository = AppDataSource.getRepository(serviceStatus)


const controlleragrements ={
    
    agregarAcuerdo:async (req:Request,res:Response):Promise<void> => {

        try{
            let {id_appointment,id_provedor}=req.body
            let description, creationDate, descriptionServicio,value
            if(id_appointment!= null || id_provedor!= null ){
                id_appointment = parseInt(id_appointment)
                id_provedor = parseInt(id_provedor);
                let agrement = await agrementsRepository.findOne({where:{providers:id_provedor,appointment:id_appointment}})
                console.log(agrement)

            } 

            
        }catch(error){
            console.log(error)
            res.status(500).json({message:"error interno en el servidor"})
        }
      
        


        
    }
    
}