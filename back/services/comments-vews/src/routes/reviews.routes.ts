import controllersReview from "../controllers/reviews";
import { Router } from "express";

const router = Router();

router.get('/', controllersReview.ping);

router.post('/registro/:id_logued/:id_dest', controllersReview.registro);
router.get('/consulta/:id_logued/:id_dest', controllersReview.consultaUno);
router.get('/consulta/:id_logued',controllersReview.ConsultaTodos);
router.put('/edit/:id_logued/:id_dest',controllersReview.edit);
router.delete('/eliminar/:id_interaccion/:id_review',controllersReview.eliminar)

module.exports = router;