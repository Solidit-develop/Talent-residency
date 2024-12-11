
import { Router } from "express";
import controlleragrements from "../controllers/agrements";


const router= Router ();

router.get('/ping',controlleragrements.ping);
router.post('/agendar/:id_appointment/:id_provedor',controlleragrements.agregarAcuerdo);
router.get('/citas/:id_provedor',controlleragrements.serviciosProvedor);
router.put("/update/:id_agrement/:id_serviceStatus",controlleragrements.updateAgrement);
module.exports = router