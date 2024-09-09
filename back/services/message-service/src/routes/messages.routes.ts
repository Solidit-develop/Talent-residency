import controllermessages from "../controllers/menssage";
import { Router } from "express";

const router= Router();

router.get('/', controllermessages.ping);
router.get('/usuarios',controllermessages.usuarios);

module.exports = router;