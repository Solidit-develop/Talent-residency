import { Router } from "express";
import controllerAppointment from "../controllers/appointment.controllers";

const router = Router()

router.get("/ping",controllerAppointment.prueba)

module.exports= router;