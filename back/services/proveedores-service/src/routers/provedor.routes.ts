import { Router } from "express";
import controllerProvider from "../controllers/provedor.controllers";

const router = Router();

router.get('/ping', controllerProvider.ping)
router.post('/correo', controllerProvider.infocomplete);
router.get('/comprovacion', controllerProvider.statusUsuario)
router.delete('/habilidad', controllerProvider.eliminarHabilidad);
router.get('/services/baner/:email', controllerProvider.topCalificaciones);
router.get('/todos/services/feed', controllerProvider.provedores);
router.get('/croll/:beet/:twen', controllerProvider.scroll)
router.get('/user/:id', controllerProvider.providerByUserId)
router.get('/provider/:id', controllerProvider.providerByProviderId)
router.get('/users/profile/:id', controllerProvider.userProfile)
router.get('/provedores/:busqueda',controllerProvider.busqueda)

module.exports = router
