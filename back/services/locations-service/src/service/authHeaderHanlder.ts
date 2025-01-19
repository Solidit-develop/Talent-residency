import { Request, Response, NextFunction } from "express";
import { getToken } from "./token_handler";

const authHeaderMiddleware = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = await getToken(); // Obtiene el token actualizado
        res.setHeader("Authorization", `Bearer ${token}`); // Agrega el header
        next();
    } catch (error) {
        console.error("Error al obtener el token:", error);
        res.status(500).json({ message: "Error al generar el token." });
    }
};

export default authHeaderMiddleware;
