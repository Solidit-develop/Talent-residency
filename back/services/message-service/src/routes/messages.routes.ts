import controllermessages from "../controllers/menssage";
import { Router } from "express";

const router = Router();

router.get('/', controllermessages.ping);
router.get('/usuarios', controllermessages.usuarios);
router.post('/mensajes/:id_logued/:id_dest', controllermessages.conversacion)
router.get('/mensajes/:id_logued/:id_dest', controllermessages.recupMessages)
// router.get('/mensajes/:id_logued', controllermessages.reviewMesage)
router.get('/mensajes/:id_logued', controllermessages.metodo)

module.exports = router;