import { Router } from "express";
import controllerProvider from "../controllers/provedor.controllers";

const router = Router();

router.get('/correo',controllerProvider.infocomplete);
router.get('/comprovacion',controllerProvider.statusUsuario)
router.delete('/habilidad',controllerProvider.eliminarHabilidad);
module.exports= router
