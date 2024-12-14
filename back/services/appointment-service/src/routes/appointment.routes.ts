import { Router } from "express";
import controllerAppointment from "../controllers/appointment.controllers";

const router = Router()

router.get("/ping",controllerAppointment.prueba);
router.post("/cita/:id_provider/:id_customer",controllerAppointment.cita);
router.put("/cancelar/:id_provider",controllerAppointment.cancelar);
router.get("/consulta/:id_provider",controllerAppointment.consultaProv)
router.put("/actualizar/:id_appointment",controllerAppointment.actualizar)
router.get("/porUser/:id_user",controllerAppointment.consultaUser);
module.exports= router;