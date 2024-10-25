import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { Messages } from "../entitis/messages";
import { Conversation } from "../entitis/conversation";
import { QueryBuilder, Timestamp } from "typeorm";

const repositoryuser= AppDataSource.getRepository(users);
const repsitorymesages = AppDataSource.getRepository(Messages);
const repositorycoversation = AppDataSource.getRepository(Conversation)



const controllermessages={

    ping: async(req:Request, res:Response): Promise<void> => {
        res.send("pong");
    },

    usuarios:async (req:Request, res:Response):Promise<void>=>{
       try{

        const usuarios = await repositoryuser.find();
        console.log(usuarios)
        res.status(200).json({usuarios})

       }catch(error){
        console.log(error)
        res.status(200).json({message:"Hay un error dentro del servidor"})
       }
        
    },

    // inivcio de conversacion

    conversacion: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_logued, id_dest } = req.params;
            const { contect, sendDate, createdate } = req.body; // `sendDate` es la fecha de los mensajes
            const user_log = parseInt(id_logued);
            const user_des = parseInt(id_dest);
    
            // Buscar si ya existe la conversación entre los usuarios
            const existe = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                .leftJoinAndSelect("conversation.messages", "messages")
                .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued: user_log, id_destino: user_des })
                // .orWhere("(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)", { id_logued: user_log, id_destino: user_des })
                .getOne();
    
            // Buscar usuario logueado
            const usuario = await repositoryuser.findOne({ where: { id_user: user_log } });
    
            if (!usuario) {
                res.status(404).json({ message: "No se encontró el usuario" });
                return;
            }
    
            // Si no existe la conversación, crear una nueva
            if (!existe) {
                console.log("No existe la conversación, verificando senddate...");
    
                // Revisar si ya existe una conversación con `creationDate`
                const senddate = await repositorycoversation.createQueryBuilder("conversation")
                    .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                    .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued: user_log, id_destino: user_des })
                    .getOne();
    
                const creacion = senddate?.creationDate;
    
                // Si no existe `creationDate`, crear una nueva conversación
                if (!senddate?.creationDate) {
                    let conversation = new Conversation();
                    conversation.id_userOrigen = usuario;
                    conversation.id_userDestino = user_des;
                    conversation.creationDate = createdate; // Usar la fecha proporcionada en `createdate`
    
                    await repositorycoversation.save(conversation);
                    console.log("Nueva conversación creada:", conversation);
    
                    // Crear el mensaje asociado a la nueva conversación
                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                    mensaje.conversation = conversation;
    
                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);
    
                } else {
                    // Si ya existe la conversación pero tiene `creationDate`, crear un nuevo mensaje
                    console.log("Existe la conversación con creationDate, agregando mensaje...");
    
                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                    mensaje.conversation = senddate;
    
                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);
                }
    
                
            } else {
                // Si ya existe la conversación, agregar el mensaje a la conversación existente
                console.log("Existe la conversación:", existe);
    
                let mensaje = new Messages();
                mensaje.contect = contect;
                mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                mensaje.conversation = existe;
    
                await repsitorymesages.save(mensaje);
                console.log("Mensaje registrado con éxito:", mensaje);
            }
    
            res.status(200).json({ message: "Mensaje guardado con éxito" });
        } catch (error) {
            console.error("Error en el servidor:", error);
            res.status(500).json({ message: "Hay un error interno en el servidor" });
        }
    },

    recupMessages:async(req:Request, res:Response):Promise<void>=>{
        
        try{
            let {id_logued,id_dest}= req.params

             
            const existe = await repositorycoversation.createQueryBuilder("conversation")
            .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
            .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
            .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_dest })  // Condición 1
            .orWhere("(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)", { id_logued, id_destino: id_dest })  // Condición 2
            .getMany();
         
            console.log("Mensaje que se nos regres ")
            // console.log("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_dest })

            const resultados = existe.map(Conversation=>{
                return{
                    id_conversation:Conversation.id_conversation,
                    interactuan:{
                        nombre:Conversation.id_userOrigen.name_User,
                        id_dest:Conversation.id_userDestino
                    },
                    message:Conversation.messages              
                }
            })

            res.status(200).json({resultados})

        }
        catch(error){
            console.log(error)
            res.status(500).json({message:"Hay un error dentro del servidor"})
        }
       
    },

    // reviewMesage:async(req:Request, res:Response):Promise<void>=>{
        
    //     try{
    //         let {id_logued}= req.params

    //         const relacion = await AppDataSource
    //         .createQueryBuilder()
    //         .select('conversation.id_conversation')  // Seleccionamos 'id_conversation'
    //         .from('conversation', 'conversation')  
    //         .where("(conversation.idUserOrigenIdUser = :id_logued OR conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_logued })
    //         .getMany();




    //         const ids = relacion.map(conversation => conversation.id_conversation)

    //         let mensagesss=[];
    //         const existe = await repositorycoversation.createQueryBuilder("conversation")
    //         .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
    //         .leftJoinAndSelect("conversation.messages", "messages")
    //         .where("(conversation.id_userOrigen = :id_logued OR conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_logued })
    //         .orderBy("messages.sendDate", "ASC")
    //         .limit(1)
    //         .getMany();

    //         let devuelto;
    //         // console.log("Tamaño del arreglo")

    //         // console.log(
    //         //     ids.length
    //         // )

    //         // const consultas = ids.map(async (id) => {
    //         //     return await AppDataSource
    //         //         .createQueryBuilder()
    //         //         .select([
    //         //             "messages.contect",           // Seleccionamos el contenido del mensaje
    //         //             "messages.sendDate",           // Seleccionamos la fecha de envío
    //         //             "user.name",                   // Seleccionamos el nombre del usuario desde la tabla 'users'
    //         //         ])
    //         //         .from("messages", "messages")
    //         //         .leftJoin("users", "user", "user.id = messages.userId")  // Hacemos un JOIN con la tabla 'users'
    //         //         .where("messages.conversationIdConversation = :conversation", { conversation: id })
    //         //         .orderBy("messages.sendDate", "ASC")
    //         //         .limit(1)
    //         //         .getOne();
    //         // });

    //         for (let i = 0; i < ids.length; i++) {
    //             const conversationId = Number(ids[i]);
    //             const devuelto = await AppDataSource
    //                 .createQueryBuilder("conversation")
    //                 .select([
    //                     "messages.contect",
    //                     "messages.sendDate",
    //                     "conversation.idUserOrigenIdUser"
    //                 ])
    //                 .from("conversation", "conversation")
    //                 .leftJoin("messages", "messages", "messages.conversationIdConversation = conversation.id_conversation")  // JOIN correcto
    //                 .where("messages.conversationIdConversation = :conversation", { conversation: ids[i] })  // Alias corregido
    //                 .orderBy("messages.sendDate", "ASC")
    //                 .limit(10)
    //                 .getMany();
            
    //             mensagesss.push(devuelto);
            


    //         // .createQueryBuilder()
    //         // .select("messages.contect")    
    //         // .from("messages","messages")
    //         // .where("(messages.conversationIdConversation=:conversasion)",{conversasion:ids[i]})
    //         // .orderBy("messages.sendDate", "ASC")
    //         // .limit(1)
    //         // .getMany();
    //         // mensagesss.push(devuelto)
    //         }
    //         // devuelto = await  AppDataSource
    //         // .createQueryBuilder()
    //         // .select("messages.contect")    
    //         // .from("messages","messages")
    //         // .where("(messages.conversationIdConversation=:conversasion)",{conversasion:1})
    //         // .orderBy("messages.sendDate", "ASC")
    //         // .limit(1)
    //         // .getMany();

    //         // const resultados = existe.map(Conversation=>{
    //         //     return{
    //         //         id_conversation:Conversation.id_conversation,
    //         //         interactuan:{   
    //         //             nombre:Conversation.id_userOrigen.name_User,
    //         //             id_dest:Conversation.id_userDestino
    //         //         },
    //         //         mensagesss:{
    //         //             message:Conversation.messages
    //         //         },
                                  
    //         //     }
    //         // })
    //         console.log("Relacion entre los grupos")
    //         console.log(ids)

    //         console.log("Mensajes recuperados")

    //         console.log("mensajes")
    //         console.log(mensagesss)
            
    //         res.status(200).json({mensagesss})
    //     }
    //     catch(error){
    //         console.log(error)
    //         res.status(500).json({message:"Hay un error dentro del servidor"})
    //     }
       
    // },
    reviewMesage: async (req: Request, res: Response): Promise<void> => {
        try {
            let { id_logued } = req.params;
    
            // Obtenemos las conversaciones relacionadas
            const relacion = await AppDataSource
                .createQueryBuilder()
                .select('conversation.id_conversation')  // Seleccionamos 'id_conversation'
                .from('conversation', 'conversation')  
                .where("(conversation.idUserOrigenIdUser = :id_logued OR conversation.id_userDestino = :id_destino)", 
                { id_logued, id_destino: id_logued })
                .getMany();


            
    
            const ids = relacion.map(conversation => conversation.id_conversation);

            // const dev = await repositorycoversation
            // .createQueryBuilder("conversation")
            // .leftJoinAndSelect("conversation.messages", "messages")  // Se obtiene la relación con los mensajes
            // .leftJoin("users", "users", "users.id_user = conversation.idUserOrigenIdUser")  // JOIN correcto con la tabla de usuarios
            // .select([
            //     "messages.contect",  // Campo 'contect' de la tabla 'messages'
            //     "conversation.id_conversation",  // Campo 'id_conversation' de la tabla 'conversation'
            //     "users.name_User", //falta---------------
            //     "conversation.id_userDestino",  // Campo 'id_userDestino' de la tabla 'conversation'
            //     "messages.senddate"  // Campo 'sendDate' de la tabla 'messages'
            // ])
            // .where("messages.conversationIdConversation = :conversationId", { conversationId:2 })  // Usar el parámetro pasado
            // .orderBy("messages.id_messages", "DESC")  // Ordenar por el campo 'id_messages' en orden descendente
            // .limit(1)  // Limitar a 1 resultado
            // .getOne();  // Obtener un solo resultado

            // const dev = await repositorycoversation.createQueryBuilder("conversation")
            // .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
            // .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
            // .where("(conversation.id_userOrigen = :id_logued OR conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_logued })  // Condición 1
            // .orWhere("(conversation.id_userOrigen = :id_destino OR conversation.id_userDestino = :id_logued)", { id_logued, id_logued: id_logued })  // Condición
            // .orderBy("messages.id_messages", "DESC")  // Ordenar por la fecha de envío
            // .limit(1)
            // .getMany(); // Obtener resultados

            // const resultados = dev.map(Conversation=>{
            //     return{
            //         id_conversation:Conversation.id_conversation,
            //         interactuan:{
            //             nombre:Conversation.id_userOrigen.name_User,
            //             id_dest:Conversation.id_userDestino
            //         },
            //         message:Conversation.messages              
            //     }
            // })

            // console.log("Hello")
            // // console.log(dev)
            // console.log("Mapeado")
            // console.log(resultados)
    
            let mensagesss = [];
    
            // Recorremos cada conversación


            for (let i = 0; i < ids.length; i++) {
                const conversationId = ids[i];


                const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
                .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
                .where("messages.conversationIdConversation = :conversationId", { conversationId:conversationId })
                // .orderBy("messages.id_messages", "DESC")  // Ordenar por la fecha de envío
                // .limit(1)
                .getMany(); // Obtener resultados

                   const resultados = devuelto.map(Conversation=>{
                return{
                    id_conversation:Conversation.id_conversation,
                    interactuan:{
                        nombre:Conversation.id_userOrigen.name_User,
                        id_dest:Conversation.id_userDestino
                    },
                    message:Conversation.messages              
                }
            })
    
                // const devuelto = await repositorycoversation
                // .createQueryBuilder("conversation") 
                // .leftJoinAndSelect("conversation.messages", "messages")
                // .select([
                //     "messages.contect",
                //     "conversation.id_conversation",
                //     "conversation.idUserOrigenIdUser",
                //     "conversation.id_userDestino",
                //     "messages.senddate"
                // ])
                // .where("messages.conversationIdConversation = :conversationId", { conversationId:conversationId })
                // .orderBy("messages.id_messages", "DESC")  // Ordenar por la fecha de envío
                // .limit(1)
                // .getOne(); // Obtener resultados
                mensagesss.push(resultados);  // Añadimos los mensajes a la lista
            }
    
            // console.log("Relacion entre los grupos");
            // console.log(ids);
    
            // console.log("Mensajes recuperados");
            // console.log("mensajes");
            // console.log(mensagesss);
    
            // Respondemos con los mensajes una vez completado el bucle
            // res.status(200).json({ mensagesss });











            //metodo de ayer


            // const interaccion1 = await AppDataSource
            // .createQueryBuilder()
            // .select('conversation.id_conversation')  // Seleccionamos 'id_conversation'
            // .from('conversation', 'conversation')  
            // .where("(conversation.idUserOrigenIdUser = :id_logued)", 
            // { id_logued: id_logued })
            // .getMany();

            // console.log("Id de las interacciones")
            // console.log(interaccion1)

            // const ides = interaccion1.map(conversation => conversation.id_conversation);
            // console.log("Mapeo de los id.................................")
            // console.log(ides);

            // for(let j=0; j<ides.length ; j++){
            //     console.log("helloooooo")
            //     //consultar los mensajes de interaccion
            //     console.log(ides[j])
            //     const relacion = await AppDataSource
            //     .createQueryBuilder()
            //     .select('conversation.id_conversation')  // Seleccionamos 'id_conversation'
            //     .from('conversation', 'conversation')  
            //     .where("(conversation.idUserOrigenIdUser = :id_origen)",
            //     { id_origen: ides[j] })
            //     .andWhere("conversation.id_userDestino = :id_destino",
            //     {id_destino:id_logued})
            //     .getMany();
            //     console.log("Destino")
            //     console.log(relacion);

            //     //consultar los mensajes de relacion
            //     //un mapeo
            //     //{ambos mensajes junto con los nombres}
            //     //almaceno en un arreglo el mapeo
            //     //regreso 2 ultimos mensajes
            //     // es comparar fechas el mas reciente
            //     if(relacion.length===0){
            //         console.log("Busca solo los mensajes de la conversacion",ides[j])


            //         // Maper los mensajes con los nombres y almacenarlos en un arreglo
            //     }else{
            //     console.log("Consultar mensajes de las conversaciones ",relacion,ides[j])
            //     }
            // }


            //Mi meotod

                       // Mi metodo

                       const interaccion1 = await AppDataSource
                       .createQueryBuilder()
                       .select('conversation.id_userDestino')  // Seleccionamos 'id_conversation'
                       .from('conversation', 'conversation')  
                       .where("(conversation.idUserOrigenIdUser = :id_logued)", 
                       { id_logued: id_logued })
                       .getMany();
           
                       console.log(interaccion1);
                       console.log("Id de las interacciones")
                       const ides = interaccion1.map(conversation => conversation.id_userDestino);
           
           
                       console.log("Todos los usuarios destinos.................................")
                       console.log(ides);
           
                       //hacer una consulta de los ids de aquellas interacciones donde estos 2 coinciden

                       console.log("El id del destino")
                       console.log(ides[0])
                           
                       const conversacion = await AppDataSource
                       .createQueryBuilder()
                       .select('conversation.id_conversation')  // Seleccionamos 'id_conversation'
                       .from('conversation', 'conversation')  
                       .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_logued })  // Condición 1
                       .orWhere("(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)", { id_logued, id_destino: ides[0] })  // Condición
                       .getMany();
                       
                       console.log("Conversacion sacada")
                       console.log(conversacion)

                       const ides_conversaciones = conversacion.map(conversation => conversation.id_conversation);

                       console.log("ID de las conversaciones")
                       console.log(ides_conversaciones[0],ides_conversaciones[1])

                       const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                       .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
                       .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
                       .where("messages.conversationIdConversation = :conversationId", { conversationId:ides_conversaciones[0] })
                       .orWhere("messages.conversationIdConversation = :conversationId", { conversationId:ides_conversaciones[1] })
                       .orderBy("messages.id_messages", "DESC")  // Ordenar por la fecha de envío
                       .limit(2)
                       .getMany(); // Obtener resultados
       
                          const resultados = devuelto.map(Conversation=>{
                       return{
                           id_conversation:Conversation.id_conversation,
                           interactuan:{
                               nombre:Conversation.id_userOrigen.name_User,
                               id_dest:Conversation.id_userDestino
                           },
                           message:Conversation.messages              
                       }
                   })

                   console.log("mensajes encontrados")
                   console.log(resultados)
                   res.status(200).json({resultados})
           
                       


    
        } catch (error) {
            console.log(error);
            res.status(500).json({ message: "Hay un error dentro del servidor" });
        }
    },

    metodo: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_logued } = req.params;
            const mensajes = [];
    
            // Obtener los ID de destino de las conversaciones del usuario logueado

            const prueba = await AppDataSource
            .createQueryBuilder()
            .select("conversation.id_userDestino")
            .from("conversation", "conversation")
            .where("conversation.idUserOrigenIdUser = :id_logued", { id_logued })
            .getMany();

            





            let interaccion1 = await AppDataSource
                .createQueryBuilder()
                .select("conversation.id_userDestino")
                .from("conversation", "conversation")
                .where("conversation.idUserOrigenIdUser = :id_logued", { id_logued })
                .getMany();
    
            console.log("Interacción 1:", interaccion1);
      
                console .log("No se encontro relacion")


               let  interaccion2 = await AppDataSource
                .createQueryBuilder()
                // .select("conversation.id_userOrigen")
                .from("conversation", "conversation")
                .where("conversation.id_userDestino = :id_destino", { id_destino:id_logued })
                .getMany();
console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")
                console.log("Interacciones que existen cuando pasa en el rol logueado")
                console.log(interaccion1)
                console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")            
                console.log("Interaccion donde es el destino")
                console.log(interaccion2)
console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")




            
            const ides = interaccion1.map(conversation => conversation.id_userDestino);
    
            console.log("Todos los usuarios destinos:", ides);
    
            for (let i = 0; i < ides.length; i++) {
                console.log("Procesando id_userDestino:", ides[i]);
    
                // Obtener las conversaciones entre el usuario logueado y el id_userDestino actual
                let conversacion = await AppDataSource
                    .createQueryBuilder()
                    .select("conversation.id_conversation")
                    .from("conversation", "conversation")
                    .where(
                        "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                        "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                        { id_logued, id_destino: ides[i] }
                    )
                    .getMany();

                    if(conversacion.length===0){
                        conversacion = await AppDataSource
                        .createQueryBuilder()
                        .select("conversation.id_conversation")
                        .from("conversation", "conversation")
                        .where(
                            "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                            "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                            { id_logued, id_destino: ides[i] }
                        )
                        .getMany();
                    }
    
                console.log("Conversaciones encontradas:", conversacion);
    
                const ides_conversaciones = conversacion.map(conv => conv.id_conversation);
    
                if (ides_conversaciones.length > 0) {
                    // Consultar los mensajes más recientes de las conversaciones encontradas
                    const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                        .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  
                        .leftJoinAndSelect("conversation.messages", "messages")
                        .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                        .orderBy("messages.id_messages", "DESC")  
                        .limit(1)
                        .getMany();
    
                    const resultados = devuelto.map(conversation => ({
                        interactuan: {
                            nombre: conversation.id_userOrigen.name_User,
                            id_dest: conversation.id_userDestino
                        },
                        message: conversation.messages
                    }));
    
                    mensajes.push(...resultados);
                }
            }
    
            console.log("Mensajes encontrados:", mensajes);
            res.status(200).json({ mensajes });
        } catch (error) {
            console.error("Error en el servidor:", error);
            res.status(500).json({ message: "Hay un error dentro del servidor" });
        }
    }
    

}

export default controllermessages;
