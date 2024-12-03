import { Router } from "express";
import controllerAppointment from "../controllers/appointment.controllers";

const router = Router()

router.get("/ping",controllerAppointment.prueba);
router.post("/cita/:id_provider/:id_customer",controllerAppointment.cita);
router.post("/relacion/:id_provider/:id_customer/:id_appointment",controllerAppointment.cancelar);
router.get("/consulta/:id_provider",controllerAppointment.consulta)
router.put("/actualizar/:id_appointment",controllerAppointment.actualizar)

module.exports= router;