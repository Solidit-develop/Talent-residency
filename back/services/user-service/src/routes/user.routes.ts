import { Router } from "express";
import usercontroller  from '../controllers/consulta.controller'
// const usercontroller = require("../controllers/consulta.controller")

const router= Router();

router.get('/ping', usercontroller.ping);
router.get('/prueba',usercontroller.usrRegis);
router.post('/login',usercontroller.inicio_sesion);
router.post('/insertar',usercontroller.insertusuario);
router.put('/actualizar/:id',usercontroller.actualizarDatos);
router.get('/todos',usercontroller.usrRegis);
router.post('/envioToken',usercontroller.registroUsuario)
router.get('/verificacion/:token',usercontroller.verificacion)
router.get('/miInfo/:email',usercontroller.miInfo);
module.exports= router;