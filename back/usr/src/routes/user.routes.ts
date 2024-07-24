import { Router } from "express";
import usercontroller  from '../controllers/consulta.controller'
// const usercontroller = require("../controllers/consulta.controller")

const router= Router();

router.get('/prueba',usercontroller.inicio_sesion);
router.post('/prueba2',usercontroller.insertusuario);

module.exports= router;