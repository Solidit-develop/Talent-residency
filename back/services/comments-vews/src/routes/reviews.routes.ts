import controllersReview from "../controllers/reviews";
import { Router } from "express";

const router = Router();

router.get('/', controllersReview.ping);

router.post('/registro/:id_logued/:id_dest', controllersReview.registro);
router.get('/consulta/:id_logued/:id_dest', controllersReview.consulta);


module.exports = router;