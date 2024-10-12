import { json, Request } from "express";
import { Response } from "express";
import { JoinColumn, Repository } from "typeorm";
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
    
            
            const cliente = await repositoryusers.findOne({ where: { id_user: Number(id_customer) } });
            console.log("Este es el cliente:", cliente);
    
           
            const proveedor = await repositoryproviders.findOne({ where: { id_provider: Number(id_provider) } });
            console.log("Esto es el proveedor:", proveedor);
    
            
            if (!proveedor || !cliente || !creationDate || !appointmentDate) {
                console.log("Se necesitan datos para continuar.");
                res.status(404).json({ message: "No se encontraron usuarios o faltan datos obligatorios." });
                return;
            }
    
            
            const creationDateObj = new Date(creationDate);
            const appointmentDateObj = new Date(appointmentDate);
    
            
            if (isNaN(creationDateObj.getTime()) || isNaN(appointmentDateObj.getTime())) {
                console.log("Las fechas proporcionadas no son válidas.");
                res.status(400).json({ message: "El formato de fecha no es válido." });
                return;
            }
    
           
            const cita = new appointment();
            cita.providers = proveedor;
            cita.users = cliente;
            cita.creationDate = creationDateObj;  // Directamente como tipo Date
            cita.apointmentDate = appointmentDateObj;  
            cita.AppointmentLocation = appointmentLocation;
            cita.statusAppointment = statusAppointment;
    
            console.log("Citas guardadas");
            console.log(cita.creationDate, cita.apointmentDate);
    
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
        try {
            const { id_provider, id_customer } = req.params;
            const { creationDate } = req.body;
    
            const prov = Number(id_provider);
            const cus = Number(id_customer);
    
            console.log("Números de callback:", prov, cus);
    
            const relacion = await repositoryappointment.createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "Providers")
                .leftJoinAndSelect("appointment.users", "users")
                .where("users.id_user = :id_user", { id_user: cus })
                .andWhere("Providers.id_provider = :id_provider", { id_provider: prov })
                .getOne();
    
            console.log("Relación de usuarios:", relacion);
    
            // Verificar si la relación existe y si la fecha coincide
            if (relacion && relacion.creationDate === creationDate) {
                relacion.statusAppointment = "Cancelado"; // Cambia  correctamente el estatus
                await repositoryappointment.save(relacion);
                res.status(200).json({ message: "Actualización exitosa" });
            } else {
                res.status(400).json({ message: "No se encontró la relación o la fecha no coincide" });
            }
        } catch (error) {
            res.status(500).json({ message: "Error interno" });
            console.log(error);
        }
    },

    consulta:async(req:Request, res:Response):Promise<void>=>{
        try{
            const {id_provider}=req.params
            const prov = Number(id_provider);
            const Verificar = await repositoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "Providers")
            .where("Providers.id_provider = :id_provider", { id_provider: prov })
            .getMany();
            console.log("Consulta")
            console.log(Verificar);
    
        res.status(200).json({Verificar})
        }catch(error){
            console.log(error)
            res.status(500).json({message:"Error de dentro del servidor"})
        }

    },
    

    
    

}

export default controllerAppointment;