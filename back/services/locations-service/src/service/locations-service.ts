import { Request, Response } from "express";
import config from "../config";
import path from "path";
import { getToken } from "./token_handler";
import axios from "axios";
import { json } from "body-parser";



const locationsService = {
    ping:async (req:Request, res:Response): Promise<void> =>{
        res.send("pong")
    },

    obtainCitiesByState: async (req: Request, res: Response): Promise<void> => {
        try {
            const stateToFind = req.params.state;
            // Obtiene el token actualizado
            const token = await getToken();

            // Realiza la solicitud a la API externa con el token en el header
            const response = await axios.get(`https://www.universal-tutorial.com/api/cities/${stateToFind}`, {
                headers: {
                    "Authorization": `Bearer ${token}`, // Agrega el token al header
                },
            });

            // Envía la respuesta de la API externa
            res.json(response.data);
        } catch (error) {
            console.error("Error al realizar la solicitud:", error);

            // Manejo de errores
            if (error) {
                res.status(500).json({ message: "Error al realizar la solicitud" });
            }
        }
    },
    
    obtainStates: async (req: Request, res: Response): Promise<void> => {
        try {
            // Obtiene el token actualizado
            const token = await getToken();

            // Realiza la solicitud a la API externa con el token en el header
            const response = await axios.get(`https://www.universal-tutorial.com/api/states/Mexico`, {
                headers: {
                    "Authorization": `Bearer ${token}`, // Agrega el token al header
                },
            });

            // Envía la respuesta de la API externa
            res.json(response.data);
        } catch (error) {
            console.error("Error al realizar la solicitud:", error);

            // Manejo de errores
            if (error) {
                res.status(500).json({ message: "Error al realizar la solicitud" });
            }
        }
    },
};


export default locationsService;
