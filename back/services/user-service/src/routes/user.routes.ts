import { Router } from "express";
import usercontroller from '../controllers/consulta.controller'
// const usercontroller = require("../controllers/consulta.controller")

const router = Router();

router.get('/ping', usercontroller.ping);
router.get('/prueba', usercontroller.prueba);
router.post('/login', usercontroller.inicio_sesion);
router.post('/insertar', usercontroller.insertusuario);
router.put('/information/:id', usercontroller.actualizarDatos);
router.get('/todos', usercontroller.prueba);
router.post('/register', usercontroller.registroUsuario)
router.get('/validate/:token', usercontroller.verificacion)
module.exports = router;