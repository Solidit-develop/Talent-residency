import controllermessages from "../controllers/menssage";
import { Router } from "express";

const router= Router();

router.get('/', controllermessages.ping);
router.get('/usuarios',controllermessages.usuarios);
router.get('/prueba/:id_logued/:id_dest',controllermessages.conversacion)

module.exports = router;