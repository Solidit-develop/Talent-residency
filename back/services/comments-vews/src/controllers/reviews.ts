import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { QueryBuilder, Timestamp } from "typeorm";
import { interaccion } from "../entitis/interaccion";
import { Providers } from "../entitis/provedores";
import { review } from "../entitis/review";
import { appointment } from "../entitis/appointment";

const repositoryuser= AppDataSource.getRepository(users);
const repositoryinteraccion = AppDataSource.getRepository(interaccion)
const repositoryprovedor = AppDataSource.getRepository(Providers)
const repositoryreview = AppDataSource.getRepository(review)
const reppsitoryappointment = AppDataSource.getRepository(appointment)

const controllersReview={

    ping: async(req:Request, res:Response): Promise<void> => {
        res.send("pong");
    },


    consulta:async(req:Request, res:Response):Promise<void>=>{
        try{

            const usuarios = repositoryuser
            const provedores = repositoryprovedor.find()
            const reviews =  repositoryreview.find();
            const appointment = reppsitoryappointment.find();
            const interaccion = repositoryinteraccion.find();

            console.log("Usuarios")
            console.log(usuarios)
            console.log("Provedore")
            console.log(provedores)
            console.log("Reviews")
            console.log(reviews)
            console.log("appointment")
            console.log(appointment)
            console.log("interaccion")
            console.log(interaccion)

            res.json("Todo bien ntp")
        }catch(error){
            res.json(error)
            console.log(error)
        }
    }

}

export default controllersReview;
