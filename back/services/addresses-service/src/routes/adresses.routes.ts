import { Router } from "express";
import controlleraddresses from "../controllers/adresses.controller"; // Asegúrate de que la ruta sea correcta

const router = Router();

// Definir las rutas y asignar los controladores
router.get('/states/:state/cities', controlleraddresses.municipio); // Asegúrate de que el controlador esté bien tipado
router.get('/states', controlleraddresses.estados);
router.get('/ping', controlleraddresses.ping);

export default router; // Uso de export para ES6
