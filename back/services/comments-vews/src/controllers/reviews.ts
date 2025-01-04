import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { Any, QueryBuilder, Repository, Timestamp, View } from "typeorm";
import { interaccion } from "../entitis/interaccion";
import { Providers } from "../entitis/provedores";
import { review } from "../entitis/review";
import { appointment } from "../entitis/appointment";

import { ImagenService } from "../services/imagenService"
import app from "../app";
// import { images } from "../entitis/images";
// import { imagesRelation } from "../entitis/imagesRelation";

const repositoryuser = AppDataSource.getRepository(users);
const repositoryinteraccion = AppDataSource.getRepository(interaccion);
const repositoryprovedor = AppDataSource.getRepository(Providers);
const repositoryreview = AppDataSource.getRepository(review);
const reppsitoryappointment = AppDataSource.getRepository(appointment);
// const repositoryMessages = AppDataSource.getRepository(images);
// const repositoryimagenRelacion  = AppDataSource.getRepository(imagesRelation);

const controllersReview = {

    ping: async (req: Request, res: Response): Promise<void> => {
        res.send("pong");
    },

    registro_user: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_user, id_prov } = req.params;
            const {
                calificacion,
                comment, // Corregido de "commentario"
                urlLocation
            } = req.body;

            const id_usuario = Number(id_user);
            const id_provedor = Number(id_prov);
            let table = "review"
            let funcionality = "comment"



            console.log("IDs recibidos:", { id_usuario, id_provedor });

            console.log("Iniciando consulta...");
            const verificar = await reppsitoryappointment
                .createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "providers")
                .leftJoin("appointment.users", "users")
                .where(
                    "(appointment.users = :id_logued OR appointment.providers = :id_prov) OR (appointment.users = :id_prov OR appointment.providers = :id_logued)",
                    { id_logued: id_usuario, id_prov: id_provedor }
                )
                .getOne();
    
            if (!verificar) {
                res.status(404).json({ message: "No se encontró un acuerdo válido" });
                console.log("Consulta fallida: No se encontró appointment");
                return;
            }
    
            const { id_appointment: id_app, providers } = verificar;
            const id_prove = providers.id_provider;
    
            console.log("ID de appointment encontrado:", id_app);
            console.log("Proveedor en la consulta:", id_prove, "Proveedor recibido:", id_provedor);
    
            let origenComoUser = false;
    
            if (id_provedor === id_prove) {
                origenComoUser = true;
                console.log("Se registra el comentario como proveedor");
            }
    
            const acuerdo = await reppsitoryappointment.findOne({
                where: { id_appointment: id_app }
            });
    
            if (!acuerdo) {
                res.status(400).json({ message: "No se encontró el acuerdo" });
                console.log("No se encontró el appointment con los IDs relacionados.");
                return;
            }
    
            console.log("Procesando registro de interacción...");
    
            const inter = await AppDataSource.createQueryBuilder()
                .select("interaccion")
                .from("interaccion", "interaccion")
                .where("interaccion.appointmentIdAppointment = :id_appointment", { id_appointment: id_app })
                .getOne();

                const interaccionEntity = await AppDataSource.getRepository(interaccion).findOne({
                    where: { id_interaccion: inter?.id_interaccion }
                });
    
            console.log("Resultado de la interacción encontrada:", inter);
    
            const comentario = new review();
            comentario.comment = comment; // Variable corregida
            comentario.calificacion = calificacion;


            const idUsedOn = inter?.id_interaccion

            console.log("Interacciones-..-.-.-.-.-.-.-.-.-.-..-.-.-.-.-.-.-.--.-.-.")
            console.log(urlLocation,funcionality,idUsedOn)

            console.log(idUsedOn)
            if(idUsedOn  && urlLocation){


            const conexion = new ImagenService();
             await conexion.PostImage({funcionality,urlLocation,idUsedOn},table)
             console.log("Se guardó con éxito:", JSON.stringify(conexion));
            }

            if (interaccionEntity) {
                comentario.interacciones = [interaccionEntity];
            }
    
            await repositoryreview.save(comentario);
    
            if(!inter){
                const interaction = new interaccion();
                interaction.origenEmitidoComoUser = origenComoUser;
                interaction.reviews = [comentario];
                interaction.appointment = acuerdo;
                await repositoryinteraccion.save(interaction);
            }
           
    
            console.log("Registro exitoso, calificación agregada.");
            res.status(200).json({ message: "Registro con éxito" });
    
        } catch (error) {
            console.error("Error en registro_user:", error);
            res.status(500).json({ error: "Error interno del servidor", detalles: error });
        }
    },
    
    registro_user2: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_user, id_prov } = req.params;
            const {
                calificacion,
                comment, // Corregido de "commentario"
                urlLocation
            } = req.body;

            const id_usuario = Number(id_user);
            const id_provedor = Number(id_prov);
            let table = "review"
            let funcionality = "comment"



            console.log("IDs recibidos:", { id_usuario, id_provedor });

            console.log("Iniciando consulta...");



            const verificar = await reppsitoryappointment
                .createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "providers")
                .leftJoin("appointment.users", "users")
                .where(
                    "(appointment.users = :id_logued OR appointment.providers = :id_prov) OR (appointment.users = :id_prov OR appointment.providers = :id_logued)",
                    { id_logued: id_usuario, id_prov: id_provedor }
                )
                .getOne();


                
    
            if (!verificar) {
                res.status(404).json({ message: "No se encontró un acuerdo válido" });
                console.log("Consulta fallida: No se encontró appointment");
                return;
            }
    
            const { id_appointment: id_app, providers } = verificar;
            const id_prove = providers.id_provider;
    
            console.log("ID de appointment encontrado:", id_app);
            console.log("Proveedor en la consulta:", id_prove, "Proveedor recibido:", id_provedor);
    
            let origenComoUser = false;
    
            if (id_provedor === id_prove) {
                origenComoUser = true;
                console.log("Se registra el comentario como proveedor");
            }
    
            const acuerdo = await reppsitoryappointment.findOne({
                where: { id_appointment: id_app }
            });
    
            if (!acuerdo) {
                res.status(400).json({ message: "No se encontró el acuerdo" });
                console.log("No se encontró el appointment con los IDs relacionados.");
                return;
            }
    
            console.log("Procesando registro de interacción...");
    
            const inter = await AppDataSource.createQueryBuilder()
                .select("interaccion")
                .from("interaccion", "interaccion")
                .where("interaccion.appointmentIdAppointment = :id_appointment", { id_appointment: id_app })
                .getOne();

                const interaccionEntity = await AppDataSource.getRepository(interaccion).findOne({
                    where: { id_interaccion: inter?.id_interaccion }
                });
    
            console.log("Resultado de la interacción encontrada:", inter);
    
            const comentario = new review();
            comentario.comment = comment; // Variable corregida
            comentario.calificacion = calificacion;


            const idUsedOn = inter?.id_interaccion

            console.log("Interacciones-..-.-.-.-.-.-.-.-.-.-..-.-.-.-.-.-.-.--.-.-.")
            console.log(urlLocation,funcionality,idUsedOn)

            console.log(idUsedOn)
            if(idUsedOn  && urlLocation){


            const conexion = new ImagenService();
             await conexion.PostImage({funcionality,urlLocation,idUsedOn},table)
             console.log("Se guardó con éxito:", JSON.stringify(conexion));
            }

            if (interaccionEntity) {
                comentario.interacciones = [interaccionEntity];
            }
    
            await repositoryreview.save(comentario);
    
            if(!inter){
                const interaction = new interaccion();
                interaction.origenEmitidoComoUser = origenComoUser;
                interaction.reviews = [comentario];
                interaction.appointment = acuerdo;
                await repositoryinteraccion.save(interaction);
            }
           
    
            console.log("Registro exitoso, calificación agregada.");
            res.status(200).json({ message: "Registro con éxito" });
    
        } catch (error) {
            console.error("Error en registro_user:", error);
            res.status(500).json({ error: "Error interno del servidor", detalles: error });
        }
    },
    

    consultaUno:async(req:Request,res:Response):Promise<void>=>{
        try{
            let id_prov=Number(req.params.id_prov)
            let user = Number(req.params.id_user)

            

            const verificar = await AppDataSource.createQueryBuilder()
            .select("appointment")
            .from("appointment", "appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoinAndSelect("appointment.users", "users")
            .leftJoinAndSelect("appointment.interaccion", "interaccion")
            .leftJoinAndSelect("interaccion.reviews", "review")
            .where("(appointment.usersIdUser = :id_logued AND appointment.providersIdProvider = :id_prov)", {
                id_logued: user,
                id_prov: id_prov,
            })
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .getOne()

            
                let id_user = verificar?.users.id_user
                let name_user=verificar?.users?.name_user
                let lastname=verificar?.users?.lastname
                let origenEmitidoComoUser=verificar?.interaccion?.origenEmitidoComoUser
                
                let id_provider=verificar?.providers?.id_provider
                
                let id_interaccion=verificar?.interaccion?.id_interaccion

                let id_review=verificar?.interaccion?.reviews

                
                const desfragment ={
                    id_user,
                    name_user,
                    lastname,
                    origenEmitidoComoUser,
                    calificacion:{
                        id_provider,
                        id_review,
                        id_interaccion,
                       
                    }
                }

                console.log(desfragment)

            if(!desfragment.id_user){
                res.status(400).json("Sin comentarios")
                return
            }
            res.status(200).json(desfragment)
        }catch(error){
            console.log(error)
        }
       

    },

    ConsultaTodos:async(req:Request,res:Response):Promise<void>=>{
        try{
            let id_user= req.params.id_user
            const id_logued= Number(id_user)

            const id_provedor = await repositoryprovedor.createQueryBuilder("providers")
            .leftJoinAndSelect("providers.user","user")
            .where("user.id_user=:id_user",{id_user:id_logued})
            .getOne();

            const id_prov = id_provedor?.id_provider

            const roles =[id_prov,id_logued]
            console.log("Roles")
            console.log(roles)

            const informacion = [];

            const verificar = await AppDataSource.createQueryBuilder()
            .select("appointment")
            .from("appointment", "appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoinAndSelect("appointment.users", "users")
            .leftJoinAndSelect("appointment.interaccion", "interaccion")
            .leftJoinAndSelect("interaccion.reviews", "review")
            .where("(appointment.usersIdUser = :id_logued OR appointment.providersIdProvider = :id_prov)", {
                id_logued: id_logued,
                id_prov: id_prov,
            })
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .getMany();
            
            console.log(verificar+ "hOLA")

            console.log("Esta es la consulta")
            console.log(verificar); 


            let usuario = false;

            for(let i=0; i<verificar.length; i++){
                let valor = verificar[i].interaccion   
                let origen = verificar[i].users?.id_user
                
                if(valor){

                    if(origen===id_logued){
                        console.log("Es emitido como usuario")
                        usuario=true
                    }
                        const desfragment =verificar.map(verificar=>({
                        id_user:verificar?.users?.id_user,
                        name_user:verificar?.users?.name_user,
                        lastname:verificar?.users?.lastname,
                        origenComoUser:usuario,
                        id_appointment:verificar?.id_appointment,
                        id_prov:verificar?.providers?.id_provider,
                        calificacion:{
                        id_review:verificar?.interaccion?.reviews,
                        id_interaccion:verificar?.interaccion?.id_interaccion,
                        }
                    }))
                    informacion.push(...desfragment)
                }     
            }

            console.log("Este es el tamaño de los de los comentarios ", verificar.length)
            console.log(informacion) 

            if(informacion.length===0){
             res.status(200).json({mensages:"Sin comentarios"})
             return;   
            }
            res.status(200).json(...informacion)
            
        }catch(error){
            console.log("Hay un error interno ", error)
            res.status(500).json({message:"Error interno en el servidor"})
        }
    },

    // ConsultaTodos2: async (req: Request, res: Response): Promise<void> => {
    //     try {
    //         const id_user = req.params.id_user;
    //         const id_logued = Number(id_user);
    
    //         // Consulta para obtener el ID del proveedor relacionado
    //         const proveedor = await repositoryprovedor.createQueryBuilder("providers")
    //             .leftJoinAndSelect("providers.user", "user")
    //             .where("user.id_user = :id_user", { id_user: id_logued })
    //             .getOne();
    
    //         const id_prov = proveedor?.id_provider;
    
    //         console.log("Roles:", { id_prov, id_logued });
    
    //         const informacion: any[] = [];
    
    //         // Consulta principal: comentarios de usuarios
    //         const verificar = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.usersIdUser = :id_logued", { id_logued })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         // Consulta principal: comentarios respondidos
    //         const verificar2 = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.providersIdProvider = :id_prov", { id_prov })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         console.log("Consulta 1 (usuarios):", verificar);
    //         console.log("Consulta 2 (proveedores):", verificar2);
    
    //         if(verificar.length === 0 && verificar2.length === 0) {
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //             return;
    //         }
    //         // Procesamiento de resultados
    //         for (const appointment of verificar) {
    //             const { interaccion, users, id_appointment } = appointment;
    
    //             for (const appointment2 of verificar2) {
    //                 const { interaccion: interaccion2, id_appointment: id_appointment2 } = appointment2;
    //                     console.log("Coincidencia encontrada para appointment:", id_appointment);
    //                     console.log("Coincidencia encontrada para appointment2:", id_appointment2);
    //                 if (id_appointment === id_appointment2) {
    //                     console.log("Coincidencia encontrada para appointment:", id_appointment);
    
    //                 }
                    
    //                     // Determinar el origen
    //                     const id_user_origen = users?.id_user;
    //                     const origenComoUser = id_user_origen === id_logued;
    
    //                     // Buscar usuario relacionado al proveedor
    //                     const proveedorRelacionado = await repositoryprovedor.findOne({
    //                         where: { id_provider: appointment.providers?.id_provider },
    //                         relations: ["user"],
    //                     });
    
    //                     const datos = {
    //                         usuario: {
    //                             id_user: proveedorRelacionado?.user.id_user || id_user_origen,
    //                             name_user: proveedorRelacionado?.user.name_user || users?.name_user,
    //                             lastname: proveedorRelacionado?.user.lastname || users?.lastname,
    //                             origenComoUser,
    //                             id_appointment,
    //                         },
    //                         id_prov: appointment.providers?.id_provider,
    //                         calificacion: {
    //                             interaccion1: interaccion?.reviews,
    //                             interaccion2: interaccion2?.reviews,
    //                         },
    //                     };

    //                 console.log("Datos encontrados:", datos);
    
    //                     informacion.push(datos);
    //                     console.log("Segundo for")
    //                 }
    //             console.log("Termino el primer for")
    //         }
    
    //         console.log("Total de comentarios procesados:", informacion.length);
    
    //         // Respuesta final
    //         if (informacion.length === 0) {
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //             return;
    //         }
            
    
    //         res.status(200).json(informacion);
    //     } catch (error) {
    //         console.error("Error interno:", error);
    //         res.status(500).json({ mensaje: "Error interno en el servidor" });
    //     }
    // },

    // ConsultaTodos2: async (req: Request, res: Response): Promise<void> => {
    //     try {
    //         const id_user = req.params.id_user;
    //         const id_logued = Number(id_user);
    
    //         // Consulta para obtener el ID del proveedor relacionado
    //         const proveedor = await repositoryprovedor.createQueryBuilder("providers")
    //             .leftJoinAndSelect("providers.user", "user")
    //             .where("user.id_user = :id_user", { id_user: id_logued })
    //             .getOne();
    
    //         const id_prov = proveedor?.id_provider;
    
    //         console.log("Roles:", { id_prov, id_logued });
    
    //         const informacion: any[] = [];
    
    //         // Consulta principal: comentarios de usuarios
    //         const verificar = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.usersIdUser = :id_logued", { id_logued })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         // Consulta principal: comentarios respondidos
    //         const verificar2 = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.providersIdProvider = :id_prov", { id_prov })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         console.log("Consulta 1 (usuarios):", verificar.length);
    //         console.log("Consulta 2 (proveedores):", verificar2.length);
            
    //                 //saber cual es el arreglo mas grande
    //         const arraylentgh = verificar.length >= verificar2.length ? verificar.length : verificar2.length;
    //         console.log("Este es el arreglo mas grande",arraylentgh)
    

    //         if(verificar.length >0 && verificar2.length > 0) {
    //             //condicion para que se tomen en cuneta ambos arreglos

    //            // Procesamiento de resultados
    //             for (const appointment of verificar) {
    //                 const { interaccion, users, id_appointment } = appointment;
        
    //                 // console.log(verificar);
    //                 console.log("Contenido de verificar - id_appointment:", id_appointment);//verificar2[0].interaccion?.id_interaccion);
    //                 console.log("Criterio de búsqueda verificar2 - id_appointment:", verificar2[0].appointment?.id_appointment);
    //                 console.log(verificar2)

    //                 // Buscar coincidencias en verificar2 basado en id_interaccion
    //                 let match= verificar2.find((element) => element.interaccion?.id_interaccion === interaccion?.id_interaccion);

    //                 console.log("Coincidencia encontrada para match:", match);
        
    //                 if (match) {
    //                     const { interaccion: interaccion2 } = match;
        
    //                     // Determinar el origen
    //                     const id_user_origen = users?.id_user;
    //                     const origenComoUser = id_user_origen === id_logued;
        
    //                     // Buscar usuario relacionado al proveedor
    //                     const proveedorRelacionado = await repositoryprovedor.findOne({
    //                         where: { id_provider: appointment.providers?.id_provider },
    //                         relations: ["user"],
    //                     });
        
    //                     const datos = {
    //                         usuario: {
    //                             id_user: proveedorRelacionado?.user?.id_user || id_user_origen || null,
    //                             name_user: proveedorRelacionado?.user?.name_user || users?.name_user || "Sin nombre",
    //                             lastname: proveedorRelacionado?.user?.lastname || users?.lastname || "Sin apellido",
    //                             origenComoUser,
    //                             id_appointment,
    //                         },
    //                         id_prov: appointment.providers?.id_provider || null,
    //                         calificacion: {
    //                             interaccion1: interaccion?.reviews || null,
    //                             interaccion2: interaccion2?.reviews || null,
    //                         },
    //                     };
        
    //                     console.log("Datos procesados:", datos);
        
    //                     informacion.push(datos);
    //                 } else {
    //                     console.log(`Sin coincidencias para id_interaccion: ${interaccion?.id_interaccion}`);
    //                 }
    //             }
    //         }else{
    //             console.log("Tamaño de verificar 1:", verificar.length);
    //             console.log("Tamaño de verificar 2:", verificar2.length);
    //             console.log("termino de la condicion else") ;
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //             return; 
    //         }
    
    //         console.log("Total de comentarios procesados:", informacion.length);
    
    //         // Respuesta final
    //         if (informacion.length === 0) {
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //             console.log("Sin comentarios en el arreglo final");
    //             return;
    //         }
    
    //         res.status(200).json(informacion);
    //     } catch (error) {
    //         console.error("Error interno:", error);
    //         res.status(500).json({ mensaje: "Error interno en el servidor" });
    //     }
    // },
    
    
    // ConsultaTodos2: async (req: Request, res: Response): Promise<void> => {
    //     try {
    //         const id_user = req.params.id_user;
    //         const id_logued = Number(id_user);
    
    //         // Obtener el ID del proveedor relacionado
    //         const proveedor = await repositoryprovedor.createQueryBuilder("providers")
    //             .leftJoinAndSelect("providers.user", "user")
    //             .where("user.id_user = :id_user", { id_user: id_logued })
    //             .getOne();
    
    //         const id_prov = proveedor?.id_provider;
    
    //         console.log("Roles:", { id_prov, id_logued });
    
    //         // Consultar comentarios de usuarios
    //         const verificar = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.usersIdUser = :id_logued", { id_logued })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         // Consultar comentarios respondidos
    //         const verificar2 = await AppDataSource.createQueryBuilder()
    //             .select("appointment")
    //             .from("appointment", "appointment")
    //             .leftJoinAndSelect("appointment.providers", "providers")
    //             .leftJoinAndSelect("appointment.users", "users")
    //             .leftJoinAndSelect("appointment.interaccion", "interaccion")
    //             .leftJoinAndSelect("interaccion.reviews", "review")
    //             .where("appointment.providersIdProvider = :id_prov", { id_prov })
    //             .andWhere("interaccion.id_interaccion IS NOT NULL")
    //             .getMany();
    
    //         console.log("Consulta 1 (usuarios):", verificar.length);
    //         console.log("Consulta 2 (proveedores):", verificar2.length);
    
    //         if (verificar.length === 0 && verificar2.length === 0) {
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //             return;
    //         }
    
    //         const informacion: any[] = [];
    
    //         // Procesar coincidencias en `appointment`
    //         for (const appointment of verificar) {
    //             const { id_appointment } = appointment;
    
    //             // Buscar coincidencias en `verificar2` basado en `id_appointment`
    //             const match = verificar2.find(
    //                 (element) => element.id_appointment === id_appointment
    //             );

    //             console.log(match)
    
    //             if (match) {
    //                 const datos = {
    //                     appointmentId: id_appointment,
    //                     usuario: {
    //                         id_user: appointment.users?.id_user || null,
    //                         name_user: appointment.users?.name_user || "Sin nombre",
    //                         lastname: appointment.users?.lastname || "Sin apellido",
    //                     },
    //                     proveedor: {
    //                         id_provider: match.providers?.id_provider || null,
    //                         nombre: match.providers?.nombre || "Sin nombre",
    //                     },
    //                     comentarios: {
    //                         interaccion1: appointment.interaccion?.reviews || null,
    //                         interaccion2: match.interaccion?.reviews || null,
    //                     },
    //                 };
    
    //                 console.log("Coincidencia encontrada:", datos);
    //                 informacion.push(datos);
    //             } else {
    //                 console.log(`Sin coincidencias para id_appointment: ${id_appointment}`);
    //             }
    //         }
    
    //         console.log("Total de comentarios procesados:", informacion.length);
    
    //         if (informacion.length === 0) {
    //             res.status(200).json({ mensaje: "Sin comentarios" });
    //         } else {
    //             res.status(200).json(informacion);
    //         }
    //     } catch (error) {
    //         console.error("Error interno:", error);
    //         res.status(500).json({ mensaje: "Error interno en el servidor" });
    //     }
    // },
    ConsultaTodos2: async (req: Request, res: Response): Promise<void> => {
        try{
            const id_user = req.params.id_user;
            const id_logued = Number(id_user);
    
            // Obtener el ID del proveedor relacionado
            const proveedor = await repositoryprovedor.createQueryBuilder("providers")
                .leftJoinAndSelect("providers.user", "user")
                .where("user.id_user = :id_user", { id_user: id_logued })
                .getOne();

            if(proveedor){             
                const id_prov = proveedor?.id_provider;
                console.log("Roles:", { id_prov, id_logued });
        
                // Consultar comentarios de usuarios
                const verificar = await AppDataSource.createQueryBuilder()
                    .select("appointment")
                    .from("appointment", "appointment")
                    .leftJoinAndSelect("appointment.providers", "providers")
                    .leftJoinAndSelect("appointment.users", "users")
                    .leftJoinAndSelect("appointment.interaccion", "interaccion")
                    .leftJoinAndSelect("interaccion.reviews", "review")
                    .where("appointment.usersIdUser = :id_logued", { id_logued })
                    .andWhere("interaccion.id_interaccion IS NOT NULL")
                    .getMany();
        
                // Consultar comentarios respondidos
                const verificar2 = await AppDataSource.createQueryBuilder()
                    .select("appointment")
                    .from("appointment", "appointment")
                    .leftJoinAndSelect("appointment.providers", "providers")
                    .leftJoinAndSelect("appointment.users", "users")
                    .leftJoinAndSelect("appointment.interaccion", "interaccion")
                    .leftJoinAndSelect("interaccion.reviews", "review")
                    .where("appointment.providersIdProvider = :id_prov", { id_prov })
                    .andWhere("interaccion.id_interaccion IS NOT NULL")
                    .getMany();

                console.log("Consulta 1 (usuarios):", verificar.length);
                console.log("Consulta 2 (proveedores):", verificar2.length);

                //validar el arreglo mas grande
                const arraylentgh = verificar.length >= verificar2.length ? verificar.length : verificar2.length;

                console.log("Este es el tamaño del arreglo")
                console.log(arraylentgh)

                //validacio para saber si ambos arreglos tienen datos
                if(verificar.length >0 && verificar2.length > 0) {
                    
                    for (let i = 0; i < arraylentgh; i++) {

                        const appointment = verificar[i];
                        const appointment2 = verificar2[i];
    
                        console.log("Coincidencia encontrada para appointment:", appointment.id_appointment);
                        console.log("Coincidencia encontrada para appointment2:", appointment2.id_appointment);
                   
                        
                        const datos = {
                            appointmentId: appointment.id_appointment,
                            appointmentId2: appointment2.id_appointment,
                            usuario: {
                                id_user: appointment.users?.id_user ||appointment2.users?.id_user,
                                name_user: appointment.users?.name_user || appointment2.users?.name_user,
                                lastname: appointment.users?.lastname ||appointment2.users?.lastname,
                            },
                            proveedor: {
                                id_provider: appointment.providers?.id_provider || appointment2.providers?.id_provider,
                                nombre: appointment.providers?.nombre || appointment2.providers?.nombre,
                            },
                            comentarios: {
                                interaccion1: appointment.interaccion?.reviews || null,
                                interaccion2: appointment2.interaccion?.reviews || null,
                            },
                        };
                        res.status(200).json({ response:datos});
    
                        if (appointment.id_appointment === appointment2.id_appointment) {
                            console.log("Coincidencia encontrada para appointment:", appointment.id_appointment);
                        }
                    }
                }else{
                    res.json({message:"No hay datos en algun de los arreglos"})

                    //hacer las consultas por separado
                }

            }else{
                res.json({message:"No se encontro el proveedor"})

                //hacer las consultas por id_usuario
            }
        
    
            // if (verificar.length === 0 && verificar2.length === 0) {
            //     res.status(200).json({ mensaje: "Sin comentarios" });
            //     return;
            // }
    
            // const informacion: any[] = [];
    
            // // Procesar coincidencias en `appointment`
            // for (const appointment of verificar) {
            //     const { id_appointment } = appointment;
    
            //     // Buscar coincidencias en `verificar2` basado en `id_appointment`
             
            // }
        }catch(error){
            console.log(error)
            res.status(500).json({message:"Error interno en el servidor"})
        }
    },

    edit:async(req:Request,res:Response):Promise<void>=>{
        try {
            const { id_user, id_prov } = req.params;
            // const commentario = req.body.commentario;
            // const calificacion= req.body.calificacion
            // const id_review = req.body.id_review

            let {commentario,calificacion ,id_review}= req.body
            id_review = Number(id_review)
            console.log(commentario);
    
            if (!commentario) {
                res.status(400).json({ message: "El comentario no puede estar vacío" });
                return;
            }
    
            const comment = await AppDataSource.createQueryBuilder()
            .select("appointment")
            .from("appointment", "appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoinAndSelect("appointment.users", "users")
            .leftJoinAndSelect("appointment.interaccion", "interaccion")
            .leftJoinAndSelect("interaccion.reviews", "review")
            .where("(appointment.usersIdUser = :id_logued OR appointment.providersIdProvider = :id_prov) or (appointment.usersIdUser = :id_prov OR appointment.providersIdProvider = :id_logued)", {
                id_logued: id_user,
                id_prov: id_prov,
            })
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .getOne();
    
            console.log("Aqui comienza")

            console.log("----------------------------")
            // let id_review =comment?.interaccion?.reviews[0].id_review
            // console.log(id_review)
            
            console.log("----------------------------")
            console.log(comment);

    
            if (!id_review) {
                res.status(404).json({ message: "No se encontró la review asociada" });
                return;
            }
            let rev = await repositoryreview.findOne({ where: { id_review: id_review } });
    
            if (rev) {
                rev.comment = commentario;
                if(calificacion){
                    console.log("Se actualizo la calificacion")
                    rev.calificacion=calificacion
                }
                await repositoryreview.save(rev); // Guarda el cambio en la base de datos
                res.status(200).json({ message: "Se actualizó el comentario" });
            } else {
                res.status(404).json({ message: "No se encontró el comentario" });
            }
        } catch (error) {
            console.log(error);
            res.status(500).json({ message: "Error interno en el servidor" });
        }
    },
    eliminar: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_interaccion, id_review } = req.params

            const result = await AppDataSource.createQueryBuilder()
                .delete()
                .from("review_interacciones_interaccion")
                .where("reviewIdReview = :id_review", { id_review })
                .andWhere("interaccionIdInteraccion = :id_interaccion", { id_interaccion })
                .execute();

            console.log("Se ellimino con exito los datos de review con el id", id_review);
            console.log("Se elimino con exito los datos de interaccion con el id", id_interaccion);


            await AppDataSource.createQueryBuilder()
                .delete()
                .from("review")
                .where("review.id_review=:id_review", { id_review: id_review })
                .execute();

            if (result.affected === 0) {
                res.status(404).json({ message: "No se encontró la relación entre la review y la interaccion." });
                return
            }
            res.status(200).json({ message: "Relación eliminada correctamente." });
        } catch (error) {
            console.log(error)
            res.status(500).json("Error interno")
        }
    },

    enable_interaction: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_provider, id_user } = req.params

            const verificar = await reppsitoryappointment
                .createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "providers")
                .leftJoin("appointment.users", "users")
                .where(
                    "(appointment.users = :id_logued OR appointment.providers = :id_prov) OR (appointment.users = :id_prov OR appointment.providers = :id_logued)",
                    { id_logued: id_user, id_prov: id_provider }
                )
                .getOne();

            if (verificar) {
                res.status(200).json({ enableToComment: true })
            } else {
                res.status(400).json({ enableToComment: false })
            }

        } catch (error) {
            console.log(error)
            res.status(500).json({ message: "Error interno" })
        }
    }


}

export default controllersReview;

