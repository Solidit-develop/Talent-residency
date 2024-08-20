import { Request, Response } from "express";
import { AppDataSource } from "../database";
import { userTypes } from "../entitis/typesUsers";
import { users } from "../entitis/users";
import { Providers } from "../entitis/provedores";
import { Address } from "../entitis/adrdess";
import { Town } from "../entitis/town";
import { State } from "../entitis/state";

const repositoryTypeU = AppDataSource.getRepository(userTypes);
const repositoryUser = AppDataSource.getRepository(users);

const repositoryTowns = AppDataSource.getRepository(Town);
const repositoryState = AppDataSource.getRepository(State);
const repositoryAddress = AppDataSource.getRepository(Address);
const repositoryProviders = AppDataSource.getRepository(Providers);

const controllerProvider = {

    // verificar si es Provedor 

    statusUsuario : async (req:Request, res:Response): Promise<void>=>{

        const {email}= req.body
        let id_user = await repositoryUser.findOne({where:{email:email},relations:['usertypes']})
        console.log("este es el usuario")
        console.log(id_user)

        if(id_user!=null){
            console.log("Sí se encontró");
        console.log(id_user.usertypes); // Accede a usertypes del usuario

        res.status(200).json({
            mensaje: "Sí se encontró el usuario",
            usertypes: id_user.usertypes
        });
        }else{
            res.status(400).json({mesaje :"No se encontro el usuario"})
            console.log("No se encontro")
            return;
        }
        
       
    },

    // complemento de informacion

    infocomplete: async (req: Request, res: Response): Promise<void> => {
        try {
            const { email } = req.body;

            const {name_state,//tabla de state
                zipcode,name_Town, // tabla de town
                street_1,street_2,localidad, // tabla de address
                skills,experienceYears,workshopName,worckshopPhoneNumber
            
            } = req.body;

            // Verificar que el correo esté presente en la solicitud
            if (!email) {
                res.status(400).json({ mensaje: "El correo es requerido" });
                return;
            }

            // Buscar el usuario por correo
            let user = await repositoryUser.findOneBy({ email: email });
            console.log(user)
            // Verificar si el usuario existe
            if (!user) {
                res.status(404).json({ mensaje: "Usuario no encontrado" });
                return;
            }

            // Buscar el tipo de usuario asociado al usuario encontrado
            let typeUser = await repositoryTypeU.findOneBy({ usertypes: user.usertypes });
            console.log(typeUser?.value);
            // Verificar si el tipo de usuario existe


            
            if (!typeUser) {
                console.log(typeUser)
                res.status(404).json({ mensaje: "Tipo de usuario no encontrado" });
                return;
            }else {
                // cambiamos el estatus 

                console.log("Se cambia el tipo de usuario")
                // const values = true;
                // const descripcion = "Provedor"
                // let tipousuario = new userTypes;
                // tipousuario.value= values;
                // tipousuario.descripcion=descripcion
                // await repositoryTypeU.save(tipousuario);
                //logica para cambiar el estatus

                // se agregan la informacion actualizada (Agregar informacion del provedor)
                let estado =  await repositoryState.findOne({where:{name_State:name_state}})
                if(!estado){
                    console.log("Se agrega un nuevo estado")
                    // estado = new State();
                    // estado.name_State = name_state;
                    // await repositoryState.save(estado);
                }
                
                //insertar la nueva ciudad
                let ciudad = await repositoryTowns.findOne({where:{name_Town:name_Town, zipCode:zipcode}})
                if(!ciudad){
                    console.log("Se agrega una nueva ciudad")
                    // ciudad= new Town();
                    // ciudad.name_Town= name_Town;
                    // ciudad.zipCode= zipcode;
                    // ciudad.state=estado;
                    // console.log(ciudad)
                    // await repositoryTowns.save(ciudad);
                }
                //  se agrega la nuenva direccion
                let addressentitits = await repositoryAddress.findOne({where:{street_1:street_1, street_2:street_2}})
                if(!addressentitits){
                    console.log("Se inserta una nueva direccion")
                // addressentitits = new Address();
                // addressentitits.street_1=street_1;
                // addressentitits.street_2= street_2;
                // addressentitits.localidad=localidad;
                // addressentitits.town=ciudad;
                // await repositoryAddress.save(addressentitits);    
                } 
                // insertar los provedores
                let provedores = await repositoryProviders.findOne ({where:{skill:skills}})
                if(!provedores){
                    // provedores = new Providers();
                    // provedores.skill= skills;
                    // provedores.experienceYears= experienceYears;
                    // provedores.workshopName = workshopName;
                    // provedores.workshopPhoneNumber= worckshopPhoneNumber;
                    
                    // provedores.address= addressentitits;
                    // await repositoryProviders.save(provedores);

                    console.log("Se agrega un nuevo provedor")
                }                
            }
            // Responder con los datos encontrados
            res.status(200).json({ user, typeUser });

        } catch (error) {
            console.error(error);
            res.status(500).json({ mensaje: "Error interno en el servidor" });
        }
    }

    // top segun sucalificacion 


}

export default controllerProvider;
