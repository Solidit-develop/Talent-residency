import { Request, Response } from "express";
import { AppDataSource } from "../database";
import { userTypes } from "../entitis/typesUsers";
import { users } from "../entitis/users";
import { Providers } from "../entitis/provedores";
import { Address } from "../entitis/adrdess";
import { Town } from "../entitis/town";
import { State } from "../entitis/state";
import { skills } from "../entitis/skill";

const repositoryTypeU = AppDataSource.getRepository(userTypes);
const repositoryUser = AppDataSource.getRepository(users);

const repositoryTowns = AppDataSource.getRepository(Town);
const repositoryState = AppDataSource.getRepository(State);
const repositoryAddress = AppDataSource.getRepository(Address);
const repositoryProviders = AppDataSource.getRepository(Providers);
const repositoryskills = AppDataSource.getRepository(skills);

//cambiar el tipo de usuarios en la tabla de users

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
            const {
                email,
                name_state, // Estado
                zipcode, name_Town, // Ciudad
                street_1, street_2, localidad, // Dirección
                skill, experienceYears, workshopName, workshopPhoneNumber // Proveedor
            } = req.body;

            // Verificar que el correo esté presente en la solicitud
            if (!email) {
                res.status(400).json({ mensaje: "El correo es requerido" });
                return;
            }

            // Buscar el usuario por correo
            let user = await repositoryUser.findOne({
                where: { email },
                relations: ['usertypes']
            });

            // Verificar si el usuario existe
            if (!user) {
                res.status(404).json({ mensaje: "Usuario no encontrado" });
                return;
            }

            const userTypeId = user.usertypes ? user.usertypes.id_userType : null;
            
            // Buscar el tipo de usuario asociado al usuario encontrado
            const typeUser = userTypeId ? await repositoryTypeU.findOne({ where: { id_userType: userTypeId } }) : null;

            // Verificar si el tipo de usuario existe
            if (!typeUser) {
                res.status(404).json({ mensaje: "Tipo de usuario no encontrado" });
                return;
            }

            // Cambiar el estatus del tipo de usuario
            const values = true;
            const descripcion = "Proveedor";


            let tipoUsuario = await repositoryTypeU.findOne({where:{descripcion:descripcion,value:values}}) 
            if(!tipoUsuario){
                tipoUsuario = new userTypes();
            tipoUsuario.value = values;
            tipoUsuario.descripcion = descripcion;
            await repositoryTypeU.save(tipoUsuario);
            }

             //Actualizamos el tipo de usuario en la tabla de usuario
           if(user){
            user.usertypes=tipoUsuario
            await repositoryUser.save(user)
           }
            


            

            // Agregar o actualizar el estado
            let estado = await repositoryState.findOne({ where: { name_State: name_state } });
            if (!estado) {
                estado = new State();
                estado.name_State = name_state;
                await repositoryState.save(estado);
            }

            // Agregar o actualizar la ciudad
            let ciudad = await repositoryTowns.findOne({
                where: { name_Town, zipCode: zipcode }
            });
            if (!ciudad) {
                ciudad = new Town();
                ciudad.name_Town = name_Town;
                ciudad.zipCode = zipcode;
                ciudad.state = estado;
                await repositoryTowns.save(ciudad);
            }

            // Agregar o actualizar la dirección
            let address = await repositoryAddress.findOne({
                where: { street_1, street_2, localidad }
            });
            if (!address) {
                address = new Address();
                address.street_1 = street_1;
                address.street_2 = street_2;
                address.localidad = localidad;
                address.town = ciudad;
                await repositoryAddress.save(address);
            }

            // Agregar o actualizar la habilidad
            let skillEntity = await repositoryskills.findOne({ where: { name: skill } });
            if (!skillEntity) {
                skillEntity = new skills();
                skillEntity.name = skill;
                await repositoryskills.save(skillEntity);
            }

            // Agregar la nueva habilidad al proveedor sin actualizar el resto de la información
            let proveedor = await repositoryProviders.findOne({
                where: { workshopName },
                relations: ['skills'] // Asegúrate de cargar la relación skills
            });

            if (proveedor) {
                // Inicializar skills como un array vacío si es undefined
                proveedor.skills = proveedor.skills || [];

                // Solo agregar nuevas habilidades si no existen
                if (!proveedor.skills.some(existingSkill => existingSkill.name === skillEntity.name)) {
                    proveedor.skills.push(skillEntity);
                    await repositoryProviders.save(proveedor); 
                    
                }
            } else {
                // Si el proveedor no existe, crea uno nuevo
                proveedor = new Providers();
                proveedor.experienceYears = experienceYears;
                proveedor.workshopName = workshopName;
                proveedor.workshopPhoneNumber = workshopPhoneNumber;
                proveedor.address = address;
                proveedor.skills = [skillEntity]; // Asociar habilidades al proveedor
                proveedor.user=user;
                await repositoryProviders.save(proveedor);
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
