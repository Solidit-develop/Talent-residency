import controllersReview from "../controllers/reviews";
import { Router } from "express";

const router = Router();

router.get('/ping', controllersReview.ping);

router.post('/comment_user/:id_user/:id_prov', controllersReview.registro_user);
router.get('/consulta/:id_user/:id_prov', controllersReview.consultaUno);
router.get('/consulta/:id_user',controllersReview.ConsultaTodos);
router.put('/edit/:id_user/:id_prov',controllersReview.edit); 
router.delete('/eliminar/:id_interaccion/:id_review',controllersReview.eliminar)
// router.post('/registro_prov/:id_user/:id_prov', controllersReview.registroProv);
module.exports = router;

