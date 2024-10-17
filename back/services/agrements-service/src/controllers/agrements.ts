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
            let stats = 'Tomado'
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
    
                if (cita && prov) {
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
    
                    // Guardar el acuerdo
                    res.status(200).json({ message: "Se guardaron todos los datos de la cita" });
                    return;
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

    //  const result = {
    //     citas: citas.map((cita: { users: { id_user: any; name_User: any; lasname: any; email: any; age: any; phoneNumber: any }; agrements: any[] }) => ({
    //         users: {
    //             id_user: cita.users.id_user,
    //             name_User: cita.users.name_User,
    //             lasname: cita.users.lasname,
    //             email: cita.users.email,
    //             age: cita.users.age,
    //             phoneNumber: cita.users.phoneNumber,
    //         },
    //         agrements: cita.agrements.map((acuerdo: { id_agements: any; description: any; creationDate: any; agrements_service: any[] }) => ({
    //             id_agements: acuerdo.id_agements,
    //             description: acuerdo.description,
    //             creationDate: acuerdo.creationDate,
    //             agrements_service: acuerdo.agrements_service.map((servicio: { id_agrements_service: any; description: any; creationDate: any; serviceStatus: { id_serviceStatus: any; description: any; value: any } }) => ({
    //                 id_agrements_service: servicio.id_agrements_service,
    //                 description: servicio.description,
    //                 creationDate: servicio.creationDate,
    //                 serviceStatus: {
    //                     id_serviceStatus: servicio.serviceStatus.id_serviceStatus,
    //                     description: servicio.serviceStatus.description,
    //                     value: servicio.serviceStatus.value,
    //                 },
    //             })),
    //         })),
    //     })),
    // };
    
    if(!cita){
        console.log("No se encontraron las citas")
        res.status(400).json({message:"no se entontraron citas"})
        
    }
    res.status(200).json({citas:cita})

    }
    
}

export default controlleragrements;