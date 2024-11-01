import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { QueryBuilder, Timestamp } from "typeorm";
import { interaccion } from "../entitis/interaccion";
import { Providers } from "../entitis/provedores";
import { review } from "../entitis/review";
import { appointment } from "../entitis/appointment";
// import { images } from "../entitis/images";
// import { imagesRelation } from "../entitis/imagesRelation";

const repositoryuser= AppDataSource.getRepository(users);
const repositoryinteraccion = AppDataSource.getRepository(interaccion);
const repositoryprovedor = AppDataSource.getRepository(Providers);
const repositoryreview = AppDataSource.getRepository(review);
const reppsitoryappointment = AppDataSource.getRepository(appointment);
// const repositoryMessages = AppDataSource.getRepository(images);
// const repositoryimagenRelacion  = AppDataSource.getRepository(imagesRelation);

const controllersReview={

    ping: async(req:Request, res:Response): Promise<void> => {
        res.send("pong");
    },


    registro:async(req:Request, res:Response):Promise<void>=>{
        try{

            const {id_logued, id_dest}=req.params

            const {origenComoUser, calificacion, commentario,id_imageRelation}= req.body
            // const id =  Number(id_appointment)

                const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers","providers")
                .leftJoin("appointment.users","users")
                .leftJoin("appointment.interaccion","interaccion")
                .leftJoinAndSelect("interaccion.review","review")
                .where("appointment.users=:id_logued",{id_logued:id_logued})
                .andWhere("appointment.providers=:providers",{providers:id_dest})
                .getOne();

                const id_app = verificar?.id_appointment;

                if(!verificar){
                    console.log("Esta es la calificacion que se le dio a este usuario")
                    console.log(verificar);
                    console.log("Se debuelve el nombre junto con la calificacion que se le dio ")
                    res.status(200).json(verificar)
                    return;

                }else{
                    //guardamos el comentario

                    // Metodo para lamacenar las imagenes

                    const acuerdo = await reppsitoryappointment.findOne({where:{id_appointment:id_app}})

                    if(acuerdo){
                        const comentario = new review();
                        comentario.comment=commentario;
                        comentario.calificacion=calificacion;
                        comentario.image=id_imageRelation;
                        await repositoryreview.save(comentario)
    
                        const interaction= new interaccion();
                        interaction.origenEmitidoComoUser=origenComoUser;
                        interaction.review=comentario;
                        interaction.appointment=acuerdo;
                        await repositoryinteraccion.save(interaction)
                        
                    }else{
                        res.status(400).json({message:"No se encontro el acuerdo"})
                    }

                    console.log("Se agrega la calificacion")
                }

            console.log(verificar)
            
            res.status(200).json({message:"Registro con exito"})

        }catch(error){
            res.json(error)
            console.log(error)
        }
    },
    consulta:async(req:Request,res:Response):Promise<void>=>{
        try{
            const {id_logued, id_dest}=req.params
            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers","providers")
            .leftJoin("appointment.users","users")
            .leftJoinAndSelect("appointment.interaccion","interaccion")
            .leftJoinAndSelect("interaccion.review","review")
            .where("appointment.users=:id_logued",{id_logued:id_logued})
            .andWhere("appointment.providers=:providers",{providers:id_dest})
            .getOne();

            res.status(200).json({verificar})
        }catch(error){
            console.log(error)
        }
       

    }


}

export default controllersReview;

