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
        const {id_logued,id_dest}= req.params
        const date = "La hora actual" //hora en cual fue iniciada la converzacion
        const {contect, dendDate}= req.body //sendDare fecha de los mensajes
        
        //logica para verificar si existe una relacion entre ambos usuarios
        // const conversacion = await repositoryuser
        // .createQueryBuilder("users")
        // // .leftJoinAndSelect()
        //logica para estar insertando
        

    }



}

export default controllermessages;