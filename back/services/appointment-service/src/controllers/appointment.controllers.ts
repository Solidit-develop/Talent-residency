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
            const id_provider = req.params.id_provider
            const { id_customer,id_appointment } = req.body;
            const app = Number(id_appointment)
            const prov = Number(id_provider);
            const cus = Number(id_customer);

            console.log("Números de callback:", prov, cus,app);

            const relacion = await repositoryappointment.createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "Providers")
                .leftJoinAndSelect("appointment.users", "users")
                .andWhere("appointment.id_appointment =:id_appointment",{id_appointment:app})
                .where("users.id_user = :id_user", { id_user: cus })
                .andWhere("Providers.id_provider = :id_provider", { id_provider: prov })
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

    consultaProv:async(req:Request, res:Response):Promise<void>=>{
        try{
            const {id_provider}=req.params
            const prov = Number(id_provider);
            const Verificar = await repositoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "Providers")
            .leftJoinAndSelect("appointment.users", "users")
            .where("Providers.id_provider = :id_provider", { id_provider: prov })
            .getMany();

            const appProvider = Verificar.map(Verificar=>({             
                id_provider:Verificar.providers.id_provider,
                id_appintment:Verificar.id_appointment,
                AppointmentLocation:Verificar.AppointmentLocation,
                creationDate:Verificar.creationDate,
                apointmentDate:Verificar.apointmentDate,
                workshopName:Verificar.providers.workshopName,
                id_user:Verificar.users.id_user,
                name_user:Verificar.users.name_user,
                lastname:Verificar.users.lastname,   
                update:Verificar.statusAppointment
            }))
        
            res.status(200).json({appProvider})
        }catch(error){
            console.log(error)
            res.status(500).json({message:"Error de dentro del servidor"})
        }

    },

    consultaUser:async(req:Request,res:Response):Promise<void>=>{
        try{
            const id_customer = Number(req.params.id_user);
            let usuario=[];
            const appuser = await repositoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "Providers")
            .leftJoinAndSelect("appointment.users", "users")
            .where("users.id_user = :id_user", { id_user: id_customer })
            .getMany();

            let id_user            
            let name_user          
            let lastname            
            let update             
            let id_appintment      
            let AppointmentLocation
            let creationDate       
            let apointmentDate     
            let id_providers        
            let workshopName
            let name_provider
            let lastname_provider

            for(let i = 0; i< appuser.length;i ++){

                id_user = appuser[i].users.id_user,
                name_user = appuser[i].users.name_user,
                lastname = appuser[i].users.lastname,   
                update = appuser[i].statusAppointment,
                id_appintment = appuser[i].id_appointment,
                AppointmentLocation = appuser[i].AppointmentLocation,
                creationDate = appuser[i].creationDate,
                apointmentDate = appuser[i].apointmentDate,    
                id_providers = appuser[i].providers.id_provider,
                workshopName = appuser[i].providers.workshopName
                
                const appuser1 = await repositoryusers.createQueryBuilder("users")
                .leftJoinAndSelect("users.provedor","provedor")
                .where("provedor.id_provider = :id_provider", { id_provider:id_providers})
                .getOne();
                name_provider    = appuser1?.name_user
                lastname_provider = appuser1?.lastname

                let testinfo ={
                    id_user,                      
                    id_appintment, 
                    update,      
                    AppointmentLocation,
                    creationDate,       
                    apointmentDate,     
                    id_providers,       
                    name_provider,   
                    lastname_provider,
                    workshopName       
                }
                usuario.push(testinfo)
            } 

            res.status(200).json(usuario)

        }catch(e){
            console.log(e)
            res.status(500).json({mesage:"error interno en el servidor" })
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