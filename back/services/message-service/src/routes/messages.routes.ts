import controllermessages from "../controllers/menssage";
import { Router } from "express";

const router= Router();

router.get('/', controllermessages.ping);

module.exports = router;