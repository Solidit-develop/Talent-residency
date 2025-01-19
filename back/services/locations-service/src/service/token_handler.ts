// utils/tokenHandler.ts

import axios from "axios";
import config from "../config";

let cachedToken: string = "";

export const getToken = async (): Promise<string> => {
    if (!cachedToken) {
        try {
            // Si no hay token cacheado, realizamos la petición para obtenerlo
            console.log("URL_TOKEN: " + config.token_url_locations)
            const response = await axios.get(config.token_url_locations, {
                headers: {
                    "api-token": config.password_token_location,   // Contraseña en el header
                    "user-email": config.username_token_location,  // Usuario en el header
                },
            });

            // Guardamos el token en cache
            cachedToken = response.data.auth_token;

            return cachedToken;
        } catch (error) {
            console.error("Error al obtener el token:", error);
            throw new Error("No se pudo obtener el token.");
        }
    }

    // Si ya tenemos el token en cache, lo retornamos
    return cachedToken;
};
