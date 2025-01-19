import { Router } from "express";
import locationsService from '../service/locations-service';

const router = Router();

router.get('/states/:state/cities', locationsService.obtainCitiesByState);
router.get('/states', locationsService.obtainStates);
router.get('/ping', locationsService.ping);

module.exports = router;