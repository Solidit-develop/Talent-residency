import { Router } from "express";
import usercontroller  from '../controllers/consulta.controller'
// const usercontroller = require("../controllers/consulta.controller")

const router= Router();

router.get('/prueba',usercontroller.inicio_sesion);
router.post('/insertar',usercontroller.insertusuario);
router.put('/actualizar/:id',usercontroller.actualizarDatos);
router.get('/todos',usercontroller.prueba);
module.exports= router;