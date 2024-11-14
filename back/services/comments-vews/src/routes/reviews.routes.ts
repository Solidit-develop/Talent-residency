import controllersReview from "../controllers/reviews";
import { Router } from "express";

const router = Router();

router.get('/ping', controllersReview.ping);

router.post('/registro/:id_logued/:id_prov', controllersReview.registro);
router.get('/consulta/:id_logued/:id_prov', controllersReview.consultaUno);
router.get('/consulta/:id_logued',controllersReview.ConsultaTodos);
router.put('/edit/:id_logued/:id_dest',controllersReview.edit);
router.delete('/eliminar/:id_interaccion/:id_review',controllersReview.eliminar)

module.exports = router;