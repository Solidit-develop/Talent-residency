const { Router } = require("express");
const router = Router();
const controlleraddresses = require("../controllers/adresses.controller")
router.get('/states/:state/cities', controlleraddresses.municipio);
router.get('/states', controlleraddresses.estados);
router.get('/ping',controlleraddresses.ping)
// router.get('/ping', locationsService.ping);

module.exports = router