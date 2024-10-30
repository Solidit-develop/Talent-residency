import controllersReview from "../controllers/reviews";
import { Router } from "express";

const router = Router();

router.get('/', controllersReview.ping);

router.get('/consulta', controllersReview.consulta);

module.exports = router;