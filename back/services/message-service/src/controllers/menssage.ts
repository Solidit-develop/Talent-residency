import { Request, Response } from "express";

import { users } from "../entitis/users";
import { AppDataSource } from "../database";
import { Messages } from "../entitis/messages";
import { Conversation } from "../entitis/conversation";
import { QueryBuilder, Timestamp } from "typeorm";

const repositoryuser = AppDataSource.getRepository(users);
const repsitorymesages = AppDataSource.getRepository(Messages);
const repositorycoversation = AppDataSource.getRepository(Conversation)



const controllermessages = {

    ping: async (req: Request, res: Response): Promise<void> => {
        res.send("pong");
    },

    usuarios: async (req: Request, res: Response): Promise<void> => {
        try {

            const usuarios = await repositoryuser.find();
            console.log(usuarios)
            res.status(200).json({ usuarios })

        } catch (error) {
            console.log(error)
            res.status(200).json({ message: "Hay un error dentro del servidor" })
        }

    },

    // inivcio de conversacion

    conversacion: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_logued, id_dest } = req.params;
            const { contect, sendDate, createdate } = req.body; // `sendDate` es la fecha de los mensajes
            const user_log = parseInt(id_logued);
            const user_des = parseInt(id_dest);

            // Buscar si ya existe la conversación entre los usuarios
            const existe = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                .leftJoinAndSelect("conversation.messages", "messages")
                .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued: user_log, id_destino: user_des })
                // .orWhere("(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)", { id_logued: user_log, id_destino: user_des })
                .getOne();

            // Buscar usuario logueado
            const usuario = await repositoryuser.findOne({ where: { id_user: user_log } });

            if (!usuario) {
                res.status(404).json({ message: "No se encontró el usuario" });
                return;
            }

            // Si no existe la conversación, crear una nueva
            if (!existe) {
                console.log("No existe la conversación, verificando senddate...");

                // Revisar si ya existe una conversación con `creationDate`
                const senddate = await repositorycoversation.createQueryBuilder("conversation")
                    .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                    .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued: user_log, id_destino: user_des })
                    .getOne();

                const creacion = senddate?.creationDate;

                // Si no existe `creationDate`, crear una nueva conversación
                if (!senddate?.creationDate) {
                    let conversation = new Conversation();
                    conversation.id_userOrigen = usuario;
                    conversation.id_userDestino = user_des;
                    conversation.creationDate = createdate; // Usar la fecha proporcionada en `createdate`

                    await repositorycoversation.save(conversation);
                    console.log("Nueva conversación creada:", conversation);

                    // Crear el mensaje asociado a la nueva conversación
                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                    mensaje.conversation = conversation;

                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);

                } else {
                    // Si ya existe la conversación pero tiene `creationDate`, crear un nuevo mensaje
                    console.log("Existe la conversación con creationDate, agregando mensaje...");

                    let mensaje = new Messages();
                    mensaje.contect = contect;
                    mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                    mensaje.conversation = senddate;

                    await repsitorymesages.save(mensaje);
                    console.log("Mensaje registrado con éxito:", mensaje);
                }


            } else {
                // Si ya existe la conversación, agregar el mensaje a la conversación existente
                console.log("Existe la conversación:", existe);

                let mensaje = new Messages();
                mensaje.contect = contect;
                mensaje.senddate = sendDate; // Convertir `sendDate` a Date
                mensaje.conversation = existe;

                await repsitorymesages.save(mensaje);
                console.log("Mensaje registrado con éxito:", mensaje);
            }

            res.status(200).json({ message: "Mensaje guardado con éxito" });
        } catch (error) {
            console.error("Error en el servidor:", error);
            res.status(500).json({ message: "Hay un error interno en el servidor" });
        }
    },

    recupMessages: async (req: Request, res: Response): Promise<void> => {

        try {
            let { id_logued, id_dest } = req.params


            const existe = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
                .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
                .where("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_dest })  // Condición 1
                .orWhere("(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)", { id_logued, id_destino: id_dest })  // Condición 2
                .getMany();

            console.log("Mensaje que se nos regres ")
            // console.log("(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)", { id_logued, id_destino: id_dest })

            const resultados = existe.map(Conversation => {
                return {
                    id_conversation: Conversation.id_conversation,
                    interactuan: {
                        nombre: Conversation.id_userOrigen.name_User,
                        id_dest: Conversation.id_userDestino
                    },
                    message: Conversation.messages
                }
            })

            res.status(200).json({ resultados })

        }
        catch (error) {
            console.log(error)
            res.status(500).json({ message: "Hay un error dentro del servidor" })
        }

    },




    reviewMesage: async (req: Request, res: Response): Promise<void> => {
        try {
            let { id_logued } = req.params;

            // Obtenemos las conversaciones relacionadas
            const relacion = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
                .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
                .leftJoinAndSelect("conversation.id_userDestino", "userDestino") // Relación con el usuario destino
                .where("conversation.idUserOrigenIdUser = :id_origen", { id_origen: id_logued })
                .orWhere("conversation.id_userDestino = :id_destino", { id_destino: id_logued })
                .getMany();

            const mensajes = [];

            const resultados = relacion.map(conversation => {
                return {
                    id_conversacion: conversation.id_conversation,
                    id_dest: conversation.id_userDestino,
                    id_origen: conversation.id_userOrigen.id_user
                };
            });

            const destino = [];
            const origen = [];

            const logueado = Number(id_logued);
            for (let k = 0; k < resultados.length; k++) {
                if (resultados[k].id_origen === logueado) {
                    destino.push(resultados[k].id_dest);
                } else {
                    origen.push(resultados[k].id_origen);
                }
            }

            let id_des;
            if (origen.length <= destino.length) {
                for (let j = 0; j < destino.length; j++) {
                    if (destino.includes(origen[j])) {
                        id_des = String(destino[j]);

                        let conversacion = await AppDataSource
                            .createQueryBuilder()
                            .select("conversation.id_conversation")
                            .from("conversation", "conversation")
                            .where(
                                "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                                "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                                { id_logued: id_logued, id_destino: id_des }
                            )
                            .getMany();

                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                        if (ides_conversaciones.length > 0) {
                            const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                                .leftJoinAndSelect("conversation.id_userDestino", "userDestino")
                                .leftJoinAndSelect("conversation.messages", "messages")
                                .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                                .orderBy("messages.id_messages", "DESC")
                                .limit(1)
                                .getMany();

                            const resultados = devuelto.map(conversation => ({
                                interactuan: [
                                    { id_origen: conversation.id_userOrigen.id_user },
                                    { id_destino: conversation.id_userDestino }
                                ],
                                message: conversation.messages
                            }));

                            mensajes.push(...resultados);
                        }
                    } else {
                        id_des = String(destino[j]);

                        let conversacion = await AppDataSource
                            .createQueryBuilder()
                            .select("conversation.id_conversation")
                            .from("conversation", "conversation")
                            .where(
                                "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)",
                                { id_logued: id_logued, id_destino: id_des }
                            )
                            .getMany();

                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                        if (ides_conversaciones.length > 0) {
                            const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                                .leftJoinAndSelect("conversation.id_userDestino", "userDestino")
                                .leftJoinAndSelect("conversation.messages", "messages")
                                .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                                .orderBy("messages.id_messages", "DESC")
                                .limit(1)
                                .getMany();

                            const resultados = devuelto.map(conversation => ({
                                interactuan: [
                                    { id_origen: conversation.id_userOrigen.id_user },
                                    { id_destino: conversation.id_userDestino }
                                ],
                                message: conversation.messages
                            }));

                            mensajes.push(...resultados);
                        }
                    }
                }
            }

            res.status(200).json(mensajes);

        } catch (error) {
            console.log(error);
            res.status(500).json({ message: "Hay un error dentro del servidor" });
        }
    },



    metodo: async (req: Request, res: Response): Promise<void> => {
        try {
            const { id_logued } = req.params;
            const mensajes = [];

            // Obtener los ID de destino de las conversaciones del usuario logueado

            const prueba = await AppDataSource
                .createQueryBuilder()
                .select("conversation.id_userDestino")
                .from("conversation", "conversation")
                .where("conversation.idUserOrigenIdUser = :id_logued", { id_logued })
                .getMany();







            let interaccion1 = await AppDataSource
                .createQueryBuilder()
                .select("conversation.id_userDestino")
                .from("conversation", "conversation")
                .where("conversation.idUserOrigenIdUser = :id_logued", { id_logued })
                .getMany();

            console.log("Interacción 1:", interaccion1);

            console.log("No se encontro relacion")


            let interaccion2 = await AppDataSource
                .createQueryBuilder()
                // .select("conversation.id_userOrigen")
                .from("conversation", "conversation")
                .where("conversation.id_userDestino = :id_destino", { id_destino: id_logued })
                .getMany();
            console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")
            console.log("Interacciones que existen cuando pasa en el rol logueado")
            console.log(interaccion1)
            console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")
            console.log("Interaccion donde es el destino")
            console.log(interaccion2)
            console.log("---------------------------------------.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-")





            const ides = interaccion1.map(conversation => conversation.id_userDestino);

            console.log("Todos los usuarios destinos:", ides);

            for (let i = 0; i < ides.length; i++) {
                console.log("Procesando id_userDestino:", ides[i]);

                // Obtener las conversaciones entre el usuario logueado y el id_userDestino actual
                let conversacion = await AppDataSource
                    .createQueryBuilder()
                    .select("conversation.id_conversation")
                    .from("conversation", "conversation")
                    .where(
                        "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                        "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                        { id_logued, id_destino: ides[i] }
                    )
                    .getMany();

                if (conversacion.length === 0) {
                    conversacion = await AppDataSource
                        .createQueryBuilder()
                        .select("conversation.id_conversation")
                        .from("conversation", "conversation")
                        .where(
                            "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                            "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                            { id_logued, id_destino: ides[i] }
                        )
                        .getMany();
                }

                console.log("Conversaciones encontradas:", conversacion);

                const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                if (ides_conversaciones.length > 0) {
                    // Consultar los mensajes más recientes de las conversaciones encontradas
                    const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                        .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                        .leftJoinAndSelect("conversation.messages", "messages")
                        .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                        .orderBy("messages.id_messages", "DESC")
                        .limit(1)
                        .getMany();

                    const resultados = devuelto.map(conversation => ({
                        interactuan: {
                            nombre: conversation.id_userOrigen.name_User,
                            id_dest: conversation.id_userDestino
                        },
                        message: conversation.messages
                    }));

                    mensajes.push(...resultados);
                }
            }

            console.log("Mensajes encontrados:", mensajes);
            res.status(200).json({ mensajes });
        } catch (error) {
            console.error("Error en el servidor:", error);
            res.status(500).json({ message: "Hay un error dentro del servidor" });
        }
    }


}

export default controllermessages;
