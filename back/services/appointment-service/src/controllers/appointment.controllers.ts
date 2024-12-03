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
            let { appointmentLocation, appointmentDate, creationDate} = req.body;
    
            console.log("Parámetros que llegan:");
            console.log({ appointmentLocation, appointmentDate, creationDate, id_provider, id_customer });
    
            
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
            cita.statusAppointment = "En espera";
    
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
            const { id_provider, id_customer,id_appointment } = req.params;
            let { creationDate } = req.body;

            const app = Number(id_appointment)
    
            const prov = Number(id_provider);
            const cus = Number(id_customer);
            // creationDate = new Date(creationDate)
            console.log("Números de callback:", prov, cus,app);

            if(!creationDate){
                console.log(creationDate)
                res.status(400).json("Ingresa la fecha de creacion del appointment")
                return;
            }
    
            const relacion = await repositoryappointment.createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "Providers")
                .leftJoinAndSelect("appointment.users", "users")
                .andWhere("appointment.id_appointment =:id_appointment",{id_appointment:app})
                .where("users.id_user = :id_user", { id_user: cus })
                .andWhere("Providers.id_provider = :id_provider", { id_provider: prov })
                .andWhere("CAST(appointment.creationDate AS TEXT) LIKE :fecha", { fecha: `%${creationDate}%` })
                .getOne();
    
            // console.log("Relación de usuarios:", relacion);
    
            // Verificar si la relación existe y si la fecha coincide
            if (relacion ) {
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
            .leftJoinAndSelect("appointment.users", "users")
            .where("Providers.id_provider = :id_provider", { id_provider: prov })
            .getMany();
            console.log("Consulta")
            // console.log(Verificar);

            const regreso = Verificar.map(Verificar=>({
                id_appintment:Verificar.id_appointment,
                AppointmentLocation:Verificar.AppointmentLocation,
                creationDate:Verificar.creationDate,
                apointmentDate:Verificar.apointmentDate,    
                id_provider:Verificar.providers.id_provider,
                workshopName:Verificar.providers.workshopName,
                id_user:Verificar.users.id_user,
                name_User:Verificar.users.name_User,
                lasname:Verificar.users.lasname,   
                update:Verificar.statusAppointment

            }))
    
        res.status(200).json({regreso})
        }catch(error){
            console.log(error)
            res.status(500).json({message:"Error de dentro del servidor"})
        }

    },
    
    actualizar: async (req: Request, res: Response): Promise<void> => {
        try {
            
            const id_appointment = Number(req.params.id_appointment);
            if (isNaN(id_appointment)) {
                res.status(400).json({ message: "ID de cita inválido" });
                return; 
            }
            const { appointmentLocation, appointmentDate } = req.body;
            const appointment = await repositoryappointment.findOne({ where: { id_appointment } });
            if (!appointment) {
                res.status(404).json({ message: "Cita no encontrada" });
                return; 
            }
            if (appointmentLocation) {
                appointment.AppointmentLocation = appointmentLocation;
            }
            if (appointmentDate) {
                appointment.apointmentDate = appointmentDate;
            }
            await repositoryappointment.save(appointment);
            console.log("Se actualizó este appointment");
            res.status(200).json({ message: "Se actualizó con éxito" });
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: "Error interno del servidor" });
        }
    }
    
}

export default controllerAppointment;