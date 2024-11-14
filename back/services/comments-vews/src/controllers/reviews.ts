import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { Any, QueryBuilder, Repository, Timestamp, View } from "typeorm";
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

    registro_user:async(req:Request, res:Response):Promise<void>=>{
        try{

            const {id_user, id_prov}=req.params

            const {calificacion, commentario,id_imageRelation,
                //requst para las imagenes
                funcionality, urlLocation, idUsedOn, table
            }= req.body


            const id_usuario =  Number(id_user)
            const id_provedor =Number(id_prov)

            console.log(id_usuario, id_provedor)

            console.log("Se esta haciendo la consulta")
            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoin("appointment.users", "users")
            .where("(appointment.users = :id_logued or appointment.providers = :id_prov) or (appointment.users = :id_prov or appointment.providers = :id_logued) ", 
                {
                id_logued: id_usuario,
                id_prov: id_provedor
                })
            .getOne();
            
                const id_app = verificar?.id_appointment;
                const id_prove =verificar?.providers.id_provider
                let origenComoUser = false

               
                console.log("Este es el id_prov de la consulta", id_prove, "Este es el provedor de cb", id_provedor)

                if(id_app){
                    console.log("Entro en el registro")

                    if(id_provedor===id_prove){
                        origenComoUser = true
                        console.log("Se registra el comentario como provedor")
                   }
                    
                   console.log("Este es el dato que pasa", origenComoUser)
                   
                    const acuerdo = await reppsitoryappointment.findOne({where:{id_appointment:id_app}})
                    // console.log("Este es el acuerdo", acuerdo)

                    if(acuerdo && id_app){
                 
                        console.log("Entro en la funcion para poder registrar-------------------")
                        const conexion = new ImagenService();
                        await conexion.PostImage({funcionality,urlLocation,idUsedOn},table)

                        //logica para guardar el id de la imagen+

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
            let id_prov=req.params.id_prov
            let user = req.params.id_user

            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoinAndSelect("appointment.users", "users")
            .leftJoinAndSelect("appointment.interaccion", "interaccion")
            .leftJoinAndSelect("interaccion.review", "review")
            .where("(users.id_user = :id_logued and providers.id_provider = :id_prov)", {
                id_logued: user,
                id_prov: id_prov
            })
           
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .getOne();

            
                let id_user = verificar?.users.id_user
                let name_User=verificar?.users?.name_User
                let lasname=verificar?.users?.lasname
                let origenEmitidoComoUser=verificar?.interaccion?.origenEmitidoComoUser
                
                let id_provider=verificar?.providers?.id_provider
                let id_review=verificar?.interaccion?.review?.id_review
                let id_interaccion=verificar?.interaccion?.id_interaccion
                let calificacion=verificar?.interaccion?.review?.calificacion
                let comment=verificar?.interaccion?.review?.comment

                //logica para recuperar la imagen
                let image=verificar?.interaccion?.review?.image
                
                const desfragment ={
                    id_user,
                    name_User,
                    lasname,
                    origenEmitidoComoUser,
                    calificacion:{
                        id_provider,
                        id_review,
                        id_interaccion,
                        calificacion,
                        comment,
                        image
                    }
                }

            if(!desfragment.id_user){
                res.status(400).json("Sin comentarios")
                return
            }
            res.status(200).json({desfragment})
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
            // console.log("Roles")
            // console.log(roles)

            const informacion = [];

            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoinAndSelect("appointment.users", "users")
            .leftJoinAndSelect("appointment.interaccion", "interaccion")
            .leftJoinAndSelect("interaccion.review", "review")
            .where("(users.id_user = :id_logued OR providers.id_provider = :id_prov)", {
                id_logued: id_logued,
                id_prov: id_prov
            })
            .andWhere("interaccion.id_interaccion IS NOT NULL")
            .getMany();

            let usuario= false;

            for(let i=0; i<verificar.length; i++){
                let valor = verificar[i].interaccion   
                let origen = verificar[i].users?.id_user
                
                if(valor){

                    if(origen===id_logued){
                        console.log("Es emitido como usuario")
                        usuario=true
                    }
                        const desfragment =verificar.map(verificar=>({
                        id_user:verificar.users.id_user,
                        Nombre:verificar.users.name_User,
                        apellido:verificar.users.lasname,
                        origenComoUser:usuario,
                        id_appointment:verificar?.id_appointment,
                        id_prov:verificar?.providers?.id_provider,
                        calificacion:{
                        id_review:verificar?.interaccion?.review?.id_review,
                        id_interaccion:verificar?.interaccion?.id_interaccion,
                        calificacion:verificar?.interaccion?.review?.calificacion,
                        comentario:verificar?.interaccion?.review?.comment,
                        imagen:verificar?.interaccion?.review?.image
                        }
                    }))
    
                   
                    informacion.push(...desfragment)
                }     
            }

            // console.log("Este es el tamaño de los de los comentarios ", verificar.length)
            // console.log(informacion) 
            if(informacion.length===0){
             res.status(200).json({mensages:"Sin comentarios"})
             return;   
            }
            res.status(200).json(informacion)
            
        }catch(error){
            console.log("Hay un error interno ", error)
            res.status(500).json({message:"Error interno en el servidor"})
        }
    },

    edit:async(req:Request,res:Response):Promise<void>=>{
        try {
            const { id_user, id_dest } = req.params;
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
                .where("appointment.users = :id_logued", { id_user })
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
    },

    registroProv:async(req:Request, res:Response):Promise<void>=>{
        try{

            const {id_user, id_prov}=req.params

            const {calificacion, commentario,id_imageRelation,
                //requst para las imagenes
                funcionality, urlLocation, idUsedOn, table
            }= req.body


            const id_usuario =  Number(id_user)
            const id_provedor =Number(id_prov)

            console.log(id_usuario, id_provedor)

            console.log("Se esta haciendo la consulta")
            const verificar = await reppsitoryappointment.createQueryBuilder("appointment")
            .leftJoinAndSelect("appointment.providers", "providers")
            .leftJoin("appointment.users", "users")
            .where("(appointment.users = :id_logued or appointment.providers = :id_prov) or (appointment.users = :id_prov or appointment.providers = :id_logued) ", 
                {
                id_logued: id_usuario,
                id_prov: id_provedor
                })
            .getOne();
            
                const id_app = verificar?.id_appointment;
                const id_prove =verificar?.providers.id_provider
                let origenComoUser = true

                

               
                console.log("Este es el id_prov de la consulta", id_prove, "Este es el provedor de cb", id_provedor)

                if(id_app){
                    console.log("Entro en el registro")

                    if(id_provedor===id_prove){
                        origenComoUser = false
                        console.log("Se registra el comentario como provedor")
                   }
                    
                   console.log("Este es el dato que pasa", origenComoUser)
                   
                    const acuerdo = await reppsitoryappointment.findOne({where:{id_appointment:id_app}})
                    // console.log("Este es el acuerdo", acuerdo)

                    if(acuerdo && id_app){
                 
                        console.log("Entro en la funcion para poder registrar-------------------")
                        const conexion = new ImagenService();
                        await conexion.PostImage({funcionality,urlLocation,idUsedOn},table)

                        //logica para guardar el id de la imagen+

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
}

export default controllersReview;

