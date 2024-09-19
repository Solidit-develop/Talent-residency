import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { Messages } from "../entitis/messages";
import { Conversation } from "../entitis/conversation";

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
                    conversation.creationDate = new Date(createdate); // Usar la fecha proporcionada en `createdate`
    
                    await repositorycoversation.save(conversation);
                    console.log("Nueva conversación creada:", conversation);
    
                    // Crear el mensaje asociado a la nueva conversación
                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = new Date(sendDate); // Convertir `sendDate` a Date
                    mensaje.conversation = conversation;
    
                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);
    
                } else {
                    // Si ya existe la conversación pero tiene `creationDate`, crear un nuevo mensaje
                    console.log("Existe la conversación con creationDate, agregando mensaje...");
    
                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = new Date(sendDate); // Convertir `sendDate` a Date
                    mensaje.conversation = senddate;
    
                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);
                }
    
            } else {
                // Si ya existe la conversación, agregar el mensaje a la conversación existente
                console.log("Existe la conversación:", existe);
    
                let mensaje = new Messages();
                mensaje.contect = contect;
                mensaje.senddate = new Date(sendDate); // Convertir `sendDate` a Date
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
       
    }



}

export default controllermessages;
