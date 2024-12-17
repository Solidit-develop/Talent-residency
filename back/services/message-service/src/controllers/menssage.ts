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
                        nombre: Conversation.id_userOrigen.name_user,
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

    getConversations: async (req: Request, res: Response) => {
        let { idLogged } = req.params;
    
        console.log("Busca las conversaciones de id: ", idLogged);
    
        const conversations = await repositorycoversation.createQueryBuilder("conversation")
            .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
            .leftJoinAndSelect("conversation.messages", "messages")
            .leftJoinAndSelect("conversation.id_userOrigen", "users")
            .where("conversation.idUserOrigenIdUser = :id_origen", { id_origen: idLogged })
            .orWhere("conversation.id_userDestino=:id_destino", { id_destino: idLogged })
            .getMany();
    
        console.log("Conversations", JSON.stringify(conversations));
    
        let conversationsMappeds = conversations.map(conversation => {
            return {
                idConversation: conversation.id_conversation,
                idOrigen: conversation.id_userOrigen.id_user,
                idDestino: conversation.id_userDestino,
                content: conversation.messages.map((message) => {
                    return {
                        contenido: message.contect,
                        idMessage: message.id_messages,
                        date: message.senddate,
                        isSent: Number(idLogged) === conversation.id_userOrigen.id_user,
                        related: Number(idLogged) === conversation.id_userOrigen.id_user ? conversation.id_userDestino : conversation.id_userOrigen.id_user,
                    };
                })
            };
        });
    
        console.log("______________________\n");
        console.log("Ids recuperados: ", JSON.stringify(conversationsMappeds));
        console.log("______________________\n");
    
        const groupedByRelated = conversationsMappeds.reduce((acc, conversation) => {
            conversation.content.forEach((message) => {
                const relatedId = message.related;
    
                if (!acc[relatedId]) {
                    acc[relatedId] = [];
                }
    
                acc[relatedId].push(message);
            });
            return acc;
        }, {} as Record<number, any[]>);
    
        const lastMessages = Object.values(groupedByRelated).map(messages => {
            return messages
                .filter(message => message.date !== null)
                .sort((a, b) => new Date(b.date!).getTime() - new Date(a.date!).getTime())[0];
        }).filter(Boolean);
    
        console.log("Last Messages (before adding names): ", lastMessages);
    
        // Obtener el nombre del usuario para cada mensaje en lastMessages
        const lastMessagesWithNames = await Promise.all(
            lastMessages.map(async (message) => {
                const usuario = await repositoryuser.findOne({ where: { id_user: message.related } });
                return {
                    ...message,
                    nameUser: usuario ? usuario.name_user : "Usuario desconocido" // Asigna el nombre o un valor por defecto si el usuario no se encuentra
                };
            })
        );
    
        console.log("Response body with names: ", lastMessagesWithNames);
    
        res.status(200).json({ succes: true, code: 200, response: lastMessagesWithNames.reverse() });
    },    
    
    reviewMesage: async (req: Request, res: Response): Promise<void> => {
        try {
            let { id_logued } = req.params;

            // Obtenemos las conversaciones relacionadas
            const relacion = await repositorycoversation.createQueryBuilder("conversation")
                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")  // Relación con el usuario que inició la conversación
                .leftJoinAndSelect("conversation.messages", "messages")  // Relación con los mensajes
                .leftJoinAndSelect("conversation.id_userOrigen", "users")
                .where("conversation.idUserOrigenIdUser = :id_origen", { id_origen: id_logued })
                .orWhere("conversation.id_userDestino=:id_destino ", { id_destino: id_logued })
                .getMany()

            const mensajes = []

            // console.log(relacion)
            console.log("-----------------------------------------------------")

            const resultados = relacion.map(Conversation => {
                return {

                    id_conversacion: Conversation.id_conversation,
                    id_dest: Conversation.id_userDestino,
                    id_origen: Conversation.id_userOrigen.id_user

                }
            })

            console.log(resultados)

            const destino = []
            const origen = []

            const logueado = Number(id_logued)
            for (let item_resultado = 0; item_resultado < resultados.length; item_resultado++) {
                if (resultados[item_resultado].id_origen === logueado) {
                    console.log("Id logeado")
                    console.log(resultados[item_resultado].id_origen, logueado)
                    destino.push(resultados[item_resultado].id_dest)
                } else {
                    console.log("Id destino")
                    console.log(resultados[item_resultado].id_origen, logueado)
                    origen.push(resultados[item_resultado].id_origen)
                }
            }

            console.log("todos los id, origen")
            console.log(origen)
            console.log("todos los id que son destinos")
            console.log(destino)
            console.log("Mapeo de los resultados.......................................")

            //hacer parejas de busqueda para saber si se puede buscar en 2 sexiones o una

            let id_des: String;
            


            if (origen.length <= destino.length) {
                for (let item_destino = 0; item_destino < destino.length; item_destino++) {
                    if (destino.includes(origen[item_destino])) {
                        console.log("Este valor si esta en destino", destino[item_destino], "....................")
                        console.log("Se hace la busqueda doble intercambiando posiciones")
                        id_des = String(destino[item_destino])


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

                        console.log(conversacion)


                        //Hacer consulta directamente a los  mensajes con los valores obtuvidos
                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                        if (ides_conversaciones.length > 0) {
                            let destinoUserRelated = destino[item_destino];
                            // Consultar los mensajes más recientes de las conversaciones encontradas
                            const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                                .leftJoinAndSelect("conversation.messages", "messages")
                                .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                                .orderBy("messages.id_messages", "DESC")
                                .limit(1)
                                .getMany();

                                console.log(devuelto);

                            const resultados = devuelto.map(conversation => ({
                                interactuan: {
                                    nombre: conversation.id_userOrigen.name_user,
                                    id_dest: conversation.id_userDestino
                                },
                                message: conversation.messages,
                                related:destinoUserRelated
                            }));                            

                            mensajes.push(...resultados);
                        }
                        console.log(mensajes);


                    } else {
                        console.log("Este valor no esta en destino", destino[item_destino])
                        console.log("Se hace una consilta unitaria")

                        id_des = String(destino[item_destino])

                        let conversacion = await AppDataSource
                            .createQueryBuilder()
                            .select("conversation.id_conversation")
                            .from("conversation", "conversation")
                            .where(
                                "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)",
                                { id_logued: id_logued, id_destino: id_des }
                            )
                            .getMany();
                        console.log(conversacion)

                        //Hacer consulta directamente a los  mensajes con los valores obtuvidos

                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                        if (ides_conversaciones.length > 0) {
                            let destinoUserRelated = destino[item_destino];
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
                                        nombre: conversation.id_userOrigen.name_user,
                                        id_dest: conversation.id_userDestino
                                    },
                                    message: conversation.messages,
                                    related:destinoUserRelated
                                }));
    
                               
    
                                mensajes.push(...resultados);
                        }
                    }
                }
            } else {
                for (let item_origen = 0; item_origen < origen.length; item_origen++) {
                    if (origen.includes(destino[item_origen])) {
                        console.log("Este valor No esta en origen", origen[item_origen])
                        console.log("Se hace una consilta unitariae")
                        let conversacion = await AppDataSource
                            .createQueryBuilder()
                            .select("conversation.id_conversation")
                            .from("conversation", "conversation")
                            .where(
                                "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino)",
                                { id_logued: origen[item_origen], id_destino: id_logued }
                            )
                            .getMany();
                        console.log(conversacion)
                        //Hacer consulta directamente a los  mensajes con los valores obtuvidos
                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);
                        if (ides_conversaciones.length > 0) {
                            // Consultar los mensajes más recientes de las conversaciones encontradas
                            let destinoUserRelated = destino[item_origen];
                            const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                                .leftJoinAndSelect("conversation.messages", "messages")
                                .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                                .orderBy("messages.id_messages", "DESC")
                                .limit(1)
                                .getMany();
                                const resultados = devuelto.map(conversation => ({
                                    interactuan: {
                                        nombre: conversation.id_userOrigen.name_user,
                                        id_dest: conversation.id_userDestino
                                    },
                                    message: conversation.messages,
                                    related:destinoUserRelated
                                }));
    
                               
    
                                mensajes.push(...resultados);
                        }
                    } else {
                        console.log("Este valor Si esta en origen", origen[item_origen], ".....................")
                        console.log("Se hace la busqueda doble intercambiando posiciones")

                        id_des = String(destino[item_origen])

                        let conversacion = await AppDataSource
                            .createQueryBuilder()
                            .select("conversation.id_conversation")
                            .from("conversation", "conversation")
                            .where(
                                "(conversation.id_userOrigen = :id_logued AND conversation.id_userDestino = :id_destino) OR " +
                                "(conversation.id_userOrigen = :id_destino AND conversation.id_userDestino = :id_logued)",
                                { id_logued: id_logued, id_destino: origen[item_origen] }
                            )
                            .getMany();

                        //Hacer consulta directamente a los  mensajes con los valores obtuvidos

                        console.log(conversacion)

                        const ides_conversaciones = conversacion.map(conv => conv.id_conversation);

                        if (ides_conversaciones.length > 0) {
                            // Consultar los mensajes más recientes de las conversaciones encontradas
                            let destinoUserRelated = destino[item_origen];
                            const devuelto = await repositorycoversation.createQueryBuilder("conversation")
                                .leftJoinAndSelect("conversation.id_userOrigen", "userOrigen")
                                .leftJoinAndSelect("conversation.messages", "messages")
                                .where("messages.conversationIdConversation IN (:...conversationIds)", { conversationIds: ides_conversaciones })
                                .orderBy("messages.id_messages", "DESC")
                                .limit(1)
                                .getMany();
                                
                                
                                const resultados = devuelto.map(conversation => ({
                                    interactuan: {
                                        nombre: conversation.id_userOrigen.name_user,
                                        id_dest: conversation.id_userDestino
                                    },
                                    message: conversation.messages,
                                    related: conversation.id_userOrigen.id_user
                                }));
    
                            
    
                                mensajes.push(...resultados);
                        }
                    }
                }
            }

            res.status(200).json(mensajes)

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
                            nombre: conversation.id_userOrigen.name_user,
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
