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

    ping:async(req:Request,res:Response):Promise<void>=>{
        try{

            console.log("Prueba de conexion")
            res.status(200).json({mesage:"pong"})
        }catch{
            res.status(500).json({mesage:"error de conexion"})
        }
    },

    agregarAcuerdo: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_appointment, id_provedor } = req.params;
            const { descripcion, creationDate, descripcionService, creacionDateService } = req.body;
            let stats = 'Agendado'
            // Convertir parámetros a números
            const appointmentId = Number(id_appointment);
            const providerId = Number(id_provedor);
    
            if (id_appointment && id_provedor) {
                // Buscar la cita primero
                const cita = await appointmentRepository.createQueryBuilder("appointment")
                    .leftJoinAndSelect("appointment.users", "users")
                    .leftJoinAndSelect("appointment.providers", "providers")
                    .where("providers.id_provider = :id_provider", { id_provider: providerId })
                    .andWhere("appointment.id_appointment = :id_appointment", { id_appointment: appointmentId })
                    .getOne();
    
                // Buscar el proveedor
                const prov = await providersRepository.findOne({ where: { id_provider: providerId } });
                const canselado = cita?.statusAppointment

                console.log(canselado)

                if (cita && prov ) {
                    if(canselado != 'Cancelado'){
                        console.log("Se encontró la relación");
                    const acuerdo = new agrements(); // Asegúrate de que el nombre de la clase esté en mayúscula
                    acuerdo.description = descripcion;
                    acuerdo.creationDate = creationDate;
                    acuerdo.providers = prov;
                    acuerdo.appointment = cita;
                    await agrementsRepository.save(acuerdo);
                    console.log("Se agrego el acuerdo")
    
                    const estatus = new serviceStatus ();
                    estatus.description = descripcionService;
                    estatus.value = stats;
                    await serviceRepository.save(estatus);
                    console.log("Se agrego el estatus")
    
                    const servicios = new agrements_service();
                    servicios.description = descripcion;
                    servicios.creationDate = creacionDateService;
                    servicios.agrements = acuerdo;
                    servicios.serviceStatus = estatus;
                    await agrements_serviceRepository.save(servicios);
                    console.log("Se agrego el servucio")

                    cita.statusAppointment = stats ;
                    await appointmentRepository.save(cita)

    
                    // Guardar el acuerdo
                    res.status(200).json({ message: "Se guardaron todos los datos de la cita" });
                    return;
                    
                    }else{
                        res.status(400).json({message:"El acuerdo esta Cancelado"})
                        return
                    }
                    
                }
    
                console.log(cita);
                res.status(404).json({ message: "Cita o proveedor no encontrados" });
            } else {
                res.status(400).json({ message: "Faltan parámetros" });
            }
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: "Error interno en el servidor" });
        }
    },
    
    serviciosProvedor: async(req:Request,res:Response):Promise<void>=>{
     const {id_provedor}=req.params
     const id_pr = parseInt(id_provedor)

     const cita = await appointmentRepository.createQueryBuilder("appointment")
     .leftJoinAndSelect("appointment.users", "users")
     .leftJoinAndSelect("appointment.providers", "providers")
     .leftJoinAndSelect("appointment.agrements","agrements")
     .leftJoinAndSelect("agrements.agrements_service","agrements_service")
     .leftJoinAndSelect("agrements_service.serviceStatus","status")
     .where("providers.id_provider = :id_provider", { id_provider: id_pr })
     .getMany();

     if(cita.length<=0){
        console.log("No se encontraron las citas")
        res.status(404).json({message:"no se entontraron citas"})
        return
        }

        const mappedCitas = cita.map(cita => ({
            idAppointment: cita.id_appointment,
            creationDate: cita.creationDate,
            appointmentDate: cita.apointmentDate,
            appointmentLocation: cita.AppointmentLocation,
            statusAppointment: cita.statusAppointment,
            user: {
            idUser: cita.users.id_user,
            name: cita.users.name_User,
            lastName: cita.users.lasname,
            phoneNumber: cita.users.phoneNumber
            },
            agreements: cita.agrements.map((agreement: { id_agements: any; description: any; creationDate: any; agrements_service: any[] }) => ({
            idAgreement: agreement.id_agements,
            description: agreement.description,
            creationDate: agreement.creationDate,
            
            agreementsService: agreement.agrements_service.map((service: { id_agrements_service: any; description: any; creationDate: any; serviceStatus: { id_serviceStatus: any; description: any; value: any } }) => ({
                idAgreementService: service.id_agrements_service,
                description: service.description,
                creationDate: service.creationDate,

                serviceStatus: {
                idServiceStatus: service.serviceStatus.id_serviceStatus,
                description: service.serviceStatus.description,
                value: service.serviceStatus.value
                }
            }))
            }))
        }));

        console.log(mappedCitas);
        

        res.status(200).json({citas:mappedCitas})
    },

    updateAgrement:async(req:Request, res:Response):Promise<void>=>{
        try{
            const id_agrement= Number(req.params.id_agrement)
            const id_serviceStatus = Number(req.params.id_serviceStatus)

            const {descripcion,descripcionService}= req.body

            const agrements  = await agrementsRepository.findOne({where:{id_agements:id_agrement}})
            const serviceStatus = await serviceRepository.findOne({where:{id_serviceStatus:id_serviceStatus}})
            if(!agrements || !serviceStatus){
                console.log("Intento de params" , id_agrement)
                res.status(406).json({message:"id_appointment Not Acceptable"})
                return;
            }

            if(serviceStatus){
                serviceStatus.description= descripcionService
                await serviceRepository.save(serviceStatus)
            }

            if(agrements){
                agrements.description=descripcion
                await agrementsRepository.save(agrements)
            }            

            res.status(200).json({message:"Actualizado"})

        }catch (e){
            console.log(e)
            res.status(500).json({message:"Error interno en el servidor"})
        }
        
    },
    cancelar:async(req:Request, res:Response):Promise<void>=>{
        const id_agrement= Number(req.params.id_agrement)
        const id_serviceStatus = Number(req.params.id_serviceStatus)
        let value = "Cancelado"
        try{
            const agrements  = await agrementsRepository.findOne({where:{id_agements:id_agrement}})
            if(agrements && value){
                let serviceStatus = await serviceRepository.findOne({where:{id_serviceStatus:id_serviceStatus}})
                if (serviceStatus){
                    serviceStatus.value= value
                    serviceRepository.save(serviceStatus);
                }                            
            }else{
                console.log("No se encontro nada")
                res.json("No se encontro nada")
                return;
            }
            res.status(200).json({message:"Actualizado con exito"})
        }catch(error){
            console.log("Error interno" , error)
            res.status(500).json("Error interno")
        }
    } 
    
}

export default controlleragrements;