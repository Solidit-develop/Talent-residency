import { Router } from "express";
import usercontroller  from '../controllers/consulta.controller'
// const usercontroller = require("../controllers/consulta.controller")

const router= Router();

router.get('/prueba',usercontroller.inicio_sesion);
router.post('/insertar',usercontroller.insertusuario);
router.put('/actualizar/:id',usercontroller.actualizarDatos);
router.get('/todos',usercontroller.prueba);
router.post('/envioToken',usercontroller.registroUsuario)
router.get('/verificacion/:token',usercontroller.verificacion)
module.exports= router;