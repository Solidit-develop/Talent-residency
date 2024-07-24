import { promises } from "dns";
import { Request,Response } from "express";
import { State } from "../entitis/state";
import { Address } from "../entitis/adrdess";
import { userTypes } from "../entitis/typesUsers";
import { users } from "../entitis/users";
import { Town } from "../entitis/town";
import { AppDataSource } from "../database";


const repositoriState = AppDataSource.getRepository(State);
const repositoriTown = AppDataSource.getRepository(Town);
const repositoriAddress = AppDataSource.getRepository(Address);
const repositoriuser= AppDataSource.getRepository(users);
const repositoritypeU= AppDataSource.getRepository(userTypes);

const controllerusuario={
    

   prueba:async(req:Request,res:Response):Promise<void>=>{
        console.log("prueba")


        try{
            const {nombre,apellido}=req.body;
            console.log(nombre," ", apellido)
            res.status(200).json({mesage:"codigo procesado con exito"})
        }catch(error){
            console.log(error);
            // res.send(500).json({mesage:"error interno del servidor"})
        }    
    },

    inicio_sesion:async(req:Request, res:Response):Promise<void>=>{
        try{
            const{email,password}=req.body
            let usuario = await repositoriuser.findOne({where:{email:email , password:password}})
            if(!usuario){
                res.status(400).json({mesage:"credenciales no existen"})
            }else{
                console.log(usuario)
                res.status(200).json({mesage:`hola ${usuario.name_User} beinvenido`})
            }
        }catch(error){
            res.status(500).json({mesage:"Error interno del servidor"})
            console.log(error);
        }
    },
    
    insertusuario:async(req:Request, res:Response):Promise<void>=>{
        try{
            
            let descripcion = "usuario"
            let value=false; // tabla de tipo de cliente
            
            const{name_state,//tabla de state
                zipcode,name_Town, // tabla de town
                street_1,street_2, // tabla de address
                name_user,lastname,email,password,age,phoneNumber //tabla de lo usuarios
            }=req.body;

            console.log("Referencia")
            let estado  = await repositoriState.findOne({ where: {name_State:name_state} });

            if (!estado){
                console.log("No se encontro el estado")
                estado = new State();
                estado.name_State=name_state;
                await repositoriState.save(estado)
            }

            let ciudad = await repositoriTown.findOne({where: {name_Town:name_state}})
            if(!ciudad){
                ciudad= new Town();
                ciudad.name_Town= name_Town;
                ciudad.zipCode= zipcode;
                ciudad.state=estado;
                await repositoriTown.save(ciudad);
            }

            let addressentitits = await repositoriAddress.findOne({where:{street_1:street_1, street_2:street_2}})
            if(!addressentitits){
            addressentitits = new Address();
            addressentitits.street_1=street_1;
            addressentitits.street_2= street_2;
            addressentitits.town=ciudad;
            await repositoriAddress.save(addressentitits);    
            } 


            let estatus = await repositoritypeU.findOne({where:{descripcion:descripcion,value:value}})
            if(!estatus){
                estatus= new userTypes();
                estatus.descripcion=descripcion;
                estatus.value= value;
                await repositoritypeU.save(estatus)
            }

            
            let usert = await repositoriuser.findOne({where:{name_User:name_user, email:email}}) 
            if(!usert){
            usert = new users();
            usert.name_User=name_user;
            usert.lasname=lastname;
            usert.email= email;
            usert.password= password;
            usert.age= age;
            usert.phoneNumber= phoneNumber;
            usert.adress=addressentitits;
            usert.usertypes=estatus;
            await repositoriuser.save(usert)

            }else{
                res.status(400).json({mesage:"El usuario ya existe"})
            }

            // res.status(200).json({mesaje:"Todo salio bein"})
        }catch(error){
            console.log("Error interno en el servidor")
            console.log(error)
            res.status(500).json({mesage:"Error interno en el servidor"})
        }
    },  

    actualizardatos:async(req:Request, res:Response):Promise<void>=>{
        try{
            console.log("esto es una prueba para que se valla todo el repositorio")
        }catch(error){

        }
    }
};


export default controllerusuario;


