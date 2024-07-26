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

        try{
            const allUs= await repositoriuser.find()
            console.log(allUs)
            console.log("Pruebas");
            res.status(200).json({mesage:"codigo procesado con exito"})
        }catch(error){
            console.log(error);
            res.send(500).json({mesage:"error interno del servidor"})
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
                street_1,street_2,localidad, // tabla de address
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

            let ciudad = await repositoriTown.findOne({where: {name_Town:name_Town, zipCode:zipcode}})
            if(!ciudad){
                ciudad= new Town();
                ciudad.name_Town= name_Town;
                ciudad.zipCode= zipcode;
                ciudad.state=estado;
                console.log(ciudad)
                await repositoriTown.save(ciudad);
            }

            let addressentitits = await repositoriAddress.findOne({where:{street_1:street_1, street_2:street_2}})
            if(!addressentitits){
            addressentitits = new Address();
            addressentitits.street_1=street_1;
            addressentitits.street_2= street_2;
            addressentitits.localidad=localidad;
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
            
            let usert = await repositoriuser.findOne({where:[{name_User:name_user}, {email:email}]}) 
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
            res.status(200).json({mesage:"Se registro un nuevo usuario"})

            }else{
                res.status(400).json({mesage:"El usuario ya existe"})
            }

            
        }catch(error){
            console.log("Error interno en el servidor")
            console.log(error)
            res.status(500).json({mesage:"Error interno en el servidor"})
        }
    },  
    
    actualizarDatos: async (req: Request, res: Response): Promise<void> => {
        try {
            
            const userId = parseInt(req.params.id, 10);

            if (isNaN(userId)) {
                res.status(400).json({ message: "ID de usuario inválido" });
                return;
            }

           
            const {
                name_State,
                zipcode,
                name_Town,
                street_1,
                street_2,
                location,
                name_user,
                lastname,
                age,
                phoneNumber,
                email
            } = req.body;

            
            let usuario = await repositoriuser.findOne({ where: { id_user: userId } });
            console.log("Usuario sin actualizar")
            console.log(usuario);

            if (usuario) {
                
                let estado = await repositoriState.findOne({ where: { name_State: name_State } });
                if (!estado) {
                    estado = new State();
                    estado.name_State = name_State;
                    await repositoriState.save(estado); 
                }

            
                let ciudad = await repositoriTown.findOne({ where: { name_Town: name_Town, zipCode: zipcode } });
                if (!ciudad) {
                    ciudad = new Town();
                    ciudad.name_Town = name_Town;
                    ciudad.zipCode = zipcode;
                    ciudad.state = estado;
                    await repositoriTown.save(ciudad); 
                }

                
                let direccion = await repositoriAddress.findOne({ where: { street_1: street_1, street_2: street_2 } });
                if (!direccion) {
                    direccion = new Address();
                    direccion.localidad = location;
                    direccion.street_1 = street_1;
                    direccion.street_2 = street_2;
                    direccion.town = ciudad;
                    await repositoriAddress.save(direccion); 
                }
                usuario.name_User = name_user;
                usuario.email = email;
                usuario.lasname = lastname;
                usuario.age = age;
                usuario.phoneNumber = phoneNumber;
                usuario.adress = direccion; 

                await repositoriuser.save(usuario); 
               
                console.log("Usuario ya actualizado");
                console.log(usuario);
                res.json(usuario);
                console.log("Usuario actualizado correctamente");
            } else {
                res.status(404).json({ message: "Usuario no encontrado" });
            }


        } catch (error) {
            console.error("Error al actualizar los datos:", error);
            res.status(500).json({ message: "Error interno del servidor" });
        }
    }

        

};


export default controllerusuario;


