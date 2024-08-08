import { Request,Response } from "express";
import { State } from "../entitis/state";
import { Address } from "../entitis/adrdess";
import { userTypes } from "../entitis/typesUsers";
import { users } from "../entitis/users";
import { Town } from "../entitis/town";
import { AppDataSource } from "../database";
import bcryptjs from 'bcryptjs'
import transporter from "../mail/mailer"
import  generateRandomToken from "../mail/tokengenerator"
import jwt from "jsonwebtoken"

const repositoriState = AppDataSource.getRepository(State);
const repositoriTown = AppDataSource.getRepository(Town);
const repositoriAddress = AppDataSource.getRepository(Address);
const repositoriuser= AppDataSource.getRepository(users);
const repositoritypeU= AppDataSource.getRepository(userTypes);
const gmail = "devsolidit@gmail.com"


interface DecodedToken {
    name_state: string;
    zipcode: string;
    name_Town: string;
    street_1: string;
    street_2: string;
    localidad: string;
    name_user: string;
    lastname: string;
    email: string;
    password: string;
    age: number;
    phoneNumber: string;
  }
  
  const SECRET_KEY = 'Secret';
const controllerusuario={
    registroUsuario:async(req:Request,res:Response):Promise<void>=>{
        try{
            const{name_state,//tabla de state
                zipcode,name_Town, // tabla de town
                street_1,street_2,localidad, // tabla de address
                name_user,lastname,email,password,age,phoneNumber //tabla de lo usuarios
            }=req.body;
            
            let passwordhas = await bcryptjs.hash(password,8)
            const token = await generateRandomToken();
            const encodedToken = encodeURIComponent(token);
            const tokenData = jwt.sign(
                {name_state,zipcode,name_Town,street_1,street_2,localidad,name_user,lastname,email,passwordhas,age,phoneNumber},
                SECRET_KEY,
                {expiresIn: "5h"}
            );
            console.log("Se envio  este roken hash")
            console.log(token)

            console.log("Se envio el siguiente token")
            console.log(tokenData)

            await transporter.sendMail({
                from: `"Talent-Residencity" <${gmail}>`, // Envia el correo
                to: email, // Lista de los correos que lo recibirán
                subject: "Verificación de seguridad.", // Este será el asunto
                html: `
                      <!DOCTYPE html>
                      <html lang="es">
                      <head>
                          <meta charset="UTF-8">
                          <meta name="viewport" content="width=device-width, initial-scale=1.0">
                          <title>Validacion</title>
                          <style>
                              body {
                                  font-family: Arial, sans-serif;
                                  text-align: center;
                                  margin: 20px;
                              }
          
                              button {
                                  padding: 10px 20px;
                                  font-size: 16px;
                                  margin: 5px;
                                  cursor: pointer;
                              }
          
                              #cancelar {
                                  background-color: #e74c3c;
                                  color: #fff;
                                  border: none;
                              }
          
                              #aceptar {
                                  background-color: #2ecc71;
                                  color: #fff;
                                  border: none;
                              }
                              img {
                                  padding: 1px 1px;
                                  width: 200px;  /* Ancho deseado en píxeles */
                                  height: 200px; /* Altura deseada en píxeles */
                              }
                          </style>
                      </head>
                      <body>
                          <h1>Verificación de datos.</h1>
                          <img src="https://static.vecteezy.com/system/resources/previews/006/925/139/non_2x/play-button-white-color-lock-user-account-login-digital-design-logo-icon-free-photo.jpg" alt="Inicio de sesión">
                          <h3>Para concluir con el registro, valida en el siguiente campo.</h3>
                          <a href="http://localhost:4001/usr/verificacion/${tokenData}">
                              <button id="aceptar">Aceptar</button>
                          </a>
                          <a href="http://localhost:3200/api/login/cancelar/${encodedToken}">
                              <button id="cancelar">Cancelar</button> 
                          </a>
                      </body>
                      </html> `,
              });
            
              res.status(200).json({mesage:"Se envio un correo para verioficarlo"})

        }catch(error){
            console.log(error)
            res.status(500).json({nesage:"Error interno del servidor"})
        }
    },



        verificacion: async (req: Request, res: Response): Promise<void> => {
          try {
            // Obtén el token de los parámetros de la URL
            const decodedToken = decodeURIComponent(req.params.token);
      
            if (!decodedToken) {
              res.status(400).json({ message: "Token no recibido" });
              return;
            }
            try {
              // Verifica y decodifica el token
              const token = jwt.verify(decodedToken, SECRET_KEY) as DecodedToken;

              console.log(token)
              // Aquí puedes realizar acciones adicionales con los datos decodificados si es necesario
              // Por ejemplo, actualizar la base de datos, o simplemente devolver los datos
              console.log("codifico la impresion de que se va a imprimir")
                console.log(token.name_user,token.lastname)
              res.status(200).json({
                message: 'Token verificado exitosamente',
                data: token,
              });
              console.log("Se decodifico el token")
            //   console.log(token)

            } catch (err) {
              // Si ocurre un error durante la verificación (token inválido o expirado)
              console.log(err);
              res.status(401).json({ message: "El token es inválido o ha expirado" });
            }
          } catch (error) {
            console.log(error);
            res.status(500).json({ message: 'No fue posible conectar con el servidor.' });
          }
        },
    


//    verificacion: async (req: Request, res: Response): Promise<void> => {
//        try {
//         console.log("Entro en primer try");
//          const {token} = req.params;

//          if (!token){
//             res.status(400).json({mesage:"token no resivido"});
//             return;
//          }
//          try {
//             console.log("Entro en el segundo try");
//            // Verifica y decodifica el token
//            const decodedToken = jwt.verify(token, SECRET_KEY) as DecodedToken;
//         console.log("Salto la decodificacion")
//            console.log(decodedToken.name_user,decodedToken.lastname);
//            // Aquí puedes hacer algo con los datos decodificados, como actualizar la base de datos
//            res.status(200).json({
//              message: 'Token verificado exitosamente',
//              data: decodedToken,
//            });
//          } catch (err) {
//             console.log(err)
//            res.status(404).json({message:"El token expiro"});
//          }
//        } catch (error) {
//         console.log(error);
//          res.status(500).json({ message: 'No fue posible conectar con el servidor.'});
//        }
//      },
    
    

   prueba:async(req:Request,res:Response):Promise<void>=>{

        try{
            const allUs= await repositoriuser.find()
            console.log(allUs)
            console.log("Pruebas");
            res.status(200).json(allUs)
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


