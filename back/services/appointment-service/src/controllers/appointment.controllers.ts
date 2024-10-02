import { json, Request } from "express";
import { Response } from "express";
import { Repository } from "typeorm";
import { AppDataSource } from "../database";

import { appointment } from "../entitis/appointment";
import { Providers } from "../entitis/provedores";
import { users } from "../entitis/users";


const repositoryappointment = AppDataSource.getRepository(appointment);
const repositoryproviders = AppDataSource.getRepository(Providers);
const repositoryusers = AppDataSource.getRepository(users);


const controllerAppointment ={


    prueba:async(req:Request,res:Response):Promise<void>=>{
        try{
            res.send("Pong")
            console.log("Pong")
        }catch(error){
            console.log(error)
        }
    },

    cita: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_provider, id_customer } = req.params;
            let { appointmentLocation, appointmentDate, creationDate, statusAppointment } = req.body;
            
            console.log("Parámetros que llegan:");
            console.log({ appointmentLocation, appointmentDate, creationDate, statusAppointment, id_provider, id_customer });
    
            // Buscar el cliente
            const cliente = await repositoryusers.findOne({ where: { id_user: Number(id_customer) } });
            console.log("Este es el cliente:", cliente);
    
            // Buscar el proveedor
            const proveedor = await repositoryproviders.findOne({ where: { id_provider: Number(id_provider) } });
            console.log("Esto es el proveedor:", proveedor);
    
            // Validar que existan los datos necesarios
            if (!proveedor || !cliente || !creationDate || !appointmentDate) {
                console.log("Se necesitan datos para continuar.");
                res.status(404).json({ message: "No se encontraron usuarios o faltan datos obligatorios." });
                return;
            }
    
            const date = new Date(creationDate)
             creationDate = date.toISOString().split('T')[0];
             appointmentDate = date.toISOString().split('T')[0];

            // Crear nueva cita
            const cita = new appointment();
            cita.providers = proveedor;
            cita.users = cliente;
            cita.creationDate = creationDate;
            cita.apointmentDate = appointmentDate;
            cita.AppointmentLocation = appointmentLocation;
            cita.statusAppointment = statusAppointment;

            console.log("CitasA guardadas")
            console.log(cita.creationDate, cita.apointmentDate)
    
            // Guardar la cita
            await repositoryappointment.save(cita);


            console.log("Se agendó una nueva cita con éxito.");
            res.status(200).json({ message: "Se registró la cita con éxito." });
    
        } catch (error) {
            console.error("Error interno del servidor:", error);
            res.status(500).json({ message: `Errores internos: ${error}` });
        }
    },
    
    
    cancelar: async (req: Request, res: Response): Promise<void> => {
        try{

            let { id_provider, id_customer } = req.params;

             const prov = Number(id_provider)
             const cus = Number(id_customer )
             
            console.log("Numeros de colback");
            console.log(prov,cus);

            const relacion = await repositoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers","Providers")
            .leftJoinAndSelect("appointment.users","users")
            .where("users.id_user=id_user",{id_user:cus})
            .andWhere("Providers.id_provider=id_provider",{id_provider:prov})
            .getOne();

            // logica para cambiar el estatus del appointment
             console.log("Relacion de usuarios");

            //  if(relacion){
            //     // relacion="Cancelado" 
            //     // acuerdo.statusAppointment
            //  }
             console.log(relacion);

             res.status(200).json({mesage:"actualizacion existosa"})

        }catch(error) {
            res.status(500).json({message:"Error interno"})
            console.log(error);
        }

    }
    

}

export default controllerAppointment;