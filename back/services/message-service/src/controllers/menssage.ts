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

    conversacion:async(req:Request, res:Response):Promise<void>=>{
        try{
            let {id_logued,id_dest}= req.params
            // const date = "La hora actual" //hora en cual fue iniciada la converzacion
            const {contect, sendDate,date}= req.body //sendDare fecha de los mensajes
            const user_log = parseInt(id_logued)
            const user_des= parseInt(id_dest);
            
           
    
            const existe = await repositorycoversation.createQueryBuilder("conversation")
            .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
            .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
            .where("conversation.id_userOrigen = :id_userOrigen", { id_userOrigen: id_logued })  // Filtrar por usuario origen
            .andWhere("conversation.id_userDestino = :id_userDestino", { id_userDestino: id_dest }) 
            .orWhere("conversation.id_userOrigen = :id_userOrigen", { id_userOrigen: id_logued })
            .andWhere("conversation.id_userOrigen = :id_userOrigen", { id_userOrigen: id_logued }) // Filtrar por usuario destino
            .getOne();

            const usuario = await repositoryuser.findOne({where:{id_user:user_log}})

           

            if(!usuario){
                res.status(404).json({message:"No se encontro el usuario"})
                return;
            }

            // const fecha = await repositorycoversation.findOneBy({
            //     where :{id_userOrigen:id_logued},
            //     relations:["id_userOrigen"]
            // })

            if (!existe){

                console.log("No existe creando la relacion")
                let conversation = new Conversation();
                conversation.id_userOrigen=usuario;
                conversation.id_userDestino= user_des;

                conversation.creationDate=date;
                
                console.log(conversation)

                // await repositorycoversation.save(conversation)
         

                console.log(existe)

            
        

                



                

                
                let mensaje = new Messages();
                mensaje.contect= contect;
                mensaje.senddate= sendDate
                mensaje.conversation=conversation;
                //  await repsitorymesages.save(mensaje)
                console.log("Se regisdtro con exito el mensaje")
                console.log(mensaje)

            }else{
                let mensaje = new Messages();
                mensaje.contect= contect;
                mensaje.senddate= sendDate
                // mensaje.conversation=existe;
                //  await repsitorymesages.save(mensaje)
                console.log("Mensaje")
                console.log(mensaje);
                console.log("Se regisdtro con exito el mensaje")
            }

            
            // res.status(200).json({message:"mensaje guardado con exito"})
            res.status(200).json({existe})
        }catch(error){

            console.log("Hay un error en el servidor ")
            console.log(error)
            res.status(500).json({Message:"Hay un error interno en el servidor"})

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
            console.log(existe);

            
            // const resultadoenJson = JSON.stringify(existe)
            // console.log(resultadoenJson)

            console.log(existe)

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
