import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { QueryBuilder, Repository, Timestamp, View } from "typeorm";
import { interaccion } from "../entitis/interaccion";
import { Providers } from "../entitis/provedores";
import { review } from "../entitis/review";
import { appointment } from "../entitis/appointment";

import {ImagenService} from "../services/imagenService"
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

    pruebaConexion:async(req:Request, res:Response): Promise<void>=>{
        const conexion = new ImagenService();
        
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
                
                console.log("Este es el registro----------------------------")
                console.log(verificar)

                const id_app = verificar?.id_appointment;

                console.log("Este es el id de appointment-----------------------------------")
                console.log(id_app)

                if(verificar){
                    // console.log("Esta es la calificacion que se le dio a este usuario")
                    // console.log(verificar);
                    console.log("Se debuelve el nombre junto con la calificacion que se le dio ")
                    res.status(200).json(verificar)
                    return;

                }else{
                    console.log("Entro en el registro")
                    
                    const acuerdo = await reppsitoryappointment.findOne({where:{id_appointment:id_app}})
                    console.log("Este es el acuerdo", acuerdo)

                    if(acuerdo && id_app){
                 
                        console.log("Entro en la funcion para poder registrar-------------------")
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

                        console.log("No se encontro el appointmet con la relacion de los ids")
                        console.log("Id del appoinment es ", id_app, "Por que no se encontro")
                        return
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
    consultaUno:async(req:Request,res:Response):Promise<void>=>{
        try{
            const {id_logued, id_dest}=req.params

            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers","providers")
            .leftJoinAndSelect("appointment.users","users")
            .leftJoinAndSelect("appointment.interaccion","interaccion")
            .leftJoinAndSelect("interaccion.review","review")
            .where("appointment.users=:id_logued",{id_logued:id_logued})
            .andWhere("appointment.providers=:providers",{providers:id_dest})
            .getMany();

            const defragment =verificar.map(verificar=>({
                Nombre:verificar.users.name_User,
                apellido:verificar.users.lasname,
                origenComoUser:verificar.interaccion.origenEmitidoComoUser,
                calificacion:{
                id_revire:verificar.interaccion.review.id_review,
                id_interaccion:verificar.interaccion.id_interaccion,
                calificacion:verificar.interaccion.review.calificacion,
                comentario:verificar.interaccion.review.comment,
                imagen:verificar.interaccion.review.image
                }
            }))

            
            console.log(defragment)

            res.status(200).json({defragment})
        }catch(error){
            console.log(error)
        }
       

    },

    ConsultaTodos:async(req:Request,res:Response):Promise<void>=>{
        try{
            const id_logued= req.params.id_logued

            const id_provedor = await repositoryprovedor.createQueryBuilder("providers")
            .leftJoinAndSelect("providers.user","user")
            .where("user.id_user=:id_user",{id_user:id_logued})
            .getOne();
            const id_prov = id_provedor?.id_provider
            const informacion = [];
            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers","providers")
            .leftJoinAndSelect("appointment.users","users")
            .leftJoinAndSelect("appointment.interaccion","interaccion")
            .leftJoinAndSelect("interaccion.review","review")
            .where("appointment.users=:id_logued",{id_logued:id_logued})
            .orWhere("appointment.providers=:providers",{providers:id_prov})
            .getMany();

            for(let i=0; i<verificar.length; i++){
                console.log("Estro a la funcion")

                const desfragment =verificar.map(verificar=>({

                    Nombre:verificar.users.name_User,
                    apellido:verificar.users.lasname,
                    origenComoUser:verificar.interaccion.origenEmitidoComoUser,
                    calificacion:{
                    id_review:verificar.interaccion.review.id_review,
                    id_interaccion:verificar.interaccion.id_interaccion,
                    calificacion:verificar.interaccion.review.calificacion,
                    comentario:verificar.interaccion.review.comment,
                    imagen:verificar.interaccion.review.image
                    }
                }))

                informacion.push(desfragment)
            }

            // console.log("Este es el tamaño de los de los comentarios ", verificar.length)
            res.status(200).json(informacion)
            
        }catch(error){
            console.log("Hay un error interno ", error)
            res.status(500).json({message:"Error interno en el servidor"})
        }
    },

    edit:async(req:Request,res:Response):Promise<void>=>{
        try {
            const { id_logued, id_dest } = req.params;
            const commentario = req.body.commentario;
            const calificacion= req.body.calificacion

            console.log(commentario);
    
            if (!commentario) {
                res.status(400).json({ message: "El comentario no puede estar vacío" });
                return;
            }
    
            let comment = await reppsitoryappointment.createQueryBuilder("appointment")
                .leftJoinAndSelect("appointment.providers", "providers")
                .leftJoinAndSelect("appointment.users", "users")
                .leftJoinAndSelect("appointment.interaccion", "interaccion")
                .leftJoinAndSelect("interaccion.review", "review")
                .where("appointment.users = :id_logued", { id_logued })
                .andWhere("appointment.providers = :providers", { providers: id_dest })
                .getOne();
    
            let id_review = comment?.interaccion?.review?.id_review;
    
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

    eliminar:async(req:Request,res:Response):Promise<void>=>{

        try{

            const {id_interaccion, id_review}=req.params

            await AppDataSource.createQueryBuilder()
            .delete()
            .from("interaccion")
            .where("interaccion.id_interaccion=:id_interaccion",{id_interaccion:id_interaccion})
            .execute()

            console.log("Se ellimino con exito los datos de review con el id",id_review);
            console.log("Se elimino con exito los datos de interaccion con el id", id_interaccion);


            await AppDataSource.createQueryBuilder()
            .delete()
            .from("review")
            .where("review.id_review=:id_review",{id_review:id_review})
            .execute();

            res.status(200).json({message:"Eliminado con exito"})
           

        }catch(error){
            console.log(error)
            res.status(500).json("Error interno")
        }
    }

}

export default controllersReview;

