import { Request, Response } from "express";
import { State } from "../entitis/state";
import { Address } from "../entitis/adrdess";
import { userTypes } from "../entitis/typesUsers";
import { users } from "../entitis/users";
import { Town } from "../entitis/town";
import { AppDataSource } from "../database";
import bcryptjs from 'bcryptjs'
import transporter from "../mail/mailer"
import generateRandomToken from "../mail/tokengenerator"
import jwt from "jsonwebtoken"
import {ImagenService} from "../Services/imagenes-service"

import { ResponseModel } from "../models/responseDto";
import config from "../config";
import { ProviderService } from "../Services/provider-service";

const repositoriState = AppDataSource.getRepository(State);
const repositoriTown = AppDataSource.getRepository(Town);
const repositoriAddress = AppDataSource.getRepository(Address);
const repositoriuser = AppDataSource.getRepository(users);
const repositoritypeU = AppDataSource.getRepository(userTypes);
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
  passwordhas: string;
  age: string;
  phoneNumber: string;

}

const SECRET_KEY = 'Secret';

const controllerusuario = {
  registroUsuario: async (req: Request, res: Response): Promise<void> => {
    // LocalConfig - AloneDeploy
    // const baseAddress = "http://localhost:4001"

    // LocalConfig - ApiGatewayDeploy
    // const baseAddress = "http://localhost:4000/api/v1/users"

    // DeployConfig - ApiGateway Deploy
    const baseAddress = config.baseAddress

    console.log("BaseAddress: ", baseAddress);
    try {
      const { name_state,//tabla de state
        zipcode, name_Town, // tabla de town
        street_1, street_2, localidad, // tabla de address
        name_user, lastname, email, password, age, phoneNumber //tabla de lo usuarios
      } = req.body;
      const value = false;
      const descripcion = "usuario";
      let passwordhas = await bcryptjs.hash(password, 8)
      const token = await generateRandomToken();
      const encodedToken = encodeURIComponent(token);
      const tokenData = jwt.sign(
        { name_state, zipcode, name_Town, street_1, street_2, localidad, name_user, lastname, email, passwordhas, age, phoneNumber, value, descripcion },
        SECRET_KEY,
        { expiresIn: "5h" }
      );
      console.log("Token hash enviado")
      console.log(token)

      console.log("Token data enviado")
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
                                  inline-size: 200px;  /* Ancho deseado en píxeles */
                                  block-size: 200px; /* Altura deseada en píxeles */
                              }
                          </style>
                      </head>
                      <body>
                          <h1>Verificación de datos.</h1>
                          <img src="https://static.vecteezy.com/system/resources/previews/006/925/139/non_2x/play-button-white-color-lock-user-account-login-digital-design-logo-icon-free-photo.jpg" alt="Inicio de sesión">
                          <h3>Para concluir con el registro, valida en el siguiente campo.</h3>
                          <a href="${baseAddress}/validate/${tokenData}">
                              <button id="aceptar">Aceptar</button>
                          </a>
                          <a href="http://localhost:3200/api/login/cancelar/${encodedToken}">
                              <button id="cancelar">Cancelar</button> 
                          </a>
                      </body>
                      </html> `,
      });

      res.status(200).json(ResponseModel.successResponse("Correo de verificación enviado correctamente"))

    } catch (error) {
      console.log(ResponseModel.errorResponse(500, `Error interno: ${error}`))
      res.status(500).json(ResponseModel.errorResponse(500, `Error interno: ${error}`))
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
        console.log("Token codificado con exito")
        console.log(token)
        console.log("Referencia")
        let estado = await repositoriState.findOne({ where: { name_State: token.name_state } });

        if (!estado) {
          console.log("No se encontro el estado")
          estado = new State();
          estado.name_State = token.name_state;
          await repositoriState.save(estado)
        }

        let ciudad = await repositoriTown.findOne({ where: { name_Town: token.name_Town, zipCode: token.zipcode } })
        if (!ciudad) {
          ciudad = new Town();
          ciudad.name_Town = token.name_Town;
          ciudad.zipCode = token.zipcode;
          ciudad.state = estado;
          console.log("Datos de la ciudad")
          await repositoriTown.save(ciudad);

        }

        let addressentitits = await repositoriAddress.findOne({ where: { street_1: token.street_1, street_2: token.street_2 } })
        if (!addressentitits) {
          addressentitits = new Address();
          addressentitits.street_1 = token.street_1;
          addressentitits.street_2 = token.street_2;
          addressentitits.localidad = token.localidad;
          addressentitits.town = ciudad;
          await repositoriAddress.save(addressentitits);

        }

        let descripcion = "usuario"
        let value = false;

        let estatus = await repositoritypeU.findOne({ where: { descripcion: descripcion, value: value } })
        if (!estatus) {
          estatus = new userTypes();
          estatus.descripcion = descripcion;
          estatus.value = value;
          await repositoritypeU.save(estatus)
        }

        let usert = await repositoriuser.findOne({ where: { email: token.email } })
        if (!usert) {
          console.log(token.passwordhas);
          usert = new users();
          usert.name_user = token.name_user;
          usert.lastname = token.lastname;
          usert.email = token.email;
          usert.password = token.passwordhas;
          usert.age = token.age;
          usert.phoneNumber = token.phoneNumber;
          usert.adress = addressentitits;
          usert.usertypes = estatus;
          await repositoriuser.save(usert)
          console.log("Se inserto con exito el usuario del token resivido")
          res.status(200).send(
            `<!DOCTYPE html>
                <html lang="es">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Registro Completado</title>
                <style>
                  body {
                    background-color: #05265b;
                    font-family: Arial, sans-serif;
                  }
                  .container {
                    text-align: center;
                    margin-block-start: 100px;
                  }
                  .success-message {
                    background-color: #4CAF50;
                    color: white;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    font-size: 24px;
                    inline-size: 300px;
                    margin: 0 auto;
                  }
                </style>
                </head>
                <body>
                <div class="container">
                  <div class="success-message">
                    ¡Registro completado con éxito bienvenido ve la app e inicia sesión!
                  </div>
                </div>
                </body>
                </html>`


          )

        } else {
          res.status(400).send(
            `<!DOCTYPE html>
        <html lang="es">
        <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro Completado</title>
        <style>
          body {
            background-color: #05265b;
            font-family: Arial, sans-serif;
          }
          .container {
            text-align: center;
            margin-block-start: 100px;
          }
          .success-message {
            background-color: #cfd441;
            color: rgb(0, 0, 0);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            font-size: 24px;
            inline-size: 300px;
            margin: 0 auto;
          }
        </style>
        </head>
        <body>
        <div class="container">
          <div class="success-message">
            ¡El correo ya existe intente de nuevo!
          </div>
        </div>
        </body>
        </html>`
          )
        }

        console.log("Se decodifico el token")


      } catch (err) {
        console.log(err);
        res.status(401).send(
          `
        <!DOCTYPE html>
        <html lang="es">
        <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro Completado</title>
        <style>
          body {
            background-color: #05265b;
            font-family: Arial, sans-serif;
          }
          .container {
            text-align: center;
            margin-block-start: 100px;
          }
          .success-message {
            background-color: #a61a1a;
            color: #eae6e6;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            font-size: 24px;
            inline-size: 300px;
            margin: 0 auto;
          }
        </style>
        </head>
        <body>
        <div class="container">
          <div class="success-message">
            <h1>Error</h1>
            ¡Error el tiempo de verificación expiro!
          </div>
        </div>
        </body>
        </html>`
        );
      }
    } catch (error) {
      console.log(error);
      res.status(500).json({ message: 'No fue posible conectar con el servidor.' });
    }
  },

  prueba: async (req: Request, res: Response): Promise<void> => {

    try {
      const allUs = await repositoriuser.find()
      console.log(allUs)
      console.log("Pruebas");
      res.status(200).json(allUs)
    } catch (error) {
      console.log(error);
      res.send(500).json({ mesage: "error interno del servidor" })
    }
  },

  ping: async (req: Request, res: Response): Promise<void> => {
    res.send("pong");
  },
  /**
   * TODO: Delete password from fields to return
   */
  inicio_sesion: async (req: Request, res: Response): Promise<void> => {
    try {
      const { email, password } = req.body
      console.log("Try to login email ", email);
      console.log("body: ", req.body);
      if (!req.body) {
        res.send(ResponseModel.errorResponse(400, "Error al enviar la solicitud: " + req.body));
        throw new Error();
      }
      let usuario = await repositoriuser.findOne({ where: { email: email } })
      let contrahas = String(usuario?.password)

      let conpare = bcryptjs.compareSync(password, contrahas)

      if (!conpare) {
        res.status(400).json(ResponseModel.errorResponse(400, "Credenciales inválidas"))
      } else {
        res.status(200).json(ResponseModel.successResponse(usuario))
      }
    } catch (error) {
      res.status(500).json(ResponseModel.errorResponse(500, "Error interno"))
      console.log(error);
    }
  },

  obtainInformation: async (req: Request, res: Response): Promise<void> => {
    const providerService = new ProviderService();
    const userImagen = new ImagenService();
    var user;
    var imagen
    var tabla = "users"
    var funcionality ="PerfilUser"
    let userIdToFind = req.params.idToFind;
    console.log("ID TO FIND: " + userIdToFind);
    
    try {
      user = await providerService.getUserInfo(userIdToFind)
      imagen = await userImagen.getImageInfo(tabla,userIdToFind,funcionality)
      // imagen = await 
        .then(resp => {
          console.log("Response on promise controller: " + JSON.stringify(resp));
          return resp;
        })
        .catch(error => {
          console.log("Error on promise controller: " + error);
        });
      console.log("Response del service: " + user);
    } catch (e) {
      user = e;
      console.log("Error: " + user);
      return;
    }

    console.log("Response on finish controller: " + user);

    const respon={
        response:{
          ...user,
          ...imagen
        }

    }

    res.status(200).json(ResponseModel.successResponse(respon));
  },

  insertusuario: async (req: Request, res: Response): Promise<void> => {
    try {

      let descripcion = "Usuario"
      let value = false; // tabla de tipo de cliente

      const { name_state,//tabla de state
        zipcode, name_Town, // tabla de town
        street_1, street_2, localidad, // tabla de address
        name_user, lastname, email, password, age, phoneNumber //tabla de lo usuarios
      } = req.body;

      console.log("Referencia")
      let estado = await repositoriState.findOne({ where: { name_State: name_state } });

      if (!estado) {
        console.log("No se encontro el estado")
        estado = new State();
        estado.name_State = name_state;
        await repositoriState.save(estado)
      }

      let ciudad = await repositoriTown.findOne({ where: { name_Town: name_Town, zipCode: zipcode } })
      if (!ciudad) {
        ciudad = new Town();
        ciudad.name_Town = name_Town;
        ciudad.zipCode = zipcode;
        ciudad.state = estado;
        console.log(ciudad)
        await repositoriTown.save(ciudad);
      }

      let addressentitits = await repositoriAddress.findOne({ where: { street_1: street_1, street_2: street_2 } })
      if (!addressentitits) {
        addressentitits = new Address();
        addressentitits.street_1 = street_1;
        addressentitits.street_2 = street_2;
        addressentitits.localidad = localidad;
        addressentitits.town = ciudad;
        await repositoriAddress.save(addressentitits);
      }


      let estatus = await repositoritypeU.findOne({ where: { descripcion: descripcion, value: value } })
      if (!estatus) {
        estatus = new userTypes();
        estatus.descripcion = descripcion;
        estatus.value = value;
        await repositoritypeU.save(estatus)
      }

      let usert = await repositoriuser.findOne({ where: [{ name_user: name_user }, { email: email }] })
      if (!usert) {
        usert = new users();
        usert.name_user = name_user;
        usert.lastname = lastname;
        usert.email = email;
        usert.password = password;
        usert.age = age;
        usert.phoneNumber = phoneNumber;
        usert.adress = addressentitits;
        usert.usertypes = estatus;
        await repositoriuser.save(usert)
        res.status(200).json(ResponseModel.successResponse("Usuario nuevo registrado"))

      } else {
        res.status(400).json(ResponseModel.errorResponse(400, "El usuario y/o correo electrónico ya está siendo utilizado"))
      }


    } catch (error) {
      console.log("Error interno: ", error)
      res.status(500).json(ResponseModel.errorResponse(500, `Error interno: ${error}`))
    }
  },

  actualizarDatos: async (req: Request, res: Response): Promise<void> => {
    try {

      const userId = parseInt(req.params.id, 10);

      if (isNaN(userId)) {
        res.status(400).json(ResponseModel.errorResponse(400, "El usuario no existe"));
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
        usuario.name_user = name_user;
        usuario.email = email;
        usuario.lastname = lastname;
        usuario.age = age;
        usuario.phoneNumber = phoneNumber;
        usuario.adress = direccion;

        await repositoriuser.save(usuario);

        console.log("Usuario ya actualizado");
        res.json(ResponseModel.successResponse(usuario));
        console.log("Usuario actualizado correctamente");
      } else {
        res.status(404).json(ResponseModel.errorResponse(400, "Usuario no encontrado"));
      }


    } catch (error) {
      console.error("Error al actualizar los datos:", error);
      res.status(500).json(ResponseModel.errorResponse(500, `Error interno: ${error}`));
    }
  },

  insertImagen: async (req: Request, res: Response): Promise<void> => {
    let id_user = Number(req.params.id_user);
    const { urlLocation } = req.body;
    let  funcionality = "PerfilUser"
      try {
      const usuario = await repositoriuser.findOne({ where: { id_user: id_user } });
      let  idUsedOn = String(id_user)
      let table = 'users'

      if(usuario){
        const conexion = new ImagenService();
        const postResp = await conexion.PostImage({ funcionality, urlLocation, idUsedOn }, table);
        console.log("Se guardó con éxito:", JSON.stringify(postResp));
      }else{
        res.status(400).json("No se encontro el usuario")
        console.log("No se encontro el usuario")
      }
      res.json({ message: "Imagen guardada con éxito" });
    } catch (e) {
      console.error("Error:", e );
      res.status(500).json({ message: "Error interno" }); 
    }
  },


  resetPassword: async (req: Request, res: Response): Promise<void> => {
    const correo = req.body.correo;
  
    if (!correo) {
      res.status(400).json({ message: 'El correo es obligatorio' });
      return;
    }
  
    try {
      const usuario = await repositoriuser.findOne({ where: { email: correo } });
  
      if (!usuario) {
        res.status(404).json({ message: 'Usuario no encontrado' });
        return;
      }
  
      const caracteres = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      const longitud = 6;
      let nuevaContrasena = '';
  
      for (let i = 0; i < longitud; i++) {
        nuevaContrasena += caracteres.charAt(Math.floor(Math.random() * caracteres.length));
      }
  
      const passwordHash = await bcryptjs.hash(nuevaContrasena, 8);
  
      // Actualiza la contraseña del usuario
      usuario.password = passwordHash;
      await repositoriuser.save(usuario);
  
      // Envía el correo
      await transporter.sendMail({
        from: `"Admin" <${gmail}>`,
        to: correo,
        subject: 'Nueva contraseña',
        html: `
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Nueva contraseña</title>
          <style>
            body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #1a3c86; }
            .container { inline-size: 80%; margin: 20px auto; }
            .header { background-color: #f5f5f5; padding: 20px; border-block-end: 1px solid #000000; }
            .content { padding: 20px; }
            .info-box { border: 1px solid #000000; padding: 10px; margin-block-end: 20px; background-color: #f9f9f9; }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">
              <h1>Nueva contraseña</h1>
              <p>Recientemente has solicitado restablecer tu contraseña. Tu nueva contraseña es:</p>
            </div>
            <div class="content">
              <div class="info-box">
                <h2>Contraseña actualizada</h2>
                <p>Su nueva contraseña es: <strong>${nuevaContrasena}</strong></p>
              </div>
              <div class="info-box">
                <h2>Recomendaciones</h2>
                <p>Cambia esta contraseña en tu perfil lo antes posible para garantizar tu seguridad.</p>
              </div>
            </div>
          </div>
        </body>
        </html>
        `,
      });
  
      res.status(200).json({ message: 'Nueva contraseña generada y enviada por correo.' });
    } catch (error) {
      console.error('Error al restablecer la contraseña:', error);
      res.status(500).json({ message: 'Error interno del servidor' });
    }
  }
  

};


export default controllerusuario;


