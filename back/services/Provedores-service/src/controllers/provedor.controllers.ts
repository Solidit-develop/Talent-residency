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

    infocomplete: async (req: Request, res: Response): Promise<void> => {
        try {
            const { correo } = req.body;

            const {name_state,//tabla de state
                zipcode,name_Town, // tabla de town
                street_1,street_2,localidad, // tabla de address
                skills,experienceYears,workshopYear,workshopName,worckshopPhoneNumber
            
            } = req.body;

            // Verificar que el correo esté presente en la solicitud
            if (!correo) {
                res.status(400).json({ mensaje: "El correo es requerido" });
                return;
            }

            // Buscar el usuario por correo
            let user = await repositoryUser.findOneBy({ email: correo });
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

                res.status(404).json({ mensaje: "Tipo de usuario no encontrado" });
                return;
            }else {
                // cambiamos el estatus 
                const values = true;
                let tipousuario = new userTypes;
                tipousuario.value= values;
                //logica para cambiar el estatus

                // se agregan la informacion actualizada (Agregar informacion del provedor)
                let estado =  await repositoryState.findOne({where:{name_State:name_state}})
                if(!estado){
                    estado = new State();
                    estado.name_State = name_state;
                    await repositoryState.save(estado);
                }
                
                let ciudad = await repositoryTowns.findOne({where:{name_Town:name_Town, zipCode:zipcode}})
                if(!ciudad){
                    ciudad= new Town();
                    ciudad.name_Town= name_Town;
                    ciudad.zipCode= zipcode;
                    ciudad.state=estado;
                    console.log(ciudad)
                    await repositoryTowns.save(ciudad);
                }
                let addressentitits = await repositoryAddress.findOne({where:{street_1:street_1, street_2:street_2}})
                if(!addressentitits){
                addressentitits = new Address();
                addressentitits.street_1=street_1;
                addressentitits.street_2= street_2;
                addressentitits.localidad=localidad;
                addressentitits.town=ciudad;
                await repositoryAddress.save(addressentitits);    
                } 

                let provedores = await repositoryProviders.findOne ({where:{}})
                
                
            }

            // Responder con los datos encontrados
            res.status(200).json({ user, typeUser });

        } catch (error) {
            console.error(error);
            res.status(500).json({ mensaje: "Error interno en el servidor" });
        }
    }

}

export default controllerProvider;
